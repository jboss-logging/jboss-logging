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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class MessageBundleInvocationHandler implements InvocationHandler {

    private final String projectCode;

    protected MessageBundleInvocationHandler(final String projectCode) {
        this.projectCode = projectCode;
    }

    protected MessageBundleInvocationHandler(final Class<?> type) {
        this(type.getAnnotation(MessageBundle.class));
    }

    protected MessageBundleInvocationHandler(final MessageBundle messageBundle) {
        this(messageBundle != null ? messageBundle.projectCode() : null);
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Message message = method.getAnnotation(Message.class);
        if (message == null) {
            // nothing to do...
            return null;
        }
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        ArrayList<Object> newArgs = new ArrayList<Object>();
        Throwable cause = extractCause(parameterAnnotations, args, newArgs);
        String result;
        switch (message.format()) {
            case PRINTF: {
                result = String.format(getFormatString(message), newArgs.toArray());
                break;
            }
            case MESSAGE_FORMAT: {
                result = MessageFormat.format(getFormatString(message), newArgs.toArray());
                break;
            }
            default: throw new IllegalStateException();
        }
        final Class<?> returnType = method.getReturnType();
        if (Throwable.class.isAssignableFrom(returnType)) {
            // the return type is an exception
            if (cause != null) {
                final Constructor<?> constructor = returnType.getConstructor(String.class, Throwable.class);
                return constructor.newInstance(result, cause);
            } else {
                final Constructor<?> constructor = returnType.getConstructor(String.class);
                return constructor.newInstance(result);
            }
        } else {
            return result;
        }
    }

    protected String getFormatString(final Message message) {
        String formatString = message.value();
        if (formatString == null) {
            return null;
        }
        final int id = message.id();
        if (id > 0) {
            // todo - support for inherited msg id
            final String projectCode = this.projectCode;
            if (projectCode != null) {
                final StringBuilder b = new StringBuilder(32);
                b.append(projectCode).append('-');
                if (id < 10) {
                    b.append("0000");
                } else if (id < 100) {
                    b.append("000");
                } else if (id < 1000) {
                    b.append("00");
                } else if (id < 10000) {
                    b.append("0");
                }
                b.append(id);
                b.append(": ");
                b.append(formatString);
                formatString = b.toString();
            }
        }
        return formatString;
    }

    protected static Throwable extractCause(final Annotation[][] parameterAnnotations, final Object[] args, final List<Object> newArgs) {
        Throwable cause = null;
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            boolean found = false;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Cause) {
                    if (cause == null) {
                        cause = (Throwable) args[i];
                    }
                    found = true;
                }
            }
            if (! found) {
                newArgs.add(args[i]);
            }
        }
        return cause;
    }
}
