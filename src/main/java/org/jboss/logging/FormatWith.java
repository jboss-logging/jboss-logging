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
 * Indicate that the given parameter should be wrapped with a formatting object of the given class.  The class
 * must have a one-argument constructor which unambiguously accepts a value of this parameter's type.  The resultant
 * object will be passed in as a parameter to the underlying format type; thus its {@link Object#toString() toString()}
 * method will be invoked (or, if the format style is {@link org.jboss.logging.Message.Format#PRINTF PRINTF}, the
 * object may implement {@link java.util.Formattable Formattable} to get extra functionality).
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@Target(PARAMETER)
@Retention(CLASS)
@Documented
public @interface FormatWith {

    /**
     * The class of the formatting object to use.
     *
     * @return the class
     */
    Class<?> value();
}
