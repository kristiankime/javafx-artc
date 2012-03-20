/*
 * Copyright (c) 2012, Kristian Kime
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
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
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */

package com.artc.javafx.indirect.bean.value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.artc.javafx.indirect.IndirectObject;

public class BaseIndirectObservableValue<V extends ObservableValue<T>, T> implements ObservableValue<T>, IndirectObject<V> {
	protected V underlyingObject;
	protected final T valueWhenUnderlyingObjectIsNull;
	protected final IndirectInvalidationListener indirectInvalidationListener = new IndirectInvalidationListener();
	protected final IndirectChangeListener indirectChangeListener = new IndirectChangeListener(); // LATER we always install a change listener even if we don't need one. This forces change events to fire even if they are not needed.
	
	public static <V extends ObservableValue<T>, T> BaseIndirectObservableValue<V, T> create(V underlyingObject){
		return new BaseIndirectObservableValue<V, T>(underlyingObject);
	}

	public static <V extends ObservableValue<T>, T> BaseIndirectObservableValue<V, T> create(V underlyingObject, T valueWhenUnderlyingObjectIsNull){
		return new BaseIndirectObservableValue<V, T>(underlyingObject, valueWhenUnderlyingObjectIsNull);
	}
	
	public BaseIndirectObservableValue(V underlyingObject) {
		this(underlyingObject, null);
	}
	
	public BaseIndirectObservableValue(V underlyingObject, T valueWhenUnderlyingObjectIsNull) {
		this.underlyingObject = underlyingObject;
		this.valueWhenUnderlyingObjectIsNull = valueWhenUnderlyingObjectIsNull;
		if (underlyingObject != null) { // NOTE we do not use installListeners() here since subclasses are allowed to override it
			underlyingObject.addListener(indirectChangeListener);
			underlyingObject.addListener(indirectInvalidationListener);
		}
	}
	
	@Override
	public V getUnderlyingObject() {
		return underlyingObject;
	}
	
	@Override
	public final void setUnderlyingObject(V newUnderlyingObject) {
		removeListeners();
		V oldUnderlyingObject = this.underlyingObject;
		this.underlyingObject = newUnderlyingObject;
		installListeners();
		fireSetUnderlyingObjectChangeEvents(oldUnderlyingObject, newUnderlyingObject);
	}
	
	protected void removeListeners() {
		if (underlyingObject != null) {
			underlyingObject.removeListener(indirectChangeListener);
			underlyingObject.removeListener(indirectInvalidationListener);
		}
	}
	
	protected void installListeners() {
		if (underlyingObject != null) {
			underlyingObject.addListener(indirectChangeListener);
			underlyingObject.addListener(indirectInvalidationListener);
		}
	}
	
	protected void fireSetUnderlyingObjectChangeEvents(V oldUnderlyingProperty, V newUnderlyingProperty) {
		for (InvalidationListener invalidationListener : indirectInvalidationListener.getInvalidationListeners()) {
			invalidationListener.invalidated(this); // LATER this could be inefficient if we've already indicated an invalid state
		}
		for (ChangeListener<? super T> changeListener : indirectChangeListener.getChangeListeners()) {
			T oldValue = valueWhenUnderlyingObjectIsNull;
			if (oldUnderlyingProperty != null) {
				oldValue = oldUnderlyingProperty.getValue();
			}
			T newValue = valueWhenUnderlyingObjectIsNull;
			if (newUnderlyingProperty != null) {
				newValue = newUnderlyingProperty.getValue();
			}
			changeListener.changed(this, oldValue, newValue);
		}
	}
	
	@Override
	public void addListener(ChangeListener<? super T> listener) {
		indirectChangeListener.addListener(listener);
	}
	
	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		indirectChangeListener.removeListener(listener);
	}
	
	@Override
	public void addListener(InvalidationListener listener) {
		indirectInvalidationListener.addListener(listener);
	}
	
	@Override
	public void removeListener(InvalidationListener listener) {
		indirectInvalidationListener.removeListener(listener);
	}
	
	@Override
	public T getValue() {
		if (underlyingObject == null) {
			return valueWhenUnderlyingObjectIsNull;
		}
		return underlyingObject.getValue();
	}
	
	protected class IndirectInvalidationListener implements InvalidationListener {
		protected final Set<InvalidationListener> invalidationListeners = new HashSet<InvalidationListener>(); // LATER the API allows for duplicate listeners
		
		public void addListener(InvalidationListener listener) {
			invalidationListeners.add(listener);
		}
		
		public void removeListener(InvalidationListener listener) {
			invalidationListeners.remove(listener);
		}
		
		@Override
		public void invalidated(Observable observable) {
			for (InvalidationListener invalidationListener : getInvalidationListeners()) {
				invalidationListener.invalidated(BaseIndirectObservableValue.this); // LATER this could be inefficient if we've already indicated an invalid state (from setUnderlyingObject)
			}
		}

		public Iterable<InvalidationListener> getInvalidationListeners() {
			return new ArrayList<InvalidationListener>(invalidationListeners);
		}
	}
	
	protected class IndirectChangeListener implements ChangeListener<T> {
		private final Set<ChangeListener<? super T>> changeListeners = new HashSet<ChangeListener<? super T>>(); // LATER the API allows for duplicate listeners
		
		
		public void addListener(ChangeListener<? super T> listener) {
			changeListeners.add(listener);
		}
		
		public void removeListener(ChangeListener<? super T> listener) {
			changeListeners.remove(listener);
		}
		
		@Override
		public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
			for (ChangeListener<? super T> changeListener : getChangeListeners()) {
				changeListener.changed(BaseIndirectObservableValue.this, oldValue, newValue);
			}
		}

		public Iterable<ChangeListener<? super T>> getChangeListeners() {
			return new ArrayList<ChangeListener<? super T>>(changeListeners);
		}
	}
}
