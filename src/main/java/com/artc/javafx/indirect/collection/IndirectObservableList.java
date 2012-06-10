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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import com.artc.javafx.indirect.IndirectObject;
import com.sun.javafx.collections.IterableChangeBuilder;

public class IndirectObservableList<T> implements ObservableList<T>, IndirectObject<ObservableList<T>> {
	private ObservableList<T> underlyingList;
	private final Set<ListChangeListener<? super T>> listChangeListeners = new HashSet<ListChangeListener<? super T>>();
	private final Set<InvalidationListener> invalidationListeners = new HashSet<InvalidationListener>();
	private final IterableChangeBuilder<T> iterableChangeBuilder = new IterableChangeBuilder<T>(this);
	
	public static <T> IndirectObservableList<T> create() {
		return new IndirectObservableList<T>();
	}
	
	public static <T> IndirectObservableList<T> create(ObservableList<T> underlyingList) {
		return new IndirectObservableList<T>(underlyingList);
	}
	
	public IndirectObservableList() {
		this(null);
	}
	
	public IndirectObservableList(ObservableList<T> underlyingList) {
		this.underlyingList = useListOrCreateEmptyListIfNull(underlyingList);
	}
	
	public void addListener(InvalidationListener listener) {
		underlyingList.addListener(listener);
		invalidationListeners.add(listener);
	}
	
	public void removeListener(InvalidationListener listener) {
		underlyingList.removeListener(listener);
		invalidationListeners.remove(listener);
	}
	
	public void addListener(ListChangeListener<? super T> listener) {
		underlyingList.addListener(listener);
		listChangeListeners.add(listener);
	}
	
	public void removeListener(ListChangeListener<? super T> listener) {
		underlyingList.removeListener(listener);
		listChangeListeners.remove(listener);
	}
	
	public void setUnderlyingObject(ObservableList<T> newList) {
		removeInvalidationListeners(invalidationListeners, this.underlyingList);
		removeListListeners(listChangeListeners, this.underlyingList);
		
		ObservableList<T> oldList = this.underlyingList;
		this.underlyingList = useListOrCreateEmptyListIfNull(newList);
		
		addInvalidationListeners(invalidationListeners, this.underlyingList);
		addListListeners(listChangeListeners, this.underlyingList);
		
		fireInvalidationChange(invalidationListeners, this);
		fireEntireListChange(oldList);
	}
	
	@Override
	public ObservableList<T> getUnderlyingObject() {
		return underlyingList;
	}
	
	private void fireEntireListChange(ObservableList<T> oldList) {
		iterableChangeBuilder.nextRemove(0, new ArrayList<T>(oldList.subList(0, oldList.size())));
		iterableChangeBuilder.nextAdd(0, this.underlyingList.size());
		Change<T> change = iterableChangeBuilder.buildAndReset();
		for (ListChangeListener<? super T> listChangeListener : new ArrayList<ListChangeListener<? super T>>(listChangeListeners)) {
			listChangeListener.onChanged(change);
		}
	}
	
	// LATER the empty list throws when changes are done to it so user must know when they've set the list to null. Is this reasonable?
	private static <T> ObservableList<T> useListOrCreateEmptyListIfNull(ObservableList<T> list) {
		return list != null ? list : FXCollections.<T> emptyObservableList();
	}
	
	// ========================================================
	// ========= Helper methods for listeners ========= 
	// ========================================================
	private static <T> void addListListeners(Set<ListChangeListener<? super T>> listChangeListeners, ObservableList<T> observable) {
		if (observable != null) {
			for (ListChangeListener<? super T> listChangeListener : listChangeListeners) {
				observable.addListener(listChangeListener);
			}
		}
	}
	
	private static <T> void removeListListeners(Set<ListChangeListener<? super T>> listChangeListeners, ObservableList<T> observable) {
		if (observable != null) {
			for (ListChangeListener<? super T> listChangeListener : listChangeListeners) {
				observable.removeListener(listChangeListener);
			}
		}
	}
	
	private static <T> void addInvalidationListeners(Set<InvalidationListener> invalidationListeners, Observable observable) {
		if (observable != null) {
			for (InvalidationListener invalidationListener : invalidationListeners) {
				observable.addListener(invalidationListener);
			}
		}
	}
	
	private static <T> void removeInvalidationListeners(Set<InvalidationListener> invalidationListeners, Observable observable) {
		if (observable != null) {
			for (InvalidationListener invalidationListener : invalidationListeners) {
				observable.removeListener(invalidationListener);
			}
		}
	}
	
	private static void fireInvalidationChange(Set<InvalidationListener> invalidationListeners, Observable observable) {
		for (InvalidationListener invalidationListener : invalidationListeners) {
			invalidationListener.invalidated(observable);
		}
	}
	
	// ========================================================
	// ========= All methods below here just delegate ========= 
	// ========================================================
	public boolean isEmpty() {
		return underlyingList.isEmpty();
	}
	
	public boolean contains(Object o) {
		return underlyingList.contains(o);
	}
	
	public Iterator<T> iterator() {
		return underlyingList.iterator();
	}
	
	public boolean add(T e) {
		return underlyingList.add(e);
	}
	
	public boolean remove(Object o) {
		return underlyingList.remove(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return underlyingList.containsAll(c);
	}
	
	public boolean addAll(Collection<? extends T> c) {
		return underlyingList.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends T> c) {
		return underlyingList.addAll(index, c);
	}
	
	public boolean removeAll(Collection<?> c) {
		return underlyingList.removeAll(c);
	}
	
	public boolean equals(Object o) {
		return underlyingList.equals(o);
	}
	
	public int hashCode() {
		return underlyingList.hashCode();
	}
	
	public T get(int index) {
		return underlyingList.get(index);
	}
	
	public void add(int index, T element) {
		underlyingList.add(index, element);
	}
	
	public boolean addAll(@SuppressWarnings("unchecked") T... elements) {
		return underlyingList.addAll(elements);
	}
	
	public void clear() {
		underlyingList.clear();
	}
	
	public int indexOf(Object o) {
		return underlyingList.indexOf(o);
	}
	
	public int lastIndexOf(Object o) {
		return underlyingList.lastIndexOf(o);
	}
	
	public ListIterator<T> listIterator() {
		return underlyingList.listIterator();
	}
	
	public ListIterator<T> listIterator(int index) {
		return underlyingList.listIterator(index);
	}
	
	public void remove(int from, int to) {
		underlyingList.remove(from, to);
	}
	
	public T remove(int index) {
		return underlyingList.remove(index);
	}
	
	public boolean removeAll(@SuppressWarnings("unchecked") T... elements) {
		return underlyingList.removeAll(elements);
	}
	
	public boolean retainAll(Collection<?> c) {
		return underlyingList.retainAll(c);
	}
	
	public boolean retainAll(@SuppressWarnings("unchecked") T... elements) {
		return underlyingList.retainAll(elements);
	}
	
	public T set(int index, T element) {
		return underlyingList.set(index, element);
	}
	
	public boolean setAll(Collection<? extends T> col) {
		return underlyingList.setAll(col);
	}
	
	public boolean setAll(@SuppressWarnings("unchecked") T... elements) {
		return underlyingList.setAll(elements);
	}
	
	public int size() {
		return underlyingList.size();
	}
	
	public Object[] toArray() {
		return underlyingList.toArray();
	}
	
	public <V> V[] toArray(V[] a) {
		return underlyingList.toArray(a);
	}
	
	public List<T> subList(int fromIndex, int toIndex) {
		return underlyingList.subList(fromIndex, toIndex);
	}
}
