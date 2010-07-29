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

final class Slf4jLogger extends Logger {

    private static final long serialVersionUID = 8685757928087758380L;

    private final org.slf4j.Logger logger;

    Slf4jLogger(final String name, final org.slf4j.Logger logger) {
        super(name);
        this.logger = logger;
    }

    public boolean isEnabled(final Level level) {
        if (level != null) switch (level) {
            case FATAL: return logger.isErrorEnabled();
            case ERROR: return logger.isErrorEnabled();
            case WARN:  return logger.isWarnEnabled();
            case INFO:  return logger.isInfoEnabled();
            case DEBUG: return logger.isDebugEnabled();
            case TRACE: return logger.isTraceEnabled();
        }
        return true;
    }

    protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
        if (isEnabled(level)) {
            final String text = parameters == null || parameters.length == 0 ? String.valueOf(message) : MessageFormat.format(String.valueOf(message), parameters);
            switch (level) {
                case FATAL:
                case ERROR:
                    logger.error(text, thrown);
                    return;
                case WARN:
                    logger.warn(text, thrown);
                    return;
                case INFO:
                    logger.info(text, thrown);
                    return;
                case DEBUG:
                    logger.debug(text, thrown);
                    return;
                case TRACE:
                    logger.trace(text, thrown);
                    return;
            }
        }
    }

    protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
        if (isEnabled(level)) {
            final String text = parameters == null ? String.format(format) : String.format(format, parameters);
            switch (level) {
                case FATAL:
                case ERROR:
                    logger.error(text, thrown);
                    return;
                case WARN:
                    logger.warn(text, thrown);
                    return;
                case INFO:
                    logger.info(text, thrown);
                    return;
                case DEBUG:
                    logger.debug(text, thrown);
                    return;
                case TRACE:
                    logger.trace(text, thrown);
                    return;
            }
        }
    }
}
