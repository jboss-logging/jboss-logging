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

import java.util.ArrayDeque;

abstract class AbstractLoggerProvider {

    private final ThreadLocal<ArrayDeque<Entry>> ndcStack = new ThreadLocal<ArrayDeque<Entry>>();

    public void clearNdc() {
        ArrayDeque<Entry> stack = ndcStack.get();
        if (stack != null)
            stack.clear();
    }

    public String getNdc() {
        ArrayDeque<Entry> stack = ndcStack.get();
        return stack == null || stack.isEmpty() ? null : stack.peek().merged;
    }

    public int getNdcDepth() {
        ArrayDeque<Entry> stack = ndcStack.get();
        return stack == null ? 0 : stack.size();
    }

    public String peekNdc() {
        ArrayDeque<Entry> stack = ndcStack.get();
        return stack == null || stack.isEmpty() ? "" : stack.peek().current;
    }

    public String popNdc() {
        ArrayDeque<Entry> stack = ndcStack.get();
        return stack == null || stack.isEmpty() ? "" : stack.pop().current;
    }

    public void pushNdc(String message) {
        ArrayDeque<Entry> stack = ndcStack.get();
        if (stack == null) {
            stack = new ArrayDeque<Entry>();
            ndcStack.set(stack);
        }
        stack.push(stack.isEmpty() ? new Entry(message) : new Entry(stack.peek(), message));
    }

    public void setNdcMaxDepth(int maxDepth) {
        final ArrayDeque<Entry> stack = ndcStack.get();
        if (stack != null) while (stack.size() > maxDepth) stack.pop();
    }

    private static class Entry {

        private String merged;
        private String current;

        Entry(String current) {
            merged = current;
            this.current = current;
        }

        Entry(Entry parent, String current) {
            merged = parent.merged + ' ' + current;
            this.current = current;
        }
    }
}
