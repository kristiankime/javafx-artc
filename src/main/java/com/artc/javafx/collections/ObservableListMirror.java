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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ObservableListMirror<M, O> implements ObservableList<M> {
	private final ObservableList<M> mirrorList;
	private final MirrorFactory<M, O> mirrorFactory;

	public ObservableListMirror(MirrorFactory<M, O> mirrorFactory, ObservableList<O> originalList) {
		this.mirrorFactory = mirrorFactory;
		this.mirrorList = FXCollections.observableArrayList();
		originalList.addListener(new Listener());
	}
	
	private class Listener extends ListChangeListenerAdapter<O> {
		@Override
		public void permutedChange(List<Permuation> permuations) {
			List<M> mirrorsToMove = new ArrayList<>();
			for(Permuation permuation : permuations){
				mirrorsToMove.add(mirrorList.get(permuation.oldIndex));
			}
			for(int i = 0; i < mirrorsToMove.size(); i++){
				mirrorList.set(permuations.get(i).newIndex, mirrorsToMove.get(i));
			}
		}
		
		public void updatedChange(int index, O element) {
			throw new UnsupportedOperationException("Coding error, list doesn't seem to use this");
		}
		
		public void removedChange(int index, O item){
			mirrorList.remove(index);
		}
		
		public void addedChange(int index, O element){
			mirrorList.set(index, mirrorFactory.create(element));
		}
	}
	
	public static interface MirrorFactory<M, O> {
		public M create(O original);
	}

	// ====== Methods below here delegate or throw unsupported exceptions ======
	public boolean isEmpty() {
		return mirrorList.isEmpty();
	}

	public boolean contains(Object o) {
		return mirrorList.contains(o);
	}

	public Iterator<M> iterator() {
		return mirrorList.iterator();
	}

	public boolean add(M e) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean containsAll(Collection<?> c) {
		return mirrorList.containsAll(c);
	}

	public boolean addAll(Collection<? extends M> c) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean addAll(int index, Collection<? extends M> c) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public void clear() {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public int hashCode() {
		return mirrorList.hashCode();
	}

	public void add(int index, M element) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean addAll(@SuppressWarnings("unchecked") M... arg0) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public void addListener(InvalidationListener arg0) {
		mirrorList.addListener(arg0);
	}

	public void addListener(ListChangeListener<? super M> arg0) {
		mirrorList.addListener(arg0);
	}

	public boolean equals(Object o) {
		return mirrorList.equals(o);
	}

	public M get(int index) {
		return mirrorList.get(index);
	}

	public int indexOf(Object o) {
		return mirrorList.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return mirrorList.lastIndexOf(o);
	}

	public ListIterator<M> listIterator() {
		return mirrorList.listIterator();
	}

	public ListIterator<M> listIterator(int index) {
		return mirrorList.listIterator(index);
	}

	public void remove(int arg0, int arg1) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public int size() {
		return mirrorList.size();
	}

	public Object[] toArray() {
		return mirrorList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return mirrorList.toArray(a);
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public M set(int index, M element) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public M remove(int index) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean removeAll(@SuppressWarnings("unchecked") M... arg0) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public void removeListener(InvalidationListener arg0) {
		mirrorList.removeListener(arg0);
	}

	public void removeListener(ListChangeListener<? super M> arg0) {
		mirrorList.removeListener(arg0);
	}

	public boolean retainAll(@SuppressWarnings("unchecked") M... arg0) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean setAll(Collection<? extends M> arg0) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public boolean setAll(@SuppressWarnings("unchecked") M... arg0) {
		throw new UnsupportedOperationException("Mirror lists should not be modified directly, modifiy the original");
	}

	public List<M> subList(int fromIndex, int toIndex) {
		return mirrorList.subList(fromIndex, toIndex);
	}	
}
