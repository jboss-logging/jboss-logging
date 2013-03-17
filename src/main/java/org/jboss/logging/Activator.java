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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {
  
  private static final Object LOCK = new Object();

  private ServiceReference serviceReference;
  private static Object service;

  @Override
  public void start(BundleContext context) {
    synchronized (LOCK) {
      // use class name to not trigger class loading so the code also works
      // in an OSGi environment that does not have ExtendedLogService present
      // ie. not Equinox
      serviceReference = context.getServiceReference("org.eclipse.equinox.log.ExtendedLogService");
      if (serviceReference != null) {
        service = context.getService(serviceReference);
      }
    }
  }

  @Override
  public void stop(BundleContext context) {
    synchronized (LOCK) {
      if (serviceReference != null) {
        context.ungetService(serviceReference);
      }
      serviceReference = null;
      service = null;
    }

  }

  static Object getService() {
    synchronized (LOCK) {
      return service;
    }
  }

}
