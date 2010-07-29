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

import org.jboss.logmanager.ExtLogRecord;

final class JBossLogManagerLogger extends Logger {

    private static final long serialVersionUID = 7429618317727584742L;

    private final org.jboss.logmanager.Logger logger;

    JBossLogManagerLogger(final String name, final org.jboss.logmanager.Logger logger) {
        super(name);
        this.logger = logger;
    }

    public boolean isEnabled(final Level level) {
        return logger.isLoggable(translate(level));
    }

    protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
        if (parameters == null) {
            logger.log(loggerClassName, translate(level), String.valueOf(message), thrown);
        } else {
            logger.log(loggerClassName, translate(level), String.valueOf(message), ExtLogRecord.FormatStyle.MESSAGE_FORMAT, parameters, thrown);
        }
    }

    protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
        if (parameters == null) {
            logger.log(loggerClassName, translate(level), format, thrown);
        } else {
            logger.log(loggerClassName, translate(level), format, ExtLogRecord.FormatStyle.PRINTF, parameters, thrown);
        }
    }

    private static java.util.logging.Level translate(final Level level) {
        if (level != null) switch (level) {
            case FATAL: return org.jboss.logmanager.Level.FATAL;
            case ERROR: return org.jboss.logmanager.Level.ERROR;
            case WARN:  return org.jboss.logmanager.Level.WARN;
            case INFO:  return org.jboss.logmanager.Level.INFO;
            case DEBUG: return org.jboss.logmanager.Level.DEBUG;
            case TRACE: return org.jboss.logmanager.Level.TRACE;
        }
        return org.jboss.logmanager.Level.ALL;
    }
}
