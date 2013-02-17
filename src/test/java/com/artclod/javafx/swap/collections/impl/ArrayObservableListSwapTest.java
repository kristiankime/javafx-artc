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
package com.artclod.javafx.swap.collections.impl;

import static com.artclod.javafx.swap.collections.ListTesting.added;
import static com.artclod.javafx.swap.collections.ListTesting.matchesTheseAdded;
import static com.artclod.javafx.swap.collections.ListTesting.matchesTheseChanges;
import static com.artclod.javafx.swap.collections.ListTesting.removed;
import static com.artclod.javafx.swap.collections.ListTesting.wasCalledWithArgThat;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.junit.Test;

import com.artclod.javafx.swap.collections.impl.ArrayObservableListSwap;

public class ArrayObservableListSwapTest {
	
	@Test
	public void constructor_null_list_becomes_empty_list() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(null);

		assertTrue(listSwap.isEmpty());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void operating_on_an_underlying_null_list_throws() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(null);

		listSwap.add("a");
	}

	@Test
	public void swap_with_null_list_becomes_empty_list() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		listSwap.swap(null);

		assertTrue(listSwap.isEmpty());
	}

	@Test
	public void changes_to_underlying_list_change_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(underlyingList);

		underlyingList.add("c");

		assertEquals(listSwap.get(0), "a");
		assertEquals(listSwap.get(1), "b");
		assertEquals(listSwap.get(2), "c");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void changes_to_underlying_list_fire_events_from_the_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(underlyingList);

		ListChangeListener<String> mock = mock(ListChangeListener.class);
		listSwap.addListener(mock);

		underlyingList.add("c");

		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("c")));
	}

	@Test
	public void changes_to_the_indirect_list_changes_the_underlying_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(underlyingList);

		listSwap.add("c");

		assertEquals(underlyingList.get(0), "a");
		assertEquals(underlyingList.get(1), "b");
		assertEquals(underlyingList.get(2), "c");
	}

	@Test
	public void swap_changes_which_underlying_list_is_effected() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(startingUnderlyingList);

		listSwap.add("c");
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		listSwap.swap(endingUnderlyingList);
		listSwap.add("f");

		assertEquals(startingUnderlyingList.get(0), "a");
		assertEquals(startingUnderlyingList.get(1), "b");
		assertEquals(startingUnderlyingList.get(2), "c");

		assertEquals(endingUnderlyingList.get(0), "d");
		assertEquals(endingUnderlyingList.get(1), "e");
		assertEquals(endingUnderlyingList.get(2), "f");
	}

	@Test
	public void swap_new_underlying_list_effects_indirect_list() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));

		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		listSwap.swap(endingUnderlyingList);
		endingUnderlyingList.add("f");

		assertEquals(listSwap.get(0), "d");
		assertEquals(listSwap.get(1), "e");
		assertEquals(listSwap.get(2), "f");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void swap_changes_to_new_underlying_list_fire_events_from_the_indirect_list() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);

		listSwap.swap(endingUnderlyingList);
		listSwap.addListener(mock);
		endingUnderlyingList.add("f");

		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("f")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void swap_changes_to_old_underlying_list_do_not_fire_events_from_the_indirect_list() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(startingUnderlyingList);
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);

		listSwap.swap(endingUnderlyingList);
		listSwap.addListener(mock);
		startingUnderlyingList.add("f");

		verifyZeroInteractions(mock);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void swap_fires_events_that_indicate_the_entire_list_has_changed() {
		ArrayObservableListSwap<String> listSwap = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		final ListChangeListener<String> listener = mock(ListChangeListener.class);
		listSwap.addListener(listener);

		listSwap.swap(FXCollections.observableArrayList("c", "d", "e"));

		verify(listener).onChanged(wasCalledWithArgThat(matchesTheseChanges(removed("a", "b"), added("c", "d", "e"))));
	}
}
