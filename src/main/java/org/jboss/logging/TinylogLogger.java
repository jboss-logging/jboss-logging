/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2010 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.jboss.logging;

import java.text.MessageFormat;

import org.pmw.tinylog.LogEntryForwarder;

final class TinylogLogger extends Logger {

	private static final long serialVersionUID = -3001488920586938216L;

	TinylogLogger(final String name) {
		super(name);
	}

	@Override
	public boolean isEnabled(final Level level) {
		return true;
	}

	@Override
	protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
		try {
			if (thrown == null) {
				if (parameters == null || parameters.length == 0) {
					LogEntryForwarder.forward(2, convert(level), message);
				} else {
					LogEntryForwarder.forward(2, convert(level), MessageFormat.format(String.valueOf(message), parameters));
				}
			} else {
				if (parameters == null || parameters.length == 0) {
					LogEntryForwarder.forward(2, convert(level), thrown, String.valueOf(message));
				} else {
					LogEntryForwarder.forward(2, convert(level), thrown, MessageFormat.format(String.valueOf(message), parameters));
				}
			}
		} catch (Throwable ignored) {
		}
	}

	@Override
	protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
		try {
			if (thrown == null) {
				if (parameters == null || parameters.length == 0) {
					LogEntryForwarder.forward(2, convert(level), format);
				} else {
					LogEntryForwarder.forward(2, convert(level), MessageFormat.format(format, parameters));
				}
			} else {
				if (parameters == null || parameters.length == 0) {
					LogEntryForwarder.forward(2, convert(level), thrown, format);
				} else {
					LogEntryForwarder.forward(2, convert(level), thrown, MessageFormat.format(format, parameters));
				}
			}
		} catch (Throwable ignored) {
		}
	}

	private static org.pmw.tinylog.Level convert(final Level level) {
		switch (level) {
			case FATAL:
			case ERROR:
				return org.pmw.tinylog.Level.ERROR;
			case WARN:
				return org.pmw.tinylog.Level.WARNING;
			case INFO:
				return org.pmw.tinylog.Level.INFO;
			case DEBUG:
				return org.pmw.tinylog.Level.DEBUG;
			default:
				return org.pmw.tinylog.Level.TRACE;
		}
	}
}
