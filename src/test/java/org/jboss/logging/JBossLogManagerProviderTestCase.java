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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.jboss.logmanager.LogContext;
import org.jboss.logmanager.LogContextSelector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class JBossLogManagerProviderTestCase extends AbstractLoggerTestCase {
    private static final LogContextSelector DEFAULT_SELECTOR = LogContext.getLogContextSelector();
    private static final AtomicBoolean SET_LOG_MANAGER = new AtomicBoolean(true);

    private LogContext logContext;
    private TestHandler handler;
    private Logger logger;

    @BeforeAll
    public static void setup() {
        SET_LOG_MANAGER.set(System.getProperty("java.util.logging.manager") == null);
        if (SET_LOG_MANAGER.get()) {
            System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        }
        System.setProperty("org.jboss.logging.provider", "jboss");
    }

    @AfterAll
    public static void tearDown() {
        if (SET_LOG_MANAGER.get()) {
            System.clearProperty("java.util.logging.manager");
        }
    }

    @BeforeEach
    public void setupLogContext() {
        logContext = LogContext.create();
        LogContext.setLogContextSelector(() -> logContext);
        logger = Logger.getLogger(getClass());
        handler = createHandler(logContext, logger.getName());
    }

    @AfterEach
    public void closeLogContext() throws Exception {
        logContext.close();
        LogContext.setLogContextSelector(DEFAULT_SELECTOR);
    }

    @Test
    public void testMdc() {
        MDC.put("test.key", "value");
        Assertions.assertEquals("value", MDC.get("test.key"));
        Assertions.assertEquals("value", org.jboss.logmanager.MDC.get("test.key"));
    }

    @Test
    public void testNdc() {
        NDC.push("value1");
        NDC.push("value2");
        final String expectedValue = "value1.value2";
        Assertions.assertEquals(expectedValue, NDC.peek());
        Assertions.assertEquals(expectedValue, NDC.get());
        Assertions.assertEquals(2, NDC.getDepth());

        // Test the log manager values
        Assertions.assertEquals(expectedValue, org.jboss.logmanager.NDC.get());
        Assertions.assertEquals(2, org.jboss.logmanager.NDC.getDepth());

        // Pop the stack
        Assertions.assertEquals("value2", NDC.pop());
        Assertions.assertEquals(1, NDC.getDepth());
        Assertions.assertEquals("value1", NDC.get());
        Assertions.assertEquals("value1", org.jboss.logmanager.NDC.get());
        Assertions.assertEquals(1, org.jboss.logmanager.NDC.getDepth());
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
        return JBossLogManagerLogger.class;
    }

    private static TestHandler createHandler(final LogContext logContext, final String loggerName) {
        final TestHandler handler = new TestHandler();
        final java.util.logging.Logger julLogger = logContext.getLogger(loggerName);
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
