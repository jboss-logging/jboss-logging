/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Inc., and individual contributors
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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Identifies a parameter is to be used for constructing an exception and excluded from the formatting of the message.
 * <p/>
 * Parameters will be order-matched first, then type-matched to resolve ambiguity. If a match fails an error should
 * occur.
 * <p/>
 * The {@link #value()} option will allow an optional class to be specified which will have to match the exact type of
 * the parameter in question, to enable unambiguous resolution. The value must be the fully qualified class name.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Target(PARAMETER)
@Retention(CLASS)
@Documented
public @interface Param {

    /**
     * Defines an exact class the parameter must match for unambiguous resolution.
     *
     * @return the class the parameter must match.
     */
    Class<?> value() default Object.class;
}
