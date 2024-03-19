/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logging;

import static java.security.AccessController.doPrivileged;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.security.PrivilegedAction;
import java.util.Locale;

/**
 * A factory class to produce message bundle implementations.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Messages {

    private Messages() {
    }

    /**
     * Get a message bundle of the given type. Equivalent to
     * <code>{@link #getBundle(Class, java.util.Locale) getBundle}(type, Locale.getDefault())</code>.
     *
     * @param type the bundle type class
     * @param <T>  the bundle type
     * @return the bundle
     * @deprecated Use {@link #getBundle(Lookup,Class)} instead to avoid errors in modular environments.
     */
    @Deprecated(forRemoval = true, since = "3.6")
    public static <T> T getBundle(Class<T> type) {
        return getBundle(type, LoggingLocale.getLocale());
    }

    /**
     * Get a message bundle of the given type.
     *
     * @param type   the bundle type class
     * @param locale the message locale to use
     * @param <T>    the bundle type
     * @return the bundle
     * @deprecated Use {@link #getBundle(Lookup,Class,Locale)} instead to avoid errors in modular environments.
     */
    @Deprecated(forRemoval = true, since = "3.6")
    public static <T> T getBundle(final Class<T> type, final Locale locale) {
        Lookup lookup;
        if (System.getSecurityManager() == null) {
            try {
                lookup = MethodHandles.privateLookupIn(type, MethodHandles.lookup());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("This library does not have private access to " + type);
            }
        } else {
            lookup = doPrivileged(new PrivilegedAction<Lookup>() {
                public Lookup run() {
                    try {
                        return MethodHandles.privateLookupIn(type, MethodHandles.lookup());
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("This library does not have private access to " + type);
                    }
                }
            });
        }
        return doGetBundle(lookup, type, locale);
    }

    /**
     * Get a message bundle of the given type. Equivalent to
     * <code>{@link #getBundle(Class, java.util.Locale) getBundle}(type, Locale.getDefault())</code>.
     *
     * @param lookup a lookup which has access to the implementation class (usually {@link MethodHandles#lookup() lookup()})
     * @param type   the bundle type class
     * @param <T>    the bundle type
     * @return the bundle
     */
    public static <T> T getBundle(Lookup lookup, Class<T> type) {
        return getBundle(lookup, type, LoggingLocale.getLocale());
    }

    /**
     * Get a message bundle of the given type.
     *
     * @param lookup a lookup which has access to the implementation class (usually {@link MethodHandles#lookup() lookup()})
     * @param type   the bundle type class
     * @param locale the message locale to use
     * @param <T>    the bundle type
     * @return the bundle
     */
    public static <T> T getBundle(final Lookup lookup, final Class<T> type, final Locale locale) {
        return doGetBundle(lookup, type, locale);
    }

    private static <T> T doGetBundle(final Lookup lookup, final Class<T> type, final Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        Class<? extends T> bundleClass = null;
        if (variant != null && !variant.isEmpty()) {
            try {
                bundleClass = lookup.findClass(join(type.getName(), "$bundle", language, country, variant)).asSubclass(type);
            } catch (ClassNotFoundException e) {
                // ignore
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("The given lookup does not have access to the implementation class");
            }
        }
        if (bundleClass == null && country != null && !country.isEmpty()) {
            try {
                bundleClass = lookup.findClass(join(type.getName(), "$bundle", language, country, null)).asSubclass(type);
            } catch (ClassNotFoundException e) {
                // ignore
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("The given lookup does not have access to the implementation class");
            }
        }
        if (bundleClass == null && language != null && !language.isEmpty()) {
            try {
                bundleClass = lookup.findClass(join(type.getName(), "$bundle", language, null, null)).asSubclass(type);
            } catch (ClassNotFoundException e) {
                // ignore
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("The given lookup does not have access to the implementation class");
            }
        }
        if (bundleClass == null) {
            try {
                bundleClass = lookup.findClass(join(type.getName(), "$bundle", null, null, null)).asSubclass(type);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Invalid bundle " + type + " (implementation not found)");
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("The given lookup does not have access to the implementation class");
            }
        }
        final MethodHandle getter;
        try {
            getter = lookup.findStaticGetter(bundleClass, "INSTANCE", bundleClass);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Bundle implementation " + bundleClass + " has no instance field");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "The given lookup does not have access to the implementation class instance field");
        }
        try {
            return type.cast(getter.invoke());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Bundle implementation " + bundleClass + " could not be instantiated", e);
        }
    }

    private static String join(String interfaceName, String a, String b, String c, String d) {
        final StringBuilder build = new StringBuilder();
        build.append(interfaceName).append('_').append(a);
        if (b != null && b.length() > 0) {
            build.append('_');
            build.append(b);
        }
        if (c != null && c.length() > 0) {
            build.append('_');
            build.append(c);
        }
        if (d != null && d.length() > 0) {
            build.append('_');
            build.append(d);
        }
        return build.toString();
    }
}
