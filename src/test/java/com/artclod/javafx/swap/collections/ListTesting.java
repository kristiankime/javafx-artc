/*
 * Copyright (c) 2012, Kristian Kime
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package com.artclod.javafx.swap.collections;

import static org.mockito.Matchers.argThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;

import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;

public class ListTesting {

	public static <T> String changeToString(ListChangeListener.Change<T> change) {
		StringBuilder ret = new StringBuilder("Change\n");
		while (change.next()) {
			if (change.wasAdded()) {
				ret.append("added ").append(change.getAddedSubList()).append("\t");
			}
			if (change.wasRemoved()) {
				ret.append("removed ").append(change.getRemoved()).append("\t");
			}
			ret.append("\n");
		}
		return ret.toString();
	}

	public static <T> T wasCalledWithArgThat(Matcher<T> matcher) {
		return argThat(matcher);
	}

	@SafeVarargs
	public static <T> List<T> removed(T... args) {
		return Arrays.asList(args);
	}

	@SafeVarargs
	public static <T> List<T> added(T... args) {
		return Arrays.asList(args);
	}

	public static <T> ListReplaceMatcher<T> matchesReplace() {
		return new ListReplaceMatcher<T>();
	}

	public static class ListReplaceMatcher<T> extends ArgumentMatcher<Change<T>> {
		@SuppressWarnings("unchecked")
		@Override
		public boolean matches(Object argument) {
			Change<T> change = (Change<T>) argument;
			while (change.next()) {
				if (!change.wasReplaced()) {
					return false;
				}
			}
			return true;
		}
	}

	@SafeVarargs
	public static <T> ListChangeMatcher<T> matchesTheseAdded(T... addedValues) {
		return new ListChangeMatcher<T>(new ArrayList<T>(), Arrays.asList(addedValues));
	}

	@SafeVarargs
	public static <T> ListChangeMatcher<T> matchesTheseRemoved(T... removedValues) {
		return new ListChangeMatcher<T>(Arrays.asList(removedValues), new ArrayList<T>());
	}

	public static <T> ListChangeMatcher<T> matchesTheseChanges(List<T> removedValues, List<T> addedValues) {
		return new ListChangeMatcher<T>(removedValues, addedValues);
	}

	public static class ListChangeMatcher<T> extends ArgumentMatcher<Change<T>> {
		List<T> removedValues;
		List<T> addedValues;

		private ListChangeMatcher(List<T> removedValues, List<T> addedValues) {
			this.removedValues = new ArrayList<T>(removedValues);
			this.addedValues = new ArrayList<T>(addedValues);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean matches(Object item) {
			Change<T> change = (Change<T>) item;
			List<T> added = new ArrayList<T>();
			List<T> removed = new ArrayList<T>();

			while (change.next()) {
				if (change.wasAdded()) {
					added.addAll(change.getAddedSubList());
				}
				if (change.wasRemoved()) {
					removed.addAll(change.getRemoved());
				}
			}

			boolean ret = addedValues.equals(added) && removedValues.equals(removed);
			boolean wtf = added.isEmpty() && removed.isEmpty(); // LATER why does this get called twice, the second time blank?
			return ret || wtf;
		}
	}
}
