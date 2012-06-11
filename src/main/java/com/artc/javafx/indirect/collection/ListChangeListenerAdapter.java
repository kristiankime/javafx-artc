package com.artc.javafx.indirect.collection;

import javafx.collections.ListChangeListener;

public class ListChangeListenerAdapter<T> implements ListChangeListener<T> {

	@Override
	public void onChanged(Change<? extends T> c) {
		while (c.next()) {
			if (c.wasPermutated()) {
				for (int oldIndex = c.getFrom(); oldIndex < c.getTo(); ++oldIndex) {
					int newIndex = c.getPermutation(oldIndex);
					permutedChange(oldIndex, newIndex, c.getList().get(newIndex));
				}
			} else if (c.wasUpdated()) {
				for (int index = c.getFrom(); index < c.getTo(); ++index) {
					updatedChange(index, c.getList().get(index));
				}
			} else {
				for (T removedElement : c.getRemoved()) {
					removedChange(removedElement);
				}
				for (int index = c.getFrom(); index < c.getTo(); ++index) {
					T addedElement = c.getList().get(index);
					addedChange(addedElement, index);
				}
			}
		}
	}
	
	public void permutedChange(int oldIndex, int newIndex, T element){
		// NOOP
	}

	public void updatedChange(int index, T element) {
		// NOOP
	}
	
	public void removedChange(T item){
		// NOOP
	}
	
	public void addedChange(T item, int index){
		// NOOP
	}
}
