/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2011 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logging;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;
import java.util.logging.LogManager;

final class LoggerProviders {
    static final String LOGGING_PROVIDER_KEY = "org.jboss.logging.provider";

    static final LoggerProvider PROVIDER = find();

    private static LoggerProvider find() {
        final LoggerProvider result = findProvider();
        // Log a debug message indicating which logger we are using
        result.getLogger("org.jboss.logging").debugf("Logging Provider: %s", result.getClass().getName());
        return result;
    }

    private static LoggerProvider findProvider() {
        // Since the impl classes refer to the back-end frameworks directly, if this classloader can't find the target
        // log classes, then it doesn't really matter if they're possibly available from the TCCL because we won't be
        // able to find it anyway
        final ClassLoader cl = LoggerProviders.class.getClassLoader();
        try {
            // Check the system property
            final String loggerProvider = AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(LOGGING_PROVIDER_KEY);
                }
            });
            if (loggerProvider != null) {
                if ("jboss".equalsIgnoreCase(loggerProvider)) {
                    return tryJBossLogManager(cl);
                } else if ("jdk".equalsIgnoreCase(loggerProvider)) {
                    return tryJDK();
                } else if ("log4j2".equalsIgnoreCase(loggerProvider)) {
                    return tryLog4j2(cl);
                } else if ("log4j".equalsIgnoreCase(loggerProvider)) {
                    return tryLog4j(cl);
                } else if ("slf4j".equalsIgnoreCase(loggerProvider)) {
                    return trySlf4j();
                }
            }
        } catch (Throwable t) {
            // nope...
        }

        // Next try for a service provider
        try {
            final ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class, cl);
            if (loader.iterator().hasNext()) {
                return loader.iterator().next();
            }
        } catch (Throwable ignore) {
            // TODO consider printing the stack trace as it should only happen once
        }

        // Finally search the class path
        try {
            return tryJBossLogManager(cl);
        } catch (Throwable t) {
            // nope...
        }
        try {
            // MUST try Log4j 2.x BEFORE Log4j 1.x because Log4j 2.x also passes Log4j 1.x test in some circumstances
            return tryLog4j2(cl);
        } catch (Throwable t) {
            // nope...
        }
        try {
            return tryLog4j(cl);
        } catch (Throwable t) {
            // nope...
        }
        try {
            // only use slf4j if Logback is in use
            Class.forName("ch.qos.logback.classic.Logger", false, cl);
            return trySlf4j();
        } catch (Throwable t) {
            // nope...
        }
        return tryJDK();
    }

    private static JDKLoggerProvider tryJDK() {
        return new JDKLoggerProvider();
    }

    private static LoggerProvider trySlf4j() {
        return new Slf4jLoggerProvider();
    }

    // JBLOGGING-95 - Add support for Log4j 2.x
    private static LoggerProvider tryLog4j2(final ClassLoader cl) throws ClassNotFoundException {
        Class.forName("org.apache.logging.log4j.Logger", true, cl);
        Class.forName("org.apache.logging.log4j.LogManager", true, cl);
        Class.forName("org.apache.logging.log4j.spi.AbstractLogger", true, cl);
        LoggerProvider provider = new Log4j2LoggerProvider();
        // if Log4j 2 has a bad implementation that doesn't extend AbstractLogger, we won't know until getting the first logger throws an exception
        provider.getLogger("org.jboss.logging");
        return provider;
    }

    private static LoggerProvider tryLog4j(final ClassLoader cl) throws ClassNotFoundException {
        Class.forName("org.apache.log4j.LogManager", true, cl);
        // JBLOGGING-65 - slf4j can disguise itself as log4j.  Test for a class that slf4j doesn't provide.
        // JBLOGGING-94 - JBoss Logging does not detect org.apache.logging.log4j:log4j-1.2-api:2.0
        Class.forName("org.apache.log4j.config.PropertySetter", true, cl);
        return new Log4jLoggerProvider();
    }

    private static LoggerProvider tryJBossLogManager(final ClassLoader cl) throws ClassNotFoundException {
        final Class<? extends LogManager> logManagerClass = LogManager.getLogManager().getClass();
        if (logManagerClass == Class.forName("org.jboss.logmanager.LogManager", false, cl)
                && Class.forName("org.jboss.logmanager.Logger$AttachmentKey", true, cl).getClassLoader() == logManagerClass.getClassLoader()) {
            return new JBossLogManagerProvider();
        }
        throw new IllegalStateException();
    }

    private LoggerProviders() {
    }
}
