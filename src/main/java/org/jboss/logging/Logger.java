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

import java.io.Serializable;

/**
 * An abstracted logging entry point.
 */
public abstract class Logger implements Serializable {

    private static final long serialVersionUID = 4232175575988879434L;

    private static final String FQCN = Logger.class.getName();

    /**
     * Levels used by this logging API.
     */
    public enum Level {
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE,
    }

    private final String name;
    private final String resourceBundleName;
    private final String prefix;

    /**
     * Construct a new instance.
     *
     * @param name the logger category name
     * @param resourceBundleName the resource bundle name or {@code null} for none
     * @param prefix the log message prefix string
     */
    protected Logger(final String name, final String resourceBundleName, final String prefix) {
        this.name = name;
        this.resourceBundleName = resourceBundleName;
        this.prefix = prefix;
    }

    /**
     * Return the name of this logger.
     *
     * @return The name of this logger.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the log message prefix string.
     *
     * @return the log message prefix string
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Check to see if the given level is enabled for this logger.
     *
     * @param level the level to check for
     * @return {@code true} if messages may be logged at the given level, {@code false} otherwise
     */
    public abstract boolean isEnabled(Level level);

    /**
     * Implementation log method (standard parameter formatting).
     *
     * @param level the level
     * @param loggerClassName the logger class name
     * @param message the message to log
     * @param parameters the parameters of the message
     * @param thrown the exception which was thrown, if any
     */
    protected abstract void doLog(Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown);

    /**
     * Implementation log method (printf formatting).
     *
     * @param level the level
     * @param loggerClassName the logger class name
     * @param format the format string to log
     * @param parameters the parameters of the message
     * @param thrown the exception which was thrown, if any
     */
    protected abstract void doLogf(Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown);
    
    /**
     * Check to see if the TRACE level is enabled for this logger.
     *
     * @return true if a {@link #trace(Object)} method invocation would pass the msg to the configured appenders, false
     *         otherwise.
     */
    public boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    /**
     * Issue a log msg with a level of TRACE.
     *
     * @param message the message
     */
    public void trace(Object message) {
        doLog(Level.TRACE, FQCN, message, null, null);
    }

    /**
     * Issue a log msg and throwable with a level of TRACE.
     *
     * @param message the message
     * @param t the throwable
     */
    public void trace(Object message, Throwable t) {
        doLog(Level.TRACE, FQCN, message, null, t);
    }

    /**
     * Issue a log msg and throwable with a level of TRACE and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void trace(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.TRACE, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of TRACE.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void trace(Object message, Object[] params) {
        doLog(Level.TRACE, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of TRACE.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void trace(Object message, Object[] params, Throwable t) {
        doLog(Level.TRACE, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of TRACE.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void trace(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.TRACE, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void tracef(String format, Object... params) {
        doLogf(Level.TRACE, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void tracef(String format, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracef(String format, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracef(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void tracef(Throwable t, String format, Object... params) {
        doLogf(Level.TRACE, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void tracef(Throwable t, String format, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracef(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of TRACE.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracef(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Check to see if the DEBUG level is enabled for this logger.
     *
     * @return true if a {@link #debug(Object)} method invocation would pass the msg to the configured appenders, false
     *         otherwise.
     */
    public boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    /**
     * Issue a log message with a level of DEBUG.
     *
     * @param message the message
     */
    public void debug(Object message) {
        doLog(Level.DEBUG, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of DEBUG.
     *
     * @param message the message
     * @param t the throwable
     */
    public void debug(Object message, Throwable t) {
        doLog(Level.DEBUG, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of DEBUG and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void debug(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.DEBUG, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of DEBUG.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void debug(Object message, Object[] params) {
        doLog(Level.DEBUG, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of DEBUG.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void debug(Object message, Object[] params, Throwable t) {
        doLog(Level.DEBUG, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of DEBUG.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void debug(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.DEBUG, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void debugf(String format, Object... params) {
        doLogf(Level.DEBUG, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void debugf(String format, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugf(String format, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugf(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void debugf(Throwable t, String format, Object... params) {
        doLogf(Level.DEBUG, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void debugf(Throwable t, String format, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugf(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of DEBUG.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugf(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Check to see if the INFO level is enabled for this logger.
     *
     * @return true if a {@link #info(Object)} method invocation would pass the msg to the configured appenders, false
     *         otherwise.
     */
    public boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    /**
     * Issue a log message with a level of INFO.
     *
     * @param message the message
     */
    public void info(Object message) {
        doLog(Level.INFO, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of INFO.
     *
     * @param message the message
     * @param t the throwable
     */
    public void info(Object message, Throwable t) {
        doLog(Level.INFO, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of INFO and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void info(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.INFO, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of INFO.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void info(Object message, Object[] params) {
        doLog(Level.INFO, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of INFO.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void info(Object message, Object[] params, Throwable t) {
        doLog(Level.INFO, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of INFO.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void info(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.INFO, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void infof(String format, Object... params) {
        doLogf(Level.INFO, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void infof(String format, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infof(String format, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infof(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void infof(Throwable t, String format, Object... params) {
        doLogf(Level.INFO, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void infof(Throwable t, String format, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infof(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of INFO.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infof(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a log message with a level of WARN.
     *
     * @param message the message
     */
    public void warn(Object message) {
        doLog(Level.WARN, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of WARN.
     *
     * @param message the message
     * @param t the throwable
     */
    public void warn(Object message, Throwable t) {
        doLog(Level.WARN, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of WARN and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void warn(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.WARN, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of WARN.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void warn(Object message, Object[] params) {
        doLog(Level.WARN, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of WARN.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void warn(Object message, Object[] params, Throwable t) {
        doLog(Level.WARN, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of WARN.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void warn(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.WARN, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void warnf(String format, Object... params) {
        doLogf(Level.WARN, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void warnf(String format, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnf(String format, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnf(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void warnf(Throwable t, String format, Object... params) {
        doLogf(Level.WARN, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void warnf(Throwable t, String format, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnf(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of WARN.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnf(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a log message with a level of ERROR.
     *
     * @param message the message
     */
    public void error(Object message) {
        doLog(Level.ERROR, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of ERROR.
     *
     * @param message the message
     * @param t the throwable
     */
    public void error(Object message, Throwable t) {
        doLog(Level.ERROR, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of ERROR and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void error(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.ERROR, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of ERROR.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void error(Object message, Object[] params) {
        doLog(Level.ERROR, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of ERROR.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void error(Object message, Object[] params, Throwable t) {
        doLog(Level.ERROR, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of ERROR.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void error(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.ERROR, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void errorf(String format, Object... params) {
        doLogf(Level.ERROR, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void errorf(String format, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorf(String format, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorf(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void errorf(Throwable t, String format, Object... params) {
        doLogf(Level.ERROR, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void errorf(Throwable t, String format, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorf(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of ERROR.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorf(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a log message with a level of FATAL.
     *
     * @param message the message
     */
    public void fatal(Object message) {
        doLog(Level.FATAL, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of FATAL.
     *
     * @param message the message
     * @param t the throwable
     */
    public void fatal(Object message, Throwable t) {
        doLog(Level.FATAL, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of FATAL and a specific logger class name.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void fatal(String loggerFqcn, Object message, Throwable t) {
        doLog(Level.FATAL, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters with a level of FATAL.
     *
     * @param message the message
     * @param params the message parameters
     */
    public void fatal(Object message, Object[] params) {
        doLog(Level.FATAL, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of FATAL.
     *
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void fatal(Object message, Object[] params, Throwable t) {
        doLog(Level.FATAL, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable with a level of FATAL.
     *
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void fatal(String loggerFqcn, Object message, Object[] params, Throwable t) {
        doLog(Level.FATAL, loggerFqcn, message, params, t);
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void fatalf(String format, Object... params) {
        doLogf(Level.FATAL, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void fatalf(String format, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalf(String format, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalf(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void fatalf(Throwable t, String format, Object... params) {
        doLogf(Level.FATAL, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void fatalf(Throwable t, String format, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalf(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message with a level of FATAL.
     *
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalf(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param message the message
     */
    public void log(Level level, Object message) {
        doLog(level, FQCN, message, null, null);
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param message the message
     * @param params the message parameters
     */
    public void log(Level level, Object message, Object[] params) {
        doLog(level, FQCN, message, params, null);
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param message the message
     * @param params the message parameters
     * @param t the throwable cause
     */
    public void log(Level level, Object message, Object[] params, Throwable t) {
        doLog(level, FQCN, message, params, t);
    }

    /**
     * Log a message at the given level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param message the message
     * @param params the message parameters
     * @param t the throwable cause
     */
    public void log(String loggerFqcn, Level level, Object message, Object[] params, Throwable t) {
        doLog(level, loggerFqcn, message, params, t);
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void logf(Level level, String format, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logf(Level level, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logf(Level level, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the message parameters
     */
    public void logf(Level level, String format, Object... params) {
        doLogf(level, FQCN, format, params, null);
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void logf(Level level, Throwable t, String format, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logf(Level level, Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logf(Level level, Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the message parameters
     */
    public void logf(Level level, Throwable t, String format, Object... params) {
        doLogf(level, FQCN, format, params, t);
    }

    /**
     * Log a message at the given level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the sole parameter
     */
    public void logf(String loggerFqcn, Level level, Throwable t, String format, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, loggerFqcn, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logf(String loggerFqcn, Level level, Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, loggerFqcn, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logf(String loggerFqcn, Level level, Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLogf(level, loggerFqcn, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Log a message at the given level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param t the throwable cause
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the message parameters
     */
    public void logf(String loggerFqcn, Level level, Throwable t, String format, Object... params) {
        doLogf(level, loggerFqcn, format, params, t);
    }

    /**
     * Read resolver; replaces deserialized instance with a canonical instance.
     *
     * @return the canonical logger instance
     */
    protected final Object writeReplace() {
        return new SerializedLogger(name, resourceBundleName, prefix);
    }

    /**
     * Get a logger instance with the given name using the given resource bundle (if supported by the underlying
     * framework).
     *
     * @param name the logger category name
     * @param resourceBundleName the resource bundle name
     *
     * @return the logger
     */
    public static Logger getI18nLogger(String name, String resourceBundleName) {
        return LoggerProviders.PROVIDER.getLogger(name, resourceBundleName, null);
    }

    /**
     * Get a logger instance with the given name using the given resource bundle (if supported by the underlying
     * framework).
     *
     * @param name the logger category name
     * @param resourceBundleName the resource bundle name
     * @param prefix the log message ID subsystem prefix to use
     *
     * @return the logger
     */
    public static Logger getI18nLogger(String name, String resourceBundleName, String prefix) {
        return LoggerProviders.PROVIDER.getLogger(name, resourceBundleName, prefix);
    }

    /**
     * Get a logger instance with the given name using the given resource bundle (if supported by the underlying
     * framework).
     *
     * @param clazz the class whose name will be used as the logger category name
     * @param resourceBundleName the resource bundle name
     *
     * @return the logger
     */
    public static Logger getI18nLogger(Class<?> clazz, String resourceBundleName) {
        return getI18nLogger(clazz.getName(), resourceBundleName);
    }

    /**
     * Get a Logger instance given the logger name.
     *
     * @param name the logger name
     *
     * @return the logger
     */
    public static Logger getLogger(String name) {
        return getI18nLogger(name, null);
    }

    /**
     * Get a Logger instance given the logger name with the given suffix.
     * <p/>
     * <p>This will include a logger separator between logger name and suffix.
     *
     * @param name the logger name
     * @param suffix a suffix to append to the logger name
     *
     * @return the logger
     */
    public static Logger getLogger(String name, String suffix) {
        return getLogger(name == null || name.length() == 0 ? suffix : name + "." + suffix);
    }

    /**
     * Get a Logger instance given the name of a class. This simply calls create(clazz.getName()).
     *
     * @param clazz the Class whose name will be used as the logger name
     *
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Get a Logger instance given the name of a class with the given suffix.
     * <p/>
     * <p>This will include a logger separator between logger name and suffix
     *
     * @param clazz the Class whose name will be used as the logger name
     * @param suffix a suffix to append to the logger name
     *
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz, String suffix) {
        return getLogger(clazz.getName(), suffix);
    }
}