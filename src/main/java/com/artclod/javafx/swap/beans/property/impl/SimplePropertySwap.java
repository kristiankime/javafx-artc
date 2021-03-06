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

package com.artclod.javafx.swap.beans.property.impl;

import com.artclod.javafx.swap.beans.property.PropertySwap;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SimplePropertySwap<T> implements PropertySwap<T> {
	private final Property<T> delegate;
	private Property<T> swap = null;
	private ObservableValue<? extends T> unidirectionBinding = null;
	
	public static <T> SimplePropertySwap<T> create() {
		return new SimplePropertySwap<T>();
	}
	
	public static <T> SimplePropertySwap<T> create(Property<T> swap) {
		return new SimplePropertySwap<T>(swap);
	}
	
	public SimplePropertySwap() {
		this.delegate = new SimpleObjectProperty<T>();
	}
	
	public SimplePropertySwap(Property<T> swap) {
		this.delegate = new SimpleObjectProperty<T>();
		swapRefObject(swap);
	}
	
	@Override
	public void swapRefObject(Property<T> newUnderlyingObject) {
		// Unbind everything in preparation for the change
		if (swap != null) {
			delegate.unbindBidirectional(swap);
		}
		delegate.unbind();
		
		// Make the change
		swap = newUnderlyingObject;
		
		if (swap != null) {
			// Rebind if the new objects exists
			delegate.bindBidirectional(swap);
			if (unidirectionBinding != null) {
				delegate.bind(unidirectionBinding);
			}
		} else {
			// The value is fixed at null if the underlyingObject is null
			delegate.setValue(null);
		}
	}
	
	@Override
	public Property<T> getRefObject() {
		return swap;
	}
	
	@Override
	public Object getBean() {
		return swap != null ? swap.getBean() : null;
	}
	
	@Override
	public String getName() {
		return swap != null ? swap.getName() : null;
	}
	
	@Override
	public void bind(ObservableValue<? extends T> observable) {
		if (swap != null) {
			delegate.bind(observable);
		}
		unidirectionBinding = observable;
	}
	
	@Override
	public void unbind() {
		delegate.unbind();
		unidirectionBinding = null;
	}
	
	@Override
	public void setValue(T value) {
		// The value is fixed at null if the underlyingObject is null
		if (swap != null) {
			delegate.setValue(value);
		}
	}
	
	// ================ below here are delegate calls ================
	@Override
	public void addListener(ChangeListener<? super T> listener) {
		delegate.addListener(listener);
	}
	
	@Override
	public void addListener(InvalidationListener listener) {
		delegate.addListener(listener);
	}
	
	@Override
	public void bindBidirectional(Property<T> other) {
		delegate.bindBidirectional(other);
	}
	
	@Override
	public T getValue() {
		return delegate.getValue();
	}
	
	@Override
	public boolean isBound() {
		return delegate.isBound();
	}
	
	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		delegate.removeListener(listener);
	}
	
	@Override
	public void removeListener(InvalidationListener listener) {
		delegate.removeListener(listener);
	}
	
	@Override
	public void unbindBidirectional(Property<T> other) {
		delegate.unbindBidirectional(other);
	}
}
