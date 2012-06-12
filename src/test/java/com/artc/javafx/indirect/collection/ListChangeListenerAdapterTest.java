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

package com.artc.javafx.indirect.collection;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.Test;

public class ListChangeListenerAdapterTest {
	
	@Test
	public void permuted_change() {
		ObservableList<String> observableList = observableList("b", "a");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		FXCollections.sort(observableList);
		
		assertListsEqual(changes, asList(permutation(0, 1, "b"), permutation(1, 0, "a")));
	}
	
	@Test
	public void set_by_index() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.set(1, "c");
		
		assertListsEqual(changes, asList(removed("b"), added(1, "c")));
	}
	
	@Test
	public void setAll_collection() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.setAll(asList("c", "d"));
		
		assertListsEqual(changes, asList(removed("a"), removed("b"), added(0, "c"), added(1, "d")));
	}
	
	@Test
	public void setAll_varargs() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.setAll("c", "d");
		
		assertListsEqual(changes, asList(removed("a"), removed("b"), added(0, "c"), added(1, "d")));
	}
	
	@Test
	public void remove_by_index() {
		ObservableList<String> observableList = observableList("a", "b", "c");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.remove(1);
		
		assertListsEqual(changes, asList(removed("b")));
	}
	
	@Test
	public void remove_by_element() {
		ObservableList<String> observableList = observableList("a", "b", "c");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.remove("c");
		
		assertListsEqual(changes, asList(removed("c")));
	}
	
	@Test
	public void removeAll_collection() {
		ObservableList<String> observableList = observableList("a", "b", "c", "d");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.removeAll(asList("b", "c"));
		
		assertListsEqual(changes, asList(removed("b"), removed("c")));
	}
	
	@Test
	public void removeAll_varargs() {
		ObservableList<String> observableList = observableList("a", "b", "c", "d");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.removeAll("b", "c");
		
		assertListsEqual(changes, asList(removed("b"), removed("c")));
	}
	
	@Test
	public void add_element() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.add("c");
		
		assertListsEqual(changes, asList(added(2, "c")));
	}
	
	@Test
	public void add_element_index() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.add(1, "c");
		
		assertListsEqual(changes, asList(added(1, "c")));
	}
	
	@Test
	public void addAll_collection() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.addAll(asList("c", "d"));
		
		assertListsEqual(changes, asList(added(2, "c"), added(3, "d")));
	}
	
	@Test
	public void addAll_varargs() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.addAll("c", "d");
		
		assertListsEqual(changes, asList(added(2, "c"), added(3, "d")));
	}
	
	@Test
	public void addAll_collection_indexed() {
		ObservableList<String> observableList = observableList("a", "b");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.addAll(1, asList("c", "d"));
		
		assertListsEqual(changes, asList(added(1, "c"), added(2, "d")));
	}
	
	@Test
	public void retainAll_collection() {
		ObservableList<String> observableList = observableList("a", "b", "c");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.retainAll(asList("b", "c"));
		
		assertListsEqual(changes, asList(removed("a")));
	}
	
	@Test
	public void retainAll_varargs() {
		ObservableList<String> observableList = observableList("a", "b", "c", "d");
		List<AChange<String>> changes = changeList();
		observableList.addListener(new ListenForAllChanges<String>(changes, observableList));
		
		observableList.retainAll("b", "c");
		
		assertListsEqual(changes, asList(removed("a"), removed("d")));
	}
	
	// ===========================
	private static class ListenForAllChanges<T> extends ListChangeListenerAdapter<T> {
		private final List<AChange<T>> changes;
		private final ObservableList<T> observableList;
		
		private ListenForAllChanges(List<AChange<T>> changes, ObservableList<T> observableList) {
			this.changes = changes;
			this.observableList = observableList;
		}
		
		@Override
		public void addedChange(T item, int index) {
			changes.add(added(index, item));
		}
		
		@Override
		public void permutedChange(int oldIndex, int newIndex, T element) {
			changes.add(permutation(oldIndex, newIndex, element));
		}
		
		public void updatedChange(int index, T element) {
			throw new UnsupportedOperationException("Can't seem to find an observable list that fires this type of change...");
		}
		
		@Override
		public void removedChange(T item) {
			changes.add(removed(item));
		}
	}
	
	// ==============  Utility Methods =============
	@SafeVarargs
	private static <T> ObservableList<T> observableList(T... elements) {
		return FXCollections.observableArrayList(elements);
	}
	
	private static <T> void assertListsEqual(List<T> one, List<T> two) {
		assertNotNull("List one should not be null", one);
		assertNotNull("List two should not be null", two);
		assertEquals("Lists should be the same size", one.size(), two.size());
		for (int i = 0; i < one.size(); i++) {
			assertEquals("items [" + i + "] should have matched", one.get(i), two.get(i));
		}
	}
	
	private static <V> List<AChange<V>> changeList() {
		return new ArrayList<AChange<V>>();
	}
	
	private static <V> AChange<V> removed(V value) {
		return new AChange<V>(null, null, value);
	}
	
	private static <V> AChange<V> added(int index, V value) {
		return new AChange<V>(null, index, value);
	}
	
	private static <V> AChange<V> update(int index, V value) {
		return new AChange<V>(index, index, value);
	}
	
	private static <V> AChange<V> permutation(int oldIndex, int newIndex, V value) {
		return new AChange<V>(oldIndex, newIndex, value);
	}
	
	private static class AChange<V> {
		public final Integer oldIndex;
		public final Integer newIndex;
		public final V value;
		
		private AChange(Integer oldIndex, Integer newIndex, V value) {
			this.oldIndex = oldIndex;
			this.newIndex = newIndex;
			this.value = value;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((newIndex == null) ? 0 : newIndex.hashCode());
			result = prime * result + ((oldIndex == null) ? 0 : oldIndex.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AChange other = (AChange) obj;
			if (newIndex == null) {
				if (other.newIndex != null)
					return false;
			} else if (!newIndex.equals(other.newIndex))
				return false;
			if (oldIndex == null) {
				if (other.oldIndex != null)
					return false;
			} else if (!oldIndex.equals(other.oldIndex))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}
