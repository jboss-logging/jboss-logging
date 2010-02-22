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

import java.util.HashMap;
import java.util.Map;

abstract class AbstractMdcLoggerProvider extends AbstractLoggerProvider {

    private final ThreadLocal<Map<String, Object>> mdcMap = new ThreadLocal<Map<String, Object>>();

    public Object getMdc(String key) {
        return mdcMap.get() == null ? null : mdcMap.get().get(key);
    }

    public Map<String, Object> getMdcMap() {
        return mdcMap.get();
    }

    public Object putMdc(String key, Object value) {
        Map<String, Object> map = mdcMap.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            mdcMap.set(map);
        }
        return map.put(key, value);
    }

    public void removeMdc(String key) {
        Map<String, Object> map = mdcMap.get();
        if (map == null)
            return;
        map.remove(key);
    }
}
