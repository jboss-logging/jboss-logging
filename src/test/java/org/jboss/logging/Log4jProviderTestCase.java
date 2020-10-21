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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Log4jProviderTestCase extends AbstractLoggerTestCase {
    private TestAppender appender;
    private Logger logger;

    @BeforeAll
    public static void setup() {
        System.setProperty("org.jboss.logging.provider", "log4j");
    }

    @BeforeEach
    public void setupLogContext() {
        logger = Logger.getLogger(getClass());
        appender = createAppender(logger.getName());
    }

    @AfterEach
    public void removeAppender() {
        org.apache.log4j.Logger.getLogger(logger.getName()).removeAppender(appender);
        appender.close();
    }

    @Test
    public void testMdc() {
        MDC.put("test.key", "value");
        Assertions.assertEquals("value", MDC.get("test.key"));
        Assertions.assertEquals("value", org.apache.log4j.MDC.get("test.key"));
    }

    @Test
    public void testNdc() {
        NDC.push("value1");
        NDC.push("value2");
        Assertions.assertEquals("value2", NDC.peek());
        Assertions.assertEquals("value1 value2", NDC.get());
        Assertions.assertEquals(2, NDC.getDepth());

        // Test the log manager values
        Assertions.assertEquals("value1 value2", org.apache.log4j.NDC.get());
        Assertions.assertEquals(2, org.apache.log4j.NDC.getDepth());

        // Pop the stack
        Assertions.assertEquals("value2", NDC.pop());
        Assertions.assertEquals(1, NDC.getDepth());
        Assertions.assertEquals("value1", NDC.get());
        Assertions.assertEquals("value1", org.apache.log4j.NDC.get());
        Assertions.assertEquals(1, org.apache.log4j.NDC.getDepth());
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
        final LoggingEvent event = appender.queue.poll();
        Assertions.assertNotNull(event, String.format("No record found for %s", level));
        Assertions.assertEquals(level.name(), event.getLevel().toString());
        Assertions.assertEquals(msg, event.getMessage());
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    Class<? extends Logger> getLoggerClass() {
        return Log4jLogger.class;
    }

    private static TestAppender createAppender(final String loggerName) {
        final TestAppender appender = new TestAppender();
        final org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(loggerName);
        log4jLogger.addAppender(appender);
        log4jLogger.setLevel(Level.ALL);
        return appender;
    }

    private static class TestAppender extends AppenderSkeleton {
        final BlockingQueue<LoggingEvent> queue = new LinkedBlockingQueue<>();

        @Override
        protected void append(final LoggingEvent loggingEvent) {
            queue.add(loggingEvent);
        }

        @Override
        public void close() throws SecurityException {
            queue.clear();
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    }
}
