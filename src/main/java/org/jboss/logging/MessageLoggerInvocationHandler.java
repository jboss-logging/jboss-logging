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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class MessageLoggerInvocationHandler extends MessageBundleInvocationHandler {

    private final Logger logger;

    MessageLoggerInvocationHandler(final Class<?> type, final String category) {
        this(type.getAnnotation(MessageLogger.class), category);
    }

    private MessageLoggerInvocationHandler(final MessageLogger messageLogger, final String category) {
        super(messageLogger.projectCode());
        logger = Logger.getLogger(category);
    }

    public Object invoke(final Object proxy, final Method method, Object[] args) throws Throwable {
        final Message message = method.getAnnotation(Message.class);
        if (message == null) {
            // nothing to do...
            return null;
        }
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final LogMessage logMessage = method.getAnnotation(LogMessage.class);
        if (logMessage != null) {

            try {
                // See if it's a basic logger method
                if (method.getDeclaringClass().equals(BasicLogger.class)) {
                    // doesn't cover overrides though!
                    return method.invoke(logger, args);
                }

                // it's a log message
                final Logger.Level level = logMessage.level();
                if (logger.isEnabled(level)) {
                    String formatString = getFormatString(message);
                    if (formatString == null) {
                        return null;
                    }
                    ArrayList<Object> newArgs = new ArrayList<Object>();
                    Throwable cause = extractCause(parameterAnnotations, args, newArgs);
                    final Message.Format format = message.format();
                    switch (format) {
                        case PRINTF: {
                            logger.logf(level, cause, formatString, newArgs.toArray());
                            return null;
                        }
                        case MESSAGE_FORMAT: {
                            logger.logv(level, cause, formatString, newArgs.toArray());
                            return null;
                        }
                        default: {
                            return null;
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        } else {
            return super.invoke(proxy, method, args);
        }
        return null;
    }
}
