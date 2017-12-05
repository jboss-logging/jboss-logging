/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2015 Red Hat, Inc., and individual contributors
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

import java.util.Map;

public class DelegatingLoggerProvider implements LoggerProvider {

	private volatile LoggerProvider delegate;

	protected final LoggerProvider getDelegate() {
		LoggerProvider theDelegate = delegate;
		if ( theDelegate != null ) {
			return theDelegate;
		}
		synchronized (this) {
			theDelegate = delegate;
			if ( theDelegate == null ) {
				theDelegate = LoggerProviders.findDelegate( getClass() );
				delegate = theDelegate;
			}
			return theDelegate;
		}
	}

	@Override
	public Logger getLogger(final String name) {
		return getDelegate().getLogger(name);
	}

	@Override
	public void clearMdc() {
		getDelegate().clearMdc();
	}

	@Override
	public Object putMdc(String key, Object value) {
		return getDelegate().putMdc( key, value );
	}

	@Override
	public Object getMdc(String key) {
		return getDelegate().getMdc( key );
	}

	@Override
	public void removeMdc(String key) {
		getDelegate().removeMdc( key );
	}

	@Override
	public Map<String, Object> getMdcMap() {
		return getDelegate().getMdcMap();
	}

	@Override
	public void clearNdc() {
		getDelegate().clearNdc();
	}

	@Override
	public String getNdc() {
		return getDelegate().getNdc();
	}

	@Override
	public int getNdcDepth() {
		return getDelegate().getNdcDepth();
	}

	@Override
	public String popNdc() {
		return getDelegate().popNdc();
	}

	@Override
	public String peekNdc() {
		return getDelegate().peekNdc();
	}

	@Override
	public void pushNdc(String message) {
		getDelegate().pushNdc( message );
	}

	@Override
	public void setNdcMaxDepth(int maxDepth) {
		getDelegate().setNdcMaxDepth( maxDepth );
	}
}
