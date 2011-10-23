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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Assigns a message string to a resource method.  The method arguments are used to supply the positional parameter
 * values for the method.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@Target(METHOD)
@Retention(CLASS)
@Documented
public @interface Message {

    /**
     * Indicates that this message has no ID.
     */
    int NONE = 0;
    /**
     * Indicates that this message should inherit the ID from another message with the same name.
     */
    int INHERIT = -1;

    /**
     * The message ID number.  Only one message with a given name may specify an ID other than {@link #INHERIT}.
     *
     * @return the message ID number
     */
    int id() default INHERIT;

    /**
     * The default format string of this message.
     *
     * @return the format string
     */
    String value();

    /**
     * The format type of this method (defaults to {@link Format#PRINTF}).
     *
     * @return the format type
     */
    Format format() default Format.PRINTF;

    /**
     * The possible format types.
     */
    enum Format {

        /**
         * A {@link java.util.Formatter}-type format string.
         */
        PRINTF,
        /**
         * A {@link java.text.MessageFormat}-type format string.
         */
        MESSAGE_FORMAT,
    }

}
