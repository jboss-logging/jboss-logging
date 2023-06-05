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

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@SuppressWarnings("SameParameterValue")
class SecurityActions {

    static String getSystemProperty(final String key) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(key);
        }
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return System.getProperty(key);
            }
        });
    }

    static String getSystemProperty(final String key, final String dft) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(key, dft);
        }
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return System.getProperty(key, dft);
            }
        });
    }
}
