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

import java.text.MessageFormat;


final class EquinoxLogger extends Logger {

  private static final long serialVersionUID = -5517882312696000315L;

  private transient final org.eclipse.equinox.log.Logger logger;

  protected EquinoxLogger(final String name, final org.eclipse.equinox.log.Logger logger) {
    super(name);
    this.logger = logger;
  }

  @Override
  public boolean isEnabled(final Level level) {
    return logger.isLoggable(translate(level));
  }

  @Override
  protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
    final int translatedLevel = translate(level);
    if (logger.isLoggable(translatedLevel)) try {
      logger.log(translatedLevel, String.valueOf(parameters == null || parameters.length == 0 ? message : MessageFormat.format(String.valueOf(message), parameters)), thrown);
    } catch (Throwable ignored) {}
  }

  @Override
  protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
    final int translatedLevel = translate(level);
    if (logger.isLoggable(translatedLevel)) try {
      logger.log(translatedLevel, parameters == null ? String.format(format) : String.format(format, parameters), thrown);
    } catch (Throwable ignored) {}
  }

  private static int translate(final Level level) {
    if (level != null) switch (level) {
    case FATAL: // no fatal -> use error
    case ERROR: return org.osgi.service.log.LogService.LOG_ERROR;
    case WARN:  return org.osgi.service.log.LogService.LOG_WARNING;
    case INFO:  return org.osgi.service.log.LogService.LOG_INFO;
    case DEBUG: // no trace -> treat debug and trace the same
    case TRACE: return org.osgi.service.log.LogService.LOG_DEBUG;
    }
    return org.osgi.service.log.LogService.LOG_DEBUG;
  }

}
