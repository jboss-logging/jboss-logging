/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
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

import org.junit.jupiter.api.Assumptions;

/**
 * A utility for assumes.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class AssumeUtil {

    /**
     * Assumes that the {@code java.version} system property is not an early access version.
     * <p>
     * Currently this is required for log4j 1.x as it checks if {@link ThreadLocal} is available by checking the
     * {@code java.version} system property. If there is no {@code .} (dot), it assumes {@code ThreadLocal} is not
     * available.
     * </p>
     */
    static void assumeNotJavaEa() {
        final String version = System.getProperty("java.version");
        Assumptions.assumeTrue(version.indexOf('.') != -1);
    }
}
