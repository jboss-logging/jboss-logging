/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2010 Red Hat, Inc.
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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import org.jboss.logmanager.LogContext;
import org.jboss.logmanager.MDC;
import org.jboss.logmanager.NDC;

import static org.jboss.logmanager.Logger.AttachmentKey;

final class JBossLogManagerProvider implements LoggerProvider {

    private static final AttachmentKey<Logger> KEY = new AttachmentKey<Logger>();

    public Logger getLogger(final String name) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Logger>() {
                public Logger run() {
                    return doGetLogger(name);
                }
            });
        } else {
            return doGetLogger(name);
        }
    }

    private static Logger doGetLogger(final String name) {
        Logger l = LogContext.getLogContext().getAttachment(name, KEY);
        if (l != null) {
            return l;
        }
        final org.jboss.logmanager.Logger logger = org.jboss.logmanager.Logger.getLogger(name);
        l = new JBossLogManagerLogger(name, logger);
        Logger a = logger.attachIfAbsent(KEY, l);
        if (a == null) {
            return l;
        } else {
            return a;
        }
    }

    public Object putMdc(final String key, final Object value) {
        return MDC.put(key, String.valueOf(value));
    }

    public Object getMdc(final String key) {
        return MDC.get(key);
    }

    public void removeMdc(final String key) {
        MDC.remove(key);
    }

    @SuppressWarnings({ "unchecked" })
    public Map<String, Object> getMdcMap() {
        // we can re-define the erasure of this map because MDC does not make further use of the copy
        return (Map)MDC.copy();
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

    public String popNdc() {
        return NDC.pop();
    }

    public String peekNdc() {
        return NDC.get();
    }

    public void pushNdc(final String message) {
        NDC.push(message);
    }

    public void setNdcMaxDepth(final int maxDepth) {
        NDC.trimTo(maxDepth);
    }
}
