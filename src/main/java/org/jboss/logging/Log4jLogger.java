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

import java.text.MessageFormat;

final class Log4jLogger extends Logger {

    private static final long serialVersionUID = -5446154366955151335L;

    private final org.apache.log4j.Logger logger;

    Log4jLogger(final String name) {
        super(name);
        logger = org.apache.log4j.Logger.getLogger(name);
    }

    public boolean isEnabled(final Level level) {
        final org.apache.log4j.Level l = translate(level);
        return logger.isEnabledFor(l) && l.isGreaterOrEqual(logger.getEffectiveLevel());
    }

    protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
        final org.apache.log4j.Level translatedLevel = translate(level);
        if (logger.isEnabledFor(translatedLevel)) {
            logger.log(loggerClassName, translatedLevel, parameters == null || parameters.length == 0 ? message : MessageFormat.format(String.valueOf(message), parameters), thrown);
        }
    }

    protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
        final org.apache.log4j.Level translatedLevel = translate(level);
        if (logger.isEnabledFor(translatedLevel)) {
            logger.log(loggerClassName, translatedLevel, parameters == null ? String.format(format) : String.format(format, parameters), thrown);
        }
    }

    private static org.apache.log4j.Level translate(final Level level) {
        if (level != null) switch (level) {
            case FATAL: return org.apache.log4j.Level.FATAL;
            case ERROR: return org.apache.log4j.Level.ERROR;
            case WARN:  return org.apache.log4j.Level.WARN;
            case INFO:  return org.apache.log4j.Level.INFO;
            case DEBUG: return org.apache.log4j.Level.DEBUG;
            case TRACE: return org.apache.log4j.Level.TRACE;
        }
        return org.apache.log4j.Level.ALL;
    }
}
