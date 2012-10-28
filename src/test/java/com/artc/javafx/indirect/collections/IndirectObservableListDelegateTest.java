package com.artc.javafx.indirect.collections;

import static com.artc.javafx.indirect.collections.ListTesting.added;
import static com.artc.javafx.indirect.collections.ListTesting.matchesTheseAdded;
import static com.artc.javafx.indirect.collections.ListTesting.matchesTheseChanges;
import static com.artc.javafx.indirect.collections.ListTesting.removed;
import static com.artc.javafx.indirect.collections.ListTesting.wasCalledWithArgThat;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.junit.Test;

import com.artc.javafx.indirect.collections.IndirectObservableListDelegate;


public class IndirectObservableListDelegateTest {
	@Test
	public void constructor_null_list_becomes_empty_list() {
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(null);
		
		assertTrue(indirectList.isEmpty());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void operating_on_an_underlying_null_list_throws() {
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(null);
		
		indirectList.add("a");
	}
	
	@Test
	public void setUnderlyingObject_with_null_list_becomes_empty_list() {
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(FXCollections.observableArrayList("a", "b"));
		indirectList.setUnderlyingObject(null);
		
		assertTrue(indirectList.isEmpty());
	}
	
	@Test
	public void changes_to_underlying_list_change_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(underlyingList);
		
		underlyingList.add("c");
		
		assertEquals(indirectList.get(0), "a");
		assertEquals(indirectList.get(1), "b");
		assertEquals(indirectList.get(2), "c");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void changes_to_underlying_list_fire_events_from_the_indirect_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(underlyingList);
		
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		indirectList.addListener(mock);
		
		underlyingList.add("c");
		
		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("c")));
	}
	
	@Test
	public void changes_to_the_indirect_list_changes_the_underlying_list() {
		ObservableList<String> underlyingList = FXCollections.observableArrayList("a", "b");
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(underlyingList);
		
		indirectList.add("c");
		
		assertEquals(underlyingList.get(0), "a");
		assertEquals(underlyingList.get(1), "b");
		assertEquals(underlyingList.get(2), "c");
	}
	
	@Test
	public void setUnderlyingObject_changes_which_underlying_list_is_effected() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(startingUnderlyingList);
		
		indirectList.add("c");
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		indirectList.setUnderlyingObject(endingUnderlyingList);
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
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(FXCollections.observableArrayList("a", "b"));
		
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		indirectList.setUnderlyingObject(endingUnderlyingList);
		endingUnderlyingList.add("f");
		
		assertEquals(indirectList.get(0), "d");
		assertEquals(indirectList.get(1), "e");
		assertEquals(indirectList.get(2), "f");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_changes_to_new_underlying_list_fire_events_from_the_indirect_list() {
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(FXCollections.observableArrayList("a", "b"));
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		
		indirectList.setUnderlyingObject(endingUnderlyingList);
		indirectList.addListener(mock);
		endingUnderlyingList.add("f");
		
		verify(mock).onChanged(wasCalledWithArgThat(matchesTheseAdded("f")));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_changes_to_old_underlying_list_do_not_fire_events_from_the_indirect_list() {
		ObservableList<String> startingUnderlyingList = FXCollections.observableArrayList("a", "b");
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(startingUnderlyingList);
		ObservableList<String> endingUnderlyingList = FXCollections.observableArrayList("d", "e");
		ListChangeListener<String> mock = mock(ListChangeListener.class);
		
		indirectList.setUnderlyingObject(endingUnderlyingList);
		indirectList.addListener(mock);
		startingUnderlyingList.add("f");
		
		verifyZeroInteractions(mock);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void setUnderlyingObject_fires_events_that_indicate_the_entire_list_has_changed() {
		IndirectObservableListDelegate<String> indirectList = IndirectObservableListDelegate.create(FXCollections.observableArrayList("a", "b"));
		final ListChangeListener<String> listener = mock(ListChangeListener.class);
		indirectList.addListener(listener);
		
		indirectList.setUnderlyingObject(FXCollections.observableArrayList("c", "d", "e"));
		
		verify(listener).onChanged(wasCalledWithArgThat(matchesTheseChanges(removed("a", "b"), added("c", "d", "e"))));
	}
}
