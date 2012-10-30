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

package com.artc.javafx.collections;

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
import javafx.collections.ObservableList;

import com.artc.javafx.indirect.beans.getter.Getter;

public class BeanObservableList<B> implements ObservableList<B> {
	private final ObservableList<B> underlyingList;
	private final Set<Getter<? extends Property<?>, B>> propertyGetters = new HashSet<Getter<? extends Property<?>, B>>();
	private final Map<B, BeanPropertyListener> beanListeners = new HashMap<B, BeanPropertyListener>();
	
	@SafeVarargs
	public static <B> BeanObservableList<B> create(Getter<? extends Property<?>, B>... getters) {
		return new BeanObservableList<B>(FXCollections.<B> observableArrayList(), Arrays.asList(getters));
	}
		
	@SafeVarargs
	public static <B> BeanObservableList<B> create(Collection<B> beans, Getter<? extends Property<?>, B>... getters) {
		return new BeanObservableList<B>(beans, Arrays.asList(getters));
	}
	
	public BeanObservableList(Collection<B> beans, Collection<Getter<? extends Property<?>, B>> propertyGetters) {
		this.underlyingList = FXCollections.<B> observableArrayList(beans);
		this.propertyGetters.addAll(propertyGetters);
		this.underlyingList.addListener(new UnderlyingListSynchronizer());
		for (B bean : underlyingList) {
			addBeanPropertyListener(bean);
		}
	}
	
	private void addBeanPropertyListener(B item) {
		if (item == null) {
			return;
		}
		if (beanListeners.containsKey(item)) {
			throw new IllegalArgumentException("Attemping to add the bean [" + item + "] to the " + BeanObservableList.class.getSimpleName() + " but it was already there, currently only unique beans are supported");
		}
		beanListeners.put(item, new BeanPropertyListener(item));
	};
	
	private class BeanPropertyListener implements ChangeListener<Object> {
		private final B bean;
		
		public BeanPropertyListener(B bean) {
			if(bean == null)
				throw new NullPointerException("Listener should not be attached to null bean");
				
			this.bean = bean;
			for (Getter<? extends Property<?>, B> getter : propertyGetters) {
				getter.get(bean).addListener(this);
			}
		}
		
		public void release() {
			for (Getter<? extends Property<?>, B> getter : propertyGetters) {
				getter.get(bean).removeListener(this);
			}
		}
		
		@Override
		public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
			int index = underlyingList.indexOf(bean);
			underlyingList.set(index, null); // LATER this is a hack to fire the proper event, should figure out a better way to make it happen later
			underlyingList.set(index, bean);
		}
	}
	
	private class UnderlyingListSynchronizer extends ListChangeListenerAdapter<B> {
		public void addedChange(int index, B item) {
			addBeanPropertyListener(item);
		}
		
		public void removedChange(B item) {
			if (item == null) {
				return;
			}
			beanListeners.remove(item).release();
		};
		
		public void updatedChange(int index, B element) {
			throw new UnsupportedOperationException("TODO unclear how to handle this case");
		};
	}

	// ============= Methods below here just delegate =============	
	public void addListener(InvalidationListener listener) {
		underlyingList.addListener(listener);
	}
	
	public void addListener(ListChangeListener<? super B> listener) {
		underlyingList.addListener(listener);
	}
	
	public void removeListener(InvalidationListener listener) {
		underlyingList.removeListener(listener);
	}
	
	public void removeListener(ListChangeListener<? super B> listener) {
		underlyingList.removeListener(listener);
	}
	
	public boolean contains(Object o) {
		return underlyingList.contains(o);
	}
	
	public boolean add(B e) {
		return underlyingList.add(e);
	}
	
	@SuppressWarnings("unchecked")
	public boolean addAll(B... arg0) {
		return underlyingList.addAll(arg0);
	}
	
	public boolean containsAll(Collection<?> c) {
		return underlyingList.containsAll(c);
	}
	
	public boolean addAll(Collection<? extends B> c) {
		return underlyingList.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends B> c) {
		return underlyingList.addAll(index, c);
	}
	
	public void clear() {
		underlyingList.clear();
	}
	
	public boolean equals(Object o) {
		return underlyingList.equals(o);
	}
	
	public B get(int index) {
		return underlyingList.get(index);
	}
	
	public void add(int index, B element) {
		underlyingList.add(index, element);
	}
	
	public void remove(int arg0, int arg1) {
		underlyingList.remove(arg0, arg1);
	}
	
	@SuppressWarnings("unchecked")
	public boolean removeAll(B... arg0) {
		return underlyingList.removeAll(arg0);
	}
	
	@SuppressWarnings("unchecked")
	public boolean retainAll(B... arg0) {
		return underlyingList.retainAll(arg0);
	}
	
	@SuppressWarnings("unchecked")
	public boolean setAll(B... arg0) {
		return underlyingList.setAll(arg0);
	}
	
	public boolean setAll(Collection<? extends B> arg0) {
		return underlyingList.setAll(arg0);
	}
	
	public int size() {
		return underlyingList.size();
	}
	
	public boolean isEmpty() {
		return underlyingList.isEmpty();
	}
	
	public Iterator<B> iterator() {
		return underlyingList.iterator();
	}
	
	public Object[] toArray() {
		return underlyingList.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return underlyingList.toArray(a);
	}
	
	public boolean remove(Object o) {
		return underlyingList.remove(o);
	}
	
	public boolean removeAll(Collection<?> c) {
		return underlyingList.removeAll(c);
	}
	
	public boolean retainAll(Collection<?> c) {
		return underlyingList.retainAll(c);
	}
	
	public int hashCode() {
		return underlyingList.hashCode();
	}
	
	public B set(int index, B element) {
		return underlyingList.set(index, element);
	}
	
	public B remove(int index) {
		return underlyingList.remove(index);
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
	
	public List<B> subList(int fromIndex, int toIndex) {
		return underlyingList.subList(fromIndex, toIndex);
	}
}
