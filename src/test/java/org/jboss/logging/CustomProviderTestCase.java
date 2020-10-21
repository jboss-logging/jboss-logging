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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class CustomProviderTestCase {

    @Test
    public void testLogger() {
        final Logger logger = Logger.getLogger(CustomProviderTestCase.class);
        Assertions.assertEquals(TestLogger.class, logger.getClass());
    }

    public static class TestProvider extends AbstractMdcLoggerProvider implements LoggerProvider {

        @Override
        public Logger getLogger(final String name) {
            return new TestLogger(name);
        }
    }

    static class TestLogger extends Logger {

        /**
         * Construct a new instance.
         *
         * @param name the logger category name
         */
        protected TestLogger(final String name) {
            super(name);
        }

        @Override
        protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {

        }

        @Override
        protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {

        }

        @Override
        public boolean isEnabled(final Level level) {
            return true;
        }
    }
}
