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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class JDKLogger extends Logger {

    private static final long serialVersionUID = 2563174097983721393L;

    @SuppressWarnings({ "NonConstantLogger" })
    private transient final java.util.logging.Logger logger;

    public JDKLogger(final String name) {
        super(name);
        logger = java.util.logging.Logger.getLogger(name);
    }

    protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
        if (isEnabled(level)) {
            final JBossLogRecord rec = new JBossLogRecord(translate(level), String.valueOf(message), loggerClassName);
            if (thrown != null) rec.setThrown(thrown);
            rec.setLoggerName(getName());
            rec.setParameters(parameters);
            rec.setResourceBundleName(logger.getResourceBundleName());
            rec.setResourceBundle(logger.getResourceBundle());
            logger.log(rec);
        }
    }

    protected void doLogf(final Level level, final String loggerClassName, String format, final Object[] parameters, final Throwable thrown) {
        if (isEnabled(level)) {
            final ResourceBundle resourceBundle = logger.getResourceBundle();
            if (resourceBundle != null) try {
                format = resourceBundle.getString(format);
            } catch (MissingResourceException e) {
                // ignore
            }
            final String msg = parameters == null ? String.format(format) : String.format(format, parameters);
            final JBossLogRecord rec = new JBossLogRecord(translate(level), msg, loggerClassName);
            if (thrown != null) rec.setThrown(thrown);
            rec.setLoggerName(getName());
            rec.setResourceBundleName(logger.getResourceBundleName());
            // we've done all the business
            rec.setResourceBundle(null);
            rec.setParameters(null);
            logger.log(rec);
        }
    }

    private static java.util.logging.Level translate(final Level level) {
        if (level != null) switch (level) {
            case FATAL: return JDKLevel.FATAL;
            case ERROR: return JDKLevel.ERROR;
            case WARN:  return JDKLevel.WARN;
            case INFO:  return JDKLevel.INFO;
            case DEBUG: return JDKLevel.DEBUG;
            case TRACE: return JDKLevel.TRACE;
        }
        return JDKLevel.ALL;
    }

    public boolean isEnabled(final Level level) {
        return logger.isLoggable(translate(level));
    }
}
