package org.jboss.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
@Retention(RUNTIME)
@Documented
public @interface Param {

    /**
     * Defines an exact class the parameter must match for unambiguous resolution.
     *
     * @return the class the parameter must match.
     */
    Class<?> value() default Object.class;
}
