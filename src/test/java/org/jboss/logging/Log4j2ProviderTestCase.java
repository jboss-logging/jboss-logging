/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logging;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Log4j2ProviderTestCase extends AbstractLoggerTestCase {
    private TestAppender appender;
    private Logger logger;

    @BeforeAll
    public static void setup() {
        System.setProperty("org.jboss.logging.provider", "log4j2");
    }

    @BeforeEach
    public void setupLogContext() {
        logger = Logger.getLogger(getClass());
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        appender = (TestAppender) config.getAppenders().get("TestAppender");
    }

    @Test
    public void testMdc() {
        MDC.put("test.key", "value");
        Assertions.assertEquals("value", MDC.get("test.key"));
        Assertions.assertEquals("value", ThreadContext.get("test.key"));
    }

    @Test
    public void testNdc() {
        NDC.push("value1");
        NDC.push("value2");
        Assertions.assertEquals("value2", NDC.peek());
        // TODO (jrp) This is a weird case we should validate. NDC.get() does ThreadContext.peek() which doesn't seem
        // TODO (jrp) correct. The method should likely do ThreadContext.getImmutableStack().toString(). At least that
        // TODO (jrp) is what the NdcPatternConverter does.
        //Assertions.assertEquals("[value1, value2]", NDC.get());
        Assertions.assertEquals(2, NDC.getDepth());

        // Test the log manager values
        Assertions.assertEquals("[value1, value2]", ThreadContext.getImmutableStack().toString());
        Assertions.assertEquals(2, ThreadContext.getDepth());

        // Pop the stack
        Assertions.assertEquals("value2", NDC.pop());
        Assertions.assertEquals(1, NDC.getDepth());
        Assertions.assertEquals("value1", NDC.get());
        Assertions.assertEquals("value1", ThreadContext.peek());
        Assertions.assertEquals(1, ThreadContext.getDepth());
    }

    @Override
    void testLog(final Logger.Level level) {
        final String msg = String.format("Test log message at %s", level);
        logger.log(level, msg);

        Assertions.assertTrue(logger.isEnabled(level), String.format("Logger not enabled for level %s", level));
        testLog(msg, level);
    }

    @Override
    void testLog(final String msg, final Logger.Level level) {
        final LogEvent event = appender.queue.poll();
        Assertions.assertNotNull(event, String.format("No record found for %s", level));
        Assertions.assertEquals(level.name(), event.getLevel().toString());
        Assertions.assertEquals(msg, event.getMessage().getFormattedMessage());
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    Class<? extends Logger> getLoggerClass() {
        return Log4j2Logger.class;
    }

    @SuppressWarnings("unused")
    @Plugin(name = "TestAppender", category = "Core", elementType = "appender", printObject = true)
    public static class TestAppender extends AbstractAppender {
        final BlockingQueue<LogEvent> queue = new LinkedBlockingQueue<>();

        protected TestAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
            super(name, filter, layout, false, null);
        }

        @Override
        public void append(final LogEvent event) {
            queue.add(event.toImmutable());
        }

        @Override
        public void stop() {
            queue.clear();
            super.stop();
        }

        @PluginFactory
        public static TestAppender createAppender(@PluginAttribute("name") String name,
                                                  @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                  @PluginElement("Filter") final Filter filter,
                                                  @PluginAttribute("otherAttribute") String otherAttribute) {
            if (name == null) {
                LOGGER.error("No name provided for TestAppender");
                return null;
            }
            if (layout == null) {
                layout = PatternLayout.createDefaultLayout();
            }
            return new TestAppender(name, filter, layout);
        }
    }
}
