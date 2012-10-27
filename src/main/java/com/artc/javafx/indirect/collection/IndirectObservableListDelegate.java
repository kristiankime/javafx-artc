package com.artc.javafx.indirect.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class IndirectObservableListDelegate<E> implements IndirectObservableList<E> {
	private final ObservableList<E> delegate;
	private ObservableList<E> underlyingObject;
	
	public static <T> IndirectObservableListDelegate<T> create() {
		return new IndirectObservableListDelegate<T>();
	}
	
	public static <T> IndirectObservableListDelegate<T> create(ObservableList<T> list) {
		IndirectObservableListDelegate<T> ret = new IndirectObservableListDelegate<T>();
		ret.setUnderlyingObject(list);
		return ret;
	}
	
	public IndirectObservableListDelegate() {
		this.delegate = FXCollections.observableArrayList();
	}
	
	@Override
	public ObservableList<E> getUnderlyingObject() {
		return underlyingObject;
	}
	
	@Override
	public void setUnderlyingObject(ObservableList<E> newUnderlyingObject) {
		if (underlyingObject != null) {
			Bindings.unbindContentBidirectional(delegate, underlyingObject);
		}
		
		underlyingObject = newUnderlyingObject;
		
		if (underlyingObject != null) {
			Bindings.bindContentBidirectional(delegate, underlyingObject);
		} else {
			Bindings.bindContentBidirectional(delegate, FXCollections.<E> emptyObservableList());
		}
	}
	
	// ======== below here are delegate methods ====
	public boolean isEmpty() {
		return delegate.isEmpty();
	}
	
	public boolean contains(Object o) {
		return delegate.contains(o);
	}
	
	public Iterator<E> iterator() {
		return delegate.iterator();
	}
	
	public boolean add(E e) {
		return delegate.add(e);
	}
	
	public boolean remove(Object o) {
		return delegate.remove(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}
	
	public boolean addAll(Collection<? extends E> c) {
		return delegate.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends E> c) {
		return delegate.addAll(index, c);
	}
	
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}
	
	public boolean equals(Object o) {
		return delegate.equals(o);
	}
	
	public int hashCode() {
		return delegate.hashCode();
	}
	
	public E get(int index) {
		return delegate.get(index);
	}
	
	public void add(int index, E element) {
		delegate.add(index, element);
	}
	
	@SuppressWarnings("unchecked")
	public boolean addAll(E... arg0) {
		return delegate.addAll(arg0);
	}
	
	public void addListener(InvalidationListener arg0) {
		delegate.addListener(arg0);
	}
	
	public void addListener(ListChangeListener<? super E> arg0) {
		delegate.addListener(arg0);
	}
	
	public void clear() {
		delegate.clear();
	}
	
	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}
	
	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}
	
	public ListIterator<E> listIterator() {
		return delegate.listIterator();
	}
	
	public ListIterator<E> listIterator(int index) {
		return delegate.listIterator(index);
	}
	
	public void remove(int arg0, int arg1) {
		delegate.remove(arg0, arg1);
	}
	
	public E remove(int index) {
		return delegate.remove(index);
	}
	
	@SuppressWarnings("unchecked")
	public boolean removeAll(E... arg0) {
		return delegate.removeAll(arg0);
	}
	
	public void removeListener(InvalidationListener arg0) {
		delegate.removeListener(arg0);
	}
	
	public void removeListener(ListChangeListener<? super E> arg0) {
		delegate.removeListener(arg0);
	}
	
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}
	
	@SuppressWarnings("unchecked")
	public boolean retainAll(E... arg0) {
		return delegate.retainAll(arg0);
	}
	
	public E set(int index, E element) {
		return delegate.set(index, element);
	}
	
	public boolean setAll(Collection<? extends E> arg0) {
		return delegate.setAll(arg0);
	}
	
	@SuppressWarnings("unchecked")
	public boolean setAll(E... arg0) {
		return delegate.setAll(arg0);
	}
	
	public int size() {
		return delegate.size();
	}
	
	public Object[] toArray() {
		return delegate.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}
	
	public List<E> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}
}
