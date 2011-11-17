/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.logging;

import java.security.AccessController;
import java.security.PrivilegedAction;
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
                } else if ("log4j".equalsIgnoreCase(loggerProvider)) {
                    return tryLog4j(cl);
                } else if ("slf4j".equalsIgnoreCase(loggerProvider)) {
                    return trySlf4j();
                }
            }
        } catch (Throwable t) {
        }
        try {
            return tryJBossLogManager(cl);
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

    private static LoggerProvider tryLog4j(final ClassLoader cl) throws ClassNotFoundException {
        Class.forName("org.apache.log4j.LogManager", true, cl);
        // JBLOGGING-65 - slf4j can disguise itself as log4j.  Test for a class that slf4j doesn't provide.
        Class.forName("org.apache.log4j.Hierarchy", true, cl);
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
