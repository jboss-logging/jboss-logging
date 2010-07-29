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

import java.util.Map;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

final class Log4jLoggerProvider implements LoggerProvider {

    public Logger getLogger(final String name) {
        return new Log4jLogger(name);
    }

    public Object getMdc(String key) {
        return MDC.get(key);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMdcMap() {
        return MDC.getContext();
    }

    public Object putMdc(String key, Object val) {
        try {
            return MDC.get(key);
        } finally {
            MDC.put(key, val);
        }
    }

    public void removeMdc(String key) {
        MDC.remove(key);
    }

    public void clearNdc() {
        NDC.clear();
    }

    public String getNdc() {
        return NDC.get();
    }

    public int getNdcDepth() {
        return NDC.getDepth();
    }

    public String peekNdc() {
        return NDC.peek();
    }

    public String popNdc() {
        return NDC.pop();
    }

    public void pushNdc(String message) {
        NDC.push(message);
    }

    public void setNdcMaxDepth(int maxDepth) {
        NDC.setMaxDepth(maxDepth);
    }
}
