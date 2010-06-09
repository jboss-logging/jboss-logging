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

import java.util.Locale;

/**
 * A converter for a specific parameter type.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @param <I> the input type
 */
public interface ParameterConverter<I> {

    /**
     * Convert the parameter to its string or string-equivalent representation.  The returned value will be passed in
     * as a parameter to either a {@link java.text.MessageFormat} or {@link java.util.Formatter} instance, depending
     * on the setting of {@link Message#format()}.
     *
     * @param locale the locale
     * @param parameter the parameter
     * @return the converted value
     */
    Object convert(Locale locale, I parameter);
}
