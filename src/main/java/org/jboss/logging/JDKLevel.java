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

import java.util.logging.Level;

/**
 * Levels used by the JDK logging backend.
 */
public final class JDKLevel extends Level {
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   protected JDKLevel(final String name, final int value) {
      super(name, value);
   }

   protected JDKLevel(final String name, final int value, final String resourceBundleName) {
      super(name, value, resourceBundleName);
   }

   public static final JDKLevel FATAL = new JDKLevel("FATAL", 1100);
   public static final JDKLevel ERROR = new JDKLevel("ERROR", 1000);
   public static final JDKLevel WARN = new JDKLevel("WARN", 900);
   @SuppressWarnings("hiding")
   public static final JDKLevel INFO = new JDKLevel("INFO", 800);
   public static final JDKLevel DEBUG = new JDKLevel("DEBUG", 500);
   public static final JDKLevel TRACE = new JDKLevel("TRACE", 400);
}