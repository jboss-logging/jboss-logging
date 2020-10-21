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
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class JulProviderTestCase extends AbstractLoggerTestCase {
    private TestHandler handler;
    private Logger logger;

    @BeforeAll
    public static void setup() {
        System.setProperty("org.jboss.logging.provider", "jdk");
    }

    @BeforeEach
    public void setupLogContext() {
        logger = Logger.getLogger(getClass());
        handler = createHandler(logger.getName());
    }

    @AfterEach
    public void removeHandler() {
        java.util.logging.Logger.getLogger(logger.getName()).removeHandler(handler);
        handler.close();
    }

    @Test
    public void testMdc() {
        MDC.put("test.key", "value");
        Assertions.assertEquals("value", MDC.get("test.key"));
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
        final LogRecord logRecord = handler.queue.poll();
        Assertions.assertNotNull(logRecord, String.format("No record found for %s", level));
        Assertions.assertEquals(level.name(), logRecord.getLevel().getName());
        Assertions.assertEquals(msg, logRecord.getMessage());
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    Class<? extends Logger> getLoggerClass() {
        return JDKLogger.class;
    }

    private static TestHandler createHandler(final String loggerName) {
        final TestHandler handler = new TestHandler();
        final java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(loggerName);
        julLogger.addHandler(handler);
        julLogger.setLevel(Level.ALL);
        return handler;
    }

    private static class TestHandler extends Handler {
        final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

        @Override
        public void publish(final LogRecord record) {
            queue.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
            queue.clear();
        }
    }
}
