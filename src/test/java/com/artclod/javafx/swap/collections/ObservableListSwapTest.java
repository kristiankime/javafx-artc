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

import com.artclod.javafx.swap.collections.ArrayObservableListSwap;

public class ObservableListSwapTest {
	
	@Test
	public void constructor_null_list_becomes_empty_list() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(null);
		
		assertTrue(indirectList.isEmpty());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void operating_on_an_underlying_null_list_throws() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(null);
		
		indirectList.add("a");
	}
	
	@Test
	public void setUnderlyingObject_with_null_list_becomes_empty_list() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		indirectList.swap(null);
		
		assertTrue(indirectList.isEmpty());
	}
	
	@Test
	public void changes_to_underlying_list_change_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(underlyingList);
		
		underlyingList.add("c");
		
		assertEquals(indirectList.get(0), "a");
		assertEquals(indirectList.get(1), "b");
		assertEquals(indirectList.get(2), "c");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void changes_to_underlying_list_fire_events_from_the_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(underlyingList);
		
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		indirectList.addListener(mock);
		
		underlyingList.add("c");
		
		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("c")));
	}
	
	@Test
	public void changes_to_the_indirect_list_changes_the_underlying_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(underlyingList);
		
		indirectList.add("c");
		
		assertEquals(underlyingList.get(0), "a");
		assertEquals(underlyingList.get(1), "b");
		assertEquals(underlyingList.get(2), "c");
	}
	
	@Test
	public void setUnderlyingObject_changes_which_underlying_list_is_effected() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(startingUnderlyingList);
		
		indirectList.add("c");
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		indirectList.swap(endingUnderlyingList);
		indirectList.add("f");
		
		assertEquals(startingUnderlyingList.get(0), "a");
		assertEquals(startingUnderlyingList.get(1), "b");
		assertEquals(startingUnderlyingList.get(2), "c");
		
		assertEquals(endingUnderlyingList.get(0), "d");
		assertEquals(endingUnderlyingList.get(1), "e");
		assertEquals(endingUnderlyingList.get(2), "f");
	}
	
	@Test
	public void setUnderlyingObject_new_underlying_list_effects_indirect_list() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		indirectList.swap(endingUnderlyingList);
		endingUnderlyingList.add("f");
		
		assertEquals(indirectList.get(0), "d");
		assertEquals(indirectList.get(1), "e");
		assertEquals(indirectList.get(2), "f");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_changes_to_new_underlying_list_fire_events_from_the_indirect_list() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		
		indirectList.swap(endingUnderlyingList);
		indirectList.addListener(mock);
		endingUnderlyingList.add("f");
		
		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("f")));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_changes_to_old_underlying_list_do_not_fire_events_from_the_indirect_list() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(startingUnderlyingList);
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		
		indirectList.swap(endingUnderlyingList);
		indirectList.addListener(mock);
		startingUnderlyingList.add("f");
		
		verifyZeroInteractions(mock);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_fires_events_that_indicate_the_entire_list_has_changed() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		final ListChangeListener<String> listener = mock(ListChangeListener.class);
		indirectList.addListener(listener);
		
		indirectList.swap(FXCollections.observableArrayList("c", "d", "e"));
		
		verify(listener).onChanged(wasCalledWithArgThat(matchesTheseChanges(removed("a", "b"), added("c", "d", "e"))));
	}
	
}
