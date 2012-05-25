/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2010 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * A typed logger method.  Indicates that this method will log the associated {@link Message} to the logger system, as
 * opposed to being a simple message lookup.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@Retention(CLASS)
@Target(METHOD)
@Documented
@Deprecated
public @interface LogMessage {

    /**
     * The log level at which this message should be logged.  Defaults to {@code INFO}.
     *
     * @return the log level
     */
    Logger.Level level() default Logger.Level.INFO;

    /**
     * The logging class name to use for this message, if any.
     *
     * @return the logging class name
     */
    Class<?> loggingClass() default Void.class;
}
