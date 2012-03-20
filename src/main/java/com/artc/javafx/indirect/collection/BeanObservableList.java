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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import com.artc.javafx.indirect.bean.getter.Getter;
import com.sun.javafx.collections.IterableChangeBuilder;

public class BeanObservableList<B> implements ObservableList<B> {
	private final ObservableList<B> underlyingList = FXCollections.observableArrayList();
	private final Set<Getter<? extends Property<?>, B>> propertyGetters = new HashSet<Getter<? extends Property<?>, B>>();
	private final Set<InvalidationListener> invalidationListeners = new HashSet<InvalidationListener>();
	private final Set<ListChangeListener<? super B>> listChangeListeners = new HashSet<ListChangeListener<? super B>>();
	private final IterableChangeBuilder<B> iterableChangeBuilder = new IterableChangeBuilder<B>(this);
	private final Map<B, BeanWatcher> beanListeners = new HashMap<B, BeanWatcher>(); // LATER we currently only allow for one "equal" bean in this list
	
	public static <T> BeanObservableList<T> create(Getter<? extends Property<?>, T> propertyGetter1) {
		ArrayList<Getter<? extends Property<?>, T>> items = new ArrayList<Getter<? extends Property<?>, T>>();
		items.add(propertyGetter1);
		return new BeanObservableList<T>(items);
	}
	
	public static <T> BeanObservableList<T> create(Getter<? extends Property<?>, T> propertyGetter1, Getter<? extends Property<?>, T> propertyGetter2) {
		ArrayList<Getter<? extends Property<?>, T>> items = new ArrayList<Getter<? extends Property<?>, T>>();
		items.add(propertyGetter1);
		items.add(propertyGetter2);
		return new BeanObservableList<T>(items);
	}
	
	public static <T> BeanObservableList<T> create(Getter<? extends Property<?>, T> propertyGetter1, Getter<? extends Property<?>, T> propertyGetter2, Getter<? extends Property<?>, T> propertyGetter3) {
		ArrayList<Getter<? extends Property<?>, T>> items = new ArrayList<Getter<? extends Property<?>, T>>();
		items.add(propertyGetter1);
		items.add(propertyGetter2);
		items.add(propertyGetter3);
		return new BeanObservableList<T>(items);
	}
	
	/**
	 * This method creates a warning if you only have a few getters the other creates with no varargs will work without the warning.
	 */
	public static <T> BeanObservableList<T> create(Getter<? extends Property<?>, T>... propertyGetters) {
		return new BeanObservableList<T>(Arrays.asList(propertyGetters));
	}
	
	public static <T> BeanObservableList<T> create(Collection<Getter<? extends Property<?>, T>> propertyGetters) {
		return new BeanObservableList<T>(propertyGetters);
	}
	
	public BeanObservableList(Collection<Getter<? extends Property<?>, B>> propertyGetters) {
		this.propertyGetters.addAll(propertyGetters);
	}
	
	public void addListener(InvalidationListener listener) {
		underlyingList.addListener(listener);
		invalidationListeners.add(listener);
	}
	
	public void addListener(ListChangeListener<? super B> listener) {
		underlyingList.addListener(listener);
		listChangeListeners.add(listener);
	}
	
	public void removeListener(InvalidationListener listener) {
		underlyingList.removeListener(listener);
		invalidationListeners.remove(listener);
	}
	
	public void removeListener(ListChangeListener<? super B> listener) {
		underlyingList.removeListener(listener);
		listChangeListeners.remove(listener);
	}
	
	private class BeanWatcher implements ChangeListener<Object> {
		private final B bean;
		
		private BeanWatcher(B bean) {
			this.bean = bean;
			
			for (Getter<? extends Property<?>, B> propertyGetter : propertyGetters) {
				Property<?> property = propertyGetter.get(bean);
				property.addListener(this);
			}
		}
		
		private void remove() {
			for (Getter<? extends Property<?>, B> propertyGetter : propertyGetters) {
				Property<?> property = propertyGetter.get(bean);
				property.removeListener(this);
			}
		}
		
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			int index = underlyingList.indexOf(bean); // LATER Here we're assuming the element is in the list at most one time				
			if (index < 0) {
				throw new IllegalStateException("The bean [" + bean + "]  should have been in the list");
			} else {
				iterableChangeBuilder.nextReplace(index, index + 1, arrayListOf(bean));
				fireBeanChangeEvent(iterableChangeBuilder.buildAndReset());
			}
		}
		
		private ArrayList<B> arrayListOf(B b) {
			ArrayList<B> arrayList = new ArrayList<B>();
			arrayList.add(b);
			return arrayList;
		}
	}
	
	protected void fireBeanChangeEvent(Change<B> change) {
		for (InvalidationListener invalidationListener : invalidationListeners) {
			invalidationListener.invalidated(this);
		}
		for (ListChangeListener<? super B> listChangeListener : new ArrayList<ListChangeListener<? super B>>(listChangeListeners)) {
			listChangeListener.onChanged(change);
		}
	}
	
	//===== add / remove helpers
	protected void addBeanPropertiesListener(B... beans) {
		for (B bean : beans) {
			addBeanPropertiesListener(bean);
		}
	}
	
	protected void addBeanPropertiesListener(Collection<? extends B> beans) {
		for (B bean : beans) {
			addBeanPropertiesListener(bean);
		}
	}
	
	protected void addBeanPropertiesListener(B bean) {
		if (beanListeners.containsKey(bean)) {
			throw new IllegalArgumentException("This class cannot currently handle having two equal beans in it");
		}
		beanListeners.put(bean, new BeanWatcher(bean));
	}
	
	protected void removeBeanPropertiesListener(B... beans) {
		for (B bean : beans) {
			removeBeanPropertiesListener(bean);
		}
	}
	
	protected void removeBeanPropertiesListener(Collection<? extends B> beans) {
		for (B bean : beans) {
			removeBeanPropertiesListener(bean);
		}
	}
	
	protected void removeBeanPropertiesListener(B bean) {
		if (beanListeners.containsKey(bean)) {
			beanListeners.get(bean).remove();
			beanListeners.remove(bean);
		}
	}
	
	// ============= Need to add listener on add and remove =============
	public boolean add(B e) {
		addBeanPropertiesListener(e);
		return underlyingList.add(e);
	}
	
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		boolean remove = underlyingList.remove(o);
		if (remove) {
			removeBeanPropertiesListener((B) o);
		}
		return remove;
	}
	
	public void add(int index, B element) {
		addBeanPropertiesListener(element);
		underlyingList.add(index, element);
	}
	
	public boolean addAll(B... arg0) {
		addBeanPropertiesListener(arg0);
		return underlyingList.addAll(arg0);
	}
	
	public boolean addAll(Collection<? extends B> c) {
		addBeanPropertiesListener(c);
		return underlyingList.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends B> c) {
		addBeanPropertiesListener(c);
		return underlyingList.addAll(index, c);
	}
	
	public void clear() {
		removeBeanPropertiesListener(this);
		underlyingList.clear();
	}
	
	public void remove(int arg0, int arg1) {
		removeBeanPropertiesListener(subList(arg0, arg1));
		underlyingList.remove(arg0, arg1);
	}
	
	public B remove(int index) {
		B remove = underlyingList.remove(index);
		removeBeanPropertiesListener(remove);
		return remove;
	}
	
	public boolean removeAll(B... arg0) {
		removeBeanPropertiesListener(arg0);
		return underlyingList.removeAll(arg0);
	}
	
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	public boolean retainAll(B... arg0) {
		throw new UnsupportedOperationException();
	}
	
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	public B set(int index, B element) {
		removeBeanPropertiesListener(get(index));
		addBeanPropertiesListener(element);
		return underlyingList.set(index, element);
	}
	
	public boolean setAll(B... arg0) {
		removeBeanPropertiesListener(this);
		addBeanPropertiesListener(arg0);
		return underlyingList.setAll(arg0);
	}
	
	public boolean setAll(Collection<? extends B> arg0) {
		throw new UnsupportedOperationException();
	}
	
	// ============= Methods below here just delegate =============
	public boolean isEmpty() {
		return underlyingList.isEmpty();
	}
	
	public boolean contains(Object o) {
		return underlyingList.contains(o);
	}
	
	public Iterator<B> iterator() {
		return underlyingList.iterator();
	}
	
	public boolean containsAll(Collection<?> c) {
		return underlyingList.containsAll(c);
	}
	
	public boolean equals(Object o) {
		return underlyingList.equals(o);
	}
	
	public int hashCode() {
		return underlyingList.hashCode();
	}
	
	public B get(int index) {
		return underlyingList.get(index);
	}
	
	public int indexOf(Object o) {
		return underlyingList.indexOf(o);
	}
	
	public int lastIndexOf(Object o) {
		return underlyingList.lastIndexOf(o);
	}
	
	public ListIterator<B> listIterator() {
		return underlyingList.listIterator();
	}
	
	public ListIterator<B> listIterator(int index) {
		return underlyingList.listIterator(index);
	}
	
	public int size() {
		return underlyingList.size();
	}
	
	public Object[] toArray() {
		return underlyingList.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return underlyingList.toArray(a);
	}
	
	public List<B> subList(int fromIndex, int toIndex) {
		return underlyingList.subList(fromIndex, toIndex);
	}
	
}
