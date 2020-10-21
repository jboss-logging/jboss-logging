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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
abstract class AbstractLoggerTestCase {

    @AfterAll
    public static void clearProviderProperty() {
        System.clearProperty("org.jboss.logging.provider");
    }

    @AfterEach
    public void clearDiagnostics() {
        MDC.clear();
        NDC.clear();
    }

    @Test
    public void testLogger() {
        Assertions.assertEquals(getLoggerClass(), getLogger().getClass());
    }

    @Test
    public void testLog() {
        for (Logger.Level level : Logger.Level.values()) {
            testLog(level);
        }
    }

    @Test
    public void testTrace() {
        getLogger().trace("Test log level TRACE");
        testLog("Test log level TRACE", Logger.Level.TRACE);
    }

    @Test
    public void testDebug() {
        getLogger().debug("Test log level DEBUG");
        testLog("Test log level DEBUG", Logger.Level.DEBUG);
    }

    @Test
    public void testInfo() {
        getLogger().info("Test log level INFO");
        testLog("Test log level INFO", Logger.Level.INFO);
    }

    @Test
    public void testWarn() {
        getLogger().warn("Test log level WARN");
        testLog("Test log level WARN", Logger.Level.WARN);
    }

    @Test
    public void testError() {
        getLogger().error("Test log level ERROR");
        testLog("Test log level ERROR", Logger.Level.ERROR);
    }

    @Test
    public void testFatal() {
        getLogger().fatal("Test log level FATAL");
        testLog("Test log level FATAL", Logger.Level.FATAL);
    }

    abstract void testLog(Logger.Level level);

    abstract void testLog(String msg, Logger.Level level);

    abstract Logger getLogger();

    abstract Class<? extends Logger> getLoggerClass();
}
