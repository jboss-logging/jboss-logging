/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import java.util.logging.LogManager;

final class LoggerProviders {
    static final LoggerProvider PROVIDER = findProvider();

    private static LoggerProvider findProvider() {
        final LogManager jdkLogManager = LogManager.getLogManager();
        if (jdkLogManager.getClass().getName().equals("org.jboss.logmanager.LogManager")) {
            return new JBossLogManagerProvider();
        }
        final ClassLoader cl = getClassLoader();
        try {
            Class.forName("org.apache.log4j.LogManager", true, cl);
            return new Log4jLoggerProvider();
        } catch (Throwable t) {
            // nope...
        }
        try {
            // only use slf4j if Logback is in use
            Class.forName("ch.qos.logback.classic.Logger", false, cl);
            return new Slf4jLoggerProvider();
        } catch (Throwable t) {
            // nope...
        }
        return new JDKLoggerProvider();
    }

    private static ClassLoader getClassLoader() {
        // Since the impl classes refer to the back-end frameworks directly, if this classloader can't find the target
        // log classes, then it doesn't really matter if they're possibly available from the TCCL because we won't be
        // able to find it anyway
        return LoggerProviders.class.getClassLoader();
    }

    private LoggerProviders() {
    }
}
