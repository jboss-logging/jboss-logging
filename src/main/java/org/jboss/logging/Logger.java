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
    
    private static String key(String prefix, int id) {
        StringBuilder b = new StringBuilder(32);
        b.append(prefix).append('-');
        if (id < 10) {
            b.append("0000");
        } else if (id < 100) {
            b.append("000");
        } else if (id < 1000) {
            b.append("00");
        } else if (id < 10000) {
            b.append('0');
        }
        b.append(id);
        return b.toString();
    }

    /**
     * Check to see if the {@code TRACE} level is enabled for this logger.
     *
     * @return {@code true} if messages logged at {@link Level#TRACE} may be accepted, {@code false} otherwise
     */
    public boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    /**
     * Issue a log message with a level of TRACE.
     *
     * @param message the message
     */
    public void trace(Object message) {
        doLog(Level.TRACE, FQCN, message, null, null);
    }

    /**
     * Issue a log message and throwable with a level of TRACE.
     *
     * @param message the message
     * @param t the throwable
     */
    public void trace(Object message, Throwable t) {
        doLog(Level.TRACE, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable with a level of TRACE and a specific logger class name.
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
     * @deprecated To log a message with parameters, using {@link #tracev(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #tracev(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void tracev(String format, Object... params) {
        doLog(Level.TRACE, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void tracev(String format, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracev(String format, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracev(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void tracev(Throwable t, String format, Object... params) {
        doLog(Level.TRACE, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void tracev(Throwable t, String format, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracev(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of TRACE using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracev(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of TRACE.
     *
     * @param key the numerical key
     */
    public void tracek(int key) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of TRACE.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void tracek(Throwable t, int key) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void tracekv(int key, Object... params) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void tracekv(int key, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracekv(int key, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracekv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void tracekv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void tracekv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracekv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracekv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void tracekf(int key, Object... params) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void tracekf(int key, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracekf(int key, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracekf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void tracekf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.TRACE)) {
            doLog(Level.TRACE, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void tracekf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void tracekf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of TRACE.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void tracekf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.TRACE)) {
            doLogf(Level.TRACE, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Check to see if the {@code DEBUG} level is enabled for this logger.
     *
     * @return {@code true} if messages logged at {@link Level#DEBUG} may be accepted, {@code false} otherwise
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
     * @deprecated To log a message with parameters, using {@link #debugv(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #debugv(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void debugv(String format, Object... params) {
        doLog(Level.DEBUG, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void debugv(String format, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugv(String format, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugv(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void debugv(Throwable t, String format, Object... params) {
        doLog(Level.DEBUG, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void debugv(Throwable t, String format, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugv(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of DEBUG using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugv(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of DEBUG.
     *
     * @param key the numerical key
     */
    public void debugk(int key) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of DEBUG.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void debugk(Throwable t, int key) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void debugkv(int key, Object... params) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void debugkv(int key, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugkv(int key, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugkv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void debugkv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void debugkv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugkv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugkv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void debugkf(int key, Object... params) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void debugkf(int key, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugkf(int key, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugkf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void debugkf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.DEBUG)) {
            doLog(Level.DEBUG, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void debugkf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void debugkf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of DEBUG.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void debugkf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.DEBUG)) {
            doLogf(Level.DEBUG, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Check to see if the {@code INFO} level is enabled for this logger.
     *
     * @return {@code true} if messages logged at {@link Level#INFO} may be accepted, {@code false} otherwise
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
     * @deprecated To log a message with parameters, using {@link #infov(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #infov(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void infov(String format, Object... params) {
        doLog(Level.INFO, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void infov(String format, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infov(String format, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infov(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void infov(Throwable t, String format, Object... params) {
        doLog(Level.INFO, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void infov(Throwable t, String format, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infov(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of INFO using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infov(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of INFO.
     *
     * @param key the numerical key
     */
    public void infok(int key) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of INFO.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void infok(Throwable t, int key) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void infokv(int key, Object... params) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void infokv(int key, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infokv(int key, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infokv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void infokv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void infokv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infokv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infokv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void infokf(int key, Object... params) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void infokf(int key, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infokf(int key, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infokf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void infokf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.INFO)) {
            doLog(Level.INFO, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void infokf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void infokf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of INFO.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void infokf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.INFO)) {
            doLogf(Level.INFO, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
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
     * @deprecated To log a message with parameters, using {@link #warnv(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #warnv(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void warnv(String format, Object... params) {
        doLog(Level.WARN, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void warnv(String format, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnv(String format, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnv(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void warnv(Throwable t, String format, Object... params) {
        doLog(Level.WARN, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void warnv(Throwable t, String format, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnv(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of WARN using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnv(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of WARN.
     *
     * @param key the numerical key
     */
    public void warnk(int key) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of WARN.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void warnk(Throwable t, int key) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void warnkv(int key, Object... params) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void warnkv(int key, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnkv(int key, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnkv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void warnkv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void warnkv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnkv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnkv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void warnkf(int key, Object... params) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void warnkf(int key, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnkf(int key, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnkf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void warnkf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.WARN)) {
            doLog(Level.WARN, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void warnkf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void warnkf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of WARN.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void warnkf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.WARN)) {
            doLogf(Level.WARN, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
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
     * @deprecated To log a message with parameters, using {@link #errorv(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #errorv(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void errorv(String format, Object... params) {
        doLog(Level.ERROR, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void errorv(String format, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorv(String format, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorv(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void errorv(Throwable t, String format, Object... params) {
        doLog(Level.ERROR, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void errorv(Throwable t, String format, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorv(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of ERROR using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorv(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of ERROR.
     *
     * @param key the numerical key
     */
    public void errork(int key) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of ERROR.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void errork(Throwable t, int key) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void errorkv(int key, Object... params) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void errorkv(int key, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorkv(int key, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorkv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void errorkv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void errorkv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorkv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorkv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void errorkf(int key, Object... params) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void errorkf(int key, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorkf(int key, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorkf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void errorkf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.ERROR)) {
            doLog(Level.ERROR, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void errorkf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void errorkf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of ERROR.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void errorkf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.ERROR)) {
            doLogf(Level.ERROR, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
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
     * @deprecated To log a message with parameters, using {@link #fatalv(String, Object...)} is recommended.
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
     * @deprecated To log a message with parameters, using {@link #fatalv(Throwable, String, Object...)} is recommended.
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
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param params the parameters
     */
    public void fatalv(String format, Object... params) {
        doLog(Level.FATAL, FQCN, format, params, null);
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void fatalv(String format, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalv(String format, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalv(String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void fatalv(Throwable t, String format, Object... params) {
        doLog(Level.FATAL, FQCN, format, params, t);
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void fatalv(Throwable t, String format, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalv(Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message with a level of FATAL using {@link java.text.MessageFormat}-style formatting.
     *
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalv(Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
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
     * Issue a log message from a numerical key with a level of FATAL.
     *
     * @param key the numerical key
     */
    public void fatalk(int key) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key with a level of FATAL.
     *
     * @param key the numerical key
     * @param t the throwable
     */
    public void fatalk(Throwable t, int key) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void fatalkv(int key, Object... params) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void fatalkv(int key, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalkv(int key, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalkv(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void fatalkv(Throwable t, int key, Object... params) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void fatalkv(Throwable t, int key, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalkv(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalkv(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param params the parameters
     */
    public void fatalkf(int key, Object... params) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void fatalkf(int key, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalkf(int key, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalkf(int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void fatalkf(Throwable t, int key, Object... params) {
        if (isEnabled(Level.FATAL)) {
            doLog(Level.FATAL, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void fatalkf(Throwable t, int key, Object param1) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void fatalkf(Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key with a level of FATAL.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void fatalkf(Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(Level.FATAL)) {
            doLogf(Level.FATAL, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
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
     * Issue a log message and throwable at the given log level.
     *
     * @param level the level
     * @param message the message
     * @param t the throwable
     */
    public void log(Level level, Object message, Throwable t) {
        doLog(level, FQCN, message, null, t);
    }

    /**
     * Issue a log message and throwable at the given log level and a specific logger class name.
     *
     * @param level the level
     * @param loggerFqcn the logger class name
     * @param message the message
     * @param t the throwable
     */
    public void log(Level level, String loggerFqcn, Object message, Throwable t) {
        doLog(level, loggerFqcn, message, null, t);
    }

    /**
     * Issue a log message with parameters at the given log level.
     *
     * @param level the level
     * @param message the message
     * @param params the message parameters
     * @deprecated To log a message with parameters, using {@link #logv(Level, String, Object...)} is recommended.
     */
    public void log(Level level, Object message, Object[] params) {
        doLog(level, FQCN, message, params, null);
    }

    /**
     * Issue a log message with parameters and a throwable at the given log level.
     *
     * @param level the level
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     * @deprecated To log a message with parameters, using {@link #logv(Level, Throwable, String, Object...)} is recommended.
     */
    public void log(Level level, Object message, Object[] params, Throwable t) {
        doLog(level, FQCN, message, params, t);
    }

    /**
     * Issue a log message with parameters and a throwable at the given log level.
     *
     * @param loggerFqcn the logger class name
     * @param level the level
     * @param message the message
     * @param params the message parameters
     * @param t the throwable
     */
    public void log(String loggerFqcn, Level level, Object message, Object[] params, Throwable t) {
        doLog(level, loggerFqcn, message, params, t);
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param format the message format string
     * @param params the parameters
     */
    public void logv(Level level, String format, Object... params) {
        doLog(level, FQCN, format, params, null);
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void logv(Level level, String format, Object param1) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logv(Level level, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logv(Level level, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param t the throwable
     * @param format the message format string
     * @param params the parameters
     */
    public void logv(Level level, Throwable t, String format, Object... params) {
        doLog(level, FQCN, format, params, t);
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param t the throwable
     * @param format the message format string
     * @param param1 the sole parameter
     */
    public void logv(Level level, Throwable t, String format, Object param1) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logv(Level level, Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a log message at the given log level using {@link java.text.MessageFormat}-style formatting.
     *
     * @param level the level
     * @param t the throwable
     * @param format the message format string
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logv(Level level, Throwable t, String format, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLog(level, FQCN, format, new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a formatted log message at the given log level.
     *
     * @param level the level
     * @param format the format string as per {@link String#format(String, Object...)} or resource bundle key therefor
     * @param params the parameters
     */
    public void logf(Level level, String format, Object... params) {
        doLogf(level, FQCN, format, params, null);
    }

    /**
     * Issue a formatted log message at the given log level.
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
     * Issue a formatted log message at the given log level.
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
     * Issue a formatted log message at the given log level.
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
     * Issue a formatted log message at the given log level.
     *
     * @param level the level
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param params the parameters
     */
    public void logf(Level level, Throwable t, String format, Object... params) {
        doLogf(level, FQCN, format, params, t);
    }

    /**
     * Issue a formatted log message at the given log level.
     *
     * @param level the level
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the sole parameter
     */
    public void logf(Level level, Throwable t, String format, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a formatted log message at the given log level.
     *
     * @param level the level
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logf(Level level, Throwable t, String format, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, format, new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a formatted log message at the given log level.
     *
     * @param level the level
     * @param t the throwable
     * @param format the format string, as per {@link String#format(String, Object...)}
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
     * Issue a log message from a numerical key at the given log level.
     *
     * @param level the level
     * @param key the numerical key
     */
    public void logk(Level level, int key) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), null, null);
        }
    }

    /**
     * Issue a log message from a numerical key at the given log level.
     *
     * @param level the level
     * @param key the numerical key
     * @param t the throwable
     */
    public void logk(Level level, Throwable t, int key) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), null, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param params the parameters
     */
    public void logkv(Level level, int key, Object... params) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void logkv(Level level, int key, Object param1) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logkv(Level level, int key, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logkv(Level level, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void logkv(Level level, Throwable t, int key, Object... params) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void logkv(Level level, Throwable t, int key, Object param1) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logkv(Level level, Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.text.MessageFormat}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logkv(Level level, Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param params the parameters
     */
    public void logkf(Level level, int key, Object... params) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), params, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void logkf(Level level, int key, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logkf(Level level, int key, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1, param2 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logkf(Level level, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, null);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param params the parameters
     */
    public void logkf(Level level, Throwable t, int key, Object... params) {
        if (isEnabled(level)) {
            doLog(level, FQCN, key(prefix, key), params, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the sole parameter
     */
    public void logkf(Level level, Throwable t, int key, Object param1) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     */
    public void logkf(Level level, Throwable t, int key, Object param1, Object param2) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1, param2 }, t);
        }
    }

    /**
     * Issue a parameterized log message from a numerical key at the given log level.  The resource value should be a
     * {@link java.util.Formatter}-style format string.
     *
     * @param level the level
     * @param t the throwable
     * @param key the numerical key
     * @param param1 the first parameter
     * @param param2 the second parameter
     * @param param3 the third parameter
     */
    public void logkf(Level level, Throwable t, int key, Object param1, Object param2, Object param3) {
        if (isEnabled(level)) {
            doLogf(level, FQCN, key(prefix, key), new Object[] { param1, param2, param3 }, t);
        }
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