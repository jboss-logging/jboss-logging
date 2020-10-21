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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Slf4jProviderTestCase extends AbstractLoggerTestCase {
    private TestAppender appender;
    private Logger logger;

    @BeforeAll
    public static void setup() {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @BeforeEach
    public void setupLogContext() {
        logger = Logger.getLogger(getClass());
        appender = createHandler(logger.getName());
    }

    @AfterEach
    public void removeAppender() {
        ch.qos.logback.classic.Logger lbLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(logger.getName());
        lbLogger.detachAppender(appender);
        appender.stop();
    }

    @Test
    public void testMdc() {
        MDC.put("test.key", "value");
        Assertions.assertEquals("value", MDC.get("test.key"));
        Assertions.assertEquals("value", org.slf4j.MDC.get("test.key"));
    }

    @Test
    public void testNdc() {
        NDC.push("value1");
        NDC.push("value2");
        Assertions.assertEquals("value2", NDC.peek());
        Assertions.assertEquals("value1 value2", NDC.get());
        Assertions.assertEquals(2, NDC.getDepth());

        // Pop the stack
        Assertions.assertEquals("value2", NDC.pop());
        Assertions.assertEquals(1, NDC.getDepth());
        Assertions.assertEquals("value1", NDC.get());
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
        final ILoggingEvent event = appender.queue.poll();
        Assertions.assertNotNull(event, String.format("No record found for %s", level));
        final Logger.Level translatedLevel = level == Logger.Level.FATAL ? Logger.Level.ERROR : level;
        Assertions.assertEquals(translatedLevel.name(), event.getLevel().toString());
        Assertions.assertEquals(msg, event.getFormattedMessage());
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    Class<? extends Logger> getLoggerClass() {
        return Slf4jLocationAwareLogger.class;
    }

    private static TestAppender createHandler(final String loggerName) {
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final TestAppender appender = new TestAppender();
        appender.setContext(context);
        appender.start();

        ch.qos.logback.classic.Logger lbLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        lbLogger.addAppender(appender);
        lbLogger.setLevel(Level.ALL);
        return appender;
    }

    public static class TestAppender extends AppenderBase<ILoggingEvent> {
        final BlockingQueue<ILoggingEvent> queue = new LinkedBlockingQueue<>();

        @Override
        protected void append(final ILoggingEvent event) {
            queue.add(event);
        }
    }
}
