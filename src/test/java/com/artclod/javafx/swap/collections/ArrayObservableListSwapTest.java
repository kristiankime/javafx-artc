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

public class ArrayObservableListSwapTest {
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
	public void swap_with_null_list_becomes_empty_list() {
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
	public void swap_changes_which_underlying_list_is_effected() {
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
	public void swap_new_underlying_list_effects_indirect_list() {
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
	public void swap_changes_to_new_underlying_list_fire_events_from_the_indirect_list() {
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
	public void swap_changes_to_old_underlying_list_do_not_fire_events_from_the_indirect_list() {
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
	public void swap_fires_events_that_indicate_the_entire_list_has_changed() {
		ArrayObservableListSwap<String> indirectList = ArrayObservableListSwap.create(FXCollections.observableArrayList("a", "b"));
		final ListChangeListener<String> listener = mock(ListChangeListener.class);
		indirectList.addListener(listener);

		indirectList.swap(FXCollections.observableArrayList("c", "d", "e"));

		verify(listener).onChanged(wasCalledWithArgThat(matchesTheseChanges(removed("a", "b"), added("c", "d", "e"))));
	}
}
