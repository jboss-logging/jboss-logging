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

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

/**
 * An implementation of the {@linkplain LoggerProvider log provider} for log4j.
 * <p>
 * Please note that log4j reached end of life on August 5, 2015. Prefer using a log manager and provider.
 * </p>
 */
public final class Log4jLoggerProvider implements LoggerProvider {

    @Override
    public Logger getLogger(final String name) {
        return new Log4jLogger("".equals(name) ? "ROOT" : name);
    }

    @Override
    public void clearMdc() {
        MDC.clear();
    }

    @Override
    public Object getMdc(String key) {
        return MDC.get(key);
    }

    @Override
    public Map<String, Object> getMdcMap() {
        @SuppressWarnings("unchecked")
        final Map<String, Object> map = MDC.getContext();
        return map == null ? Collections.emptyMap() : map;
    }

    @Override
    public Object putMdc(String key, Object val) {
        try {
            return MDC.get(key);
        } finally {
            MDC.put(key, val);
        }
    }

    @Override
    public void removeMdc(String key) {
        MDC.remove(key);
    }

    @Override
    public void clearNdc() {
        NDC.remove();
    }

    @Override
    public String getNdc() {
        return NDC.get();
    }

    @Override
    public int getNdcDepth() {
        return NDC.getDepth();
    }

    @Override
    public String peekNdc() {
        return NDC.peek();
    }

    @Override
    public String popNdc() {
        return NDC.pop();
    }

    @Override
    public void pushNdc(String message) {
        NDC.push(message);
    }

    @Override
    public void setNdcMaxDepth(int maxDepth) {
        NDC.setMaxDepth(maxDepth);
    }
}
