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

import java.lang.reflect.Field;
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
     * Get a message bundle of the given type.  Equivalent to <code>{@link #getBundle(Class, java.util.Locale) getBundle}(type, Locale.getDefault())</code>.
     *
     * @param type the bundle type class
     * @param <T> the bundle type
     * @return the bundle
     */
    public static <T> T getBundle(Class<T> type) {
        return getBundle(type, Locale.getDefault());
    }

    /**
     * Get a message bundle of the given type.
     *
     * @param type the bundle type class
     * @param locale the message locale to use
     * @param <T> the bundle type
     * @return the bundle
     */
    public static <T> T getBundle(Class<T> type, Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        Class<? extends T> bundleClass = null;
        if (variant != null && variant.length() > 0) try {
            bundleClass = Class.forName(join(type.getName(), "$bundle", language, country, variant), true, type.getClassLoader()).asSubclass(type);
        } catch (ClassNotFoundException e) {
            // ignore
        }
        if (bundleClass == null && country != null && country.length() > 0) try {
            bundleClass = Class.forName(join(type.getName(), "$bundle", language, country, null), true, type.getClassLoader()).asSubclass(type);
        } catch (ClassNotFoundException e) {
            // ignore
        }
        if (bundleClass == null && language != null && language.length() > 0) try {
            bundleClass = Class.forName(join(type.getName(), "$bundle", language, null, null), true, type.getClassLoader()).asSubclass(type);
        } catch (ClassNotFoundException e) {
            // ignore
        }
        if (bundleClass == null) try {
            bundleClass = Class.forName(join(type.getName(), "$bundle", null, null, null), true, type.getClassLoader()).asSubclass(type);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid bundle " + type + " (implementation not found)");
        }
        final Field field;
        try {
            field = bundleClass.getField("INSTANCE");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Bundle implementation " + bundleClass + " has no instance field");
        }
        try {
            return type.cast(field.get(null));
        } catch (IllegalAccessException e) {
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
