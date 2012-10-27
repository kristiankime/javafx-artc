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

package com.artc.javafx.indirect.bean.property;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import com.artc.javafx.indirect.bean.value.BaseIndirectObservableValue;

public class IndirectPropertyManual<T> extends BaseIndirectObservableValue<Property<T>, T> implements IndirectProperty<T> {
	private ObservableValue<? extends T> unidirectionBinding = null; // LATER since unbind removes any unibinding we can accidentally remove one placed on the underlying object
	private final Set<Property<T>> bidirectionalBindings = new HashSet<Property<T>>();
	
	public static <T> IndirectPropertyManual<T> create() {
		return new IndirectPropertyManual<T>();
	}
	
	public static <T> IndirectPropertyManual<T> create(Property<T> underlyingProperty) {
		return new IndirectPropertyManual<T>(underlyingProperty);
	}
	
	public IndirectPropertyManual() {
		super(new SimpleObjectProperty<T>());
	}
	
	public IndirectPropertyManual(Property<T> underlyingProperty) {
		super(underlyingProperty);
	}
	
	@Override
	protected void removeListeners() {
		super.removeListeners();
		if (underlyingObject != null) {
			if (unidirectionBinding != null) {
				underlyingObject.unbind();
			}
			for (Property<T> bidirectionalBinding : bidirectionalBindings) {
				underlyingObject.unbindBidirectional(bidirectionalBinding);
			}
		}
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		if (underlyingObject != null) {
			if (unidirectionBinding != null) {
				underlyingObject.bind(unidirectionBinding);
			}
			for (Property<T> bidirectionalBinding : bidirectionalBindings) {
				underlyingObject.bindBidirectional(bidirectionalBinding);
			}
		}
	}
	
	@Override
	public void bindBidirectional(Property<T> other) {
		if (underlyingObject != null) {
			underlyingObject.bindBidirectional(other);
		}
		bidirectionalBindings.add(other);
	}
	
	@Override
	public void unbindBidirectional(Property<T> other) {
		if (underlyingObject != null) {
			underlyingObject.unbindBidirectional(other);
		}
		bidirectionalBindings.remove(other);
	}
	
	@Override
	public void bind(ObservableValue<? extends T> observable) {
		if (underlyingObject != null) {
			underlyingObject.bind(observable);
		}
		unidirectionBinding = observable;
	}
	
	@Override
	public void unbind() {
		if (underlyingObject != null) {
			underlyingObject.unbind();
		}
		unidirectionBinding = null;
	}
	
	@Override
	public Object getBean() {
		return underlyingObject != null ? underlyingObject.getBean() : null;
	}
	
	@Override
	public String getName() {
		return underlyingObject != null ? underlyingObject.getName() : null;
	}
	
	@Override
	public boolean isBound() {
		// LATER We indicated that this object is bound if there is an unidirectionBinding even if they underlyingObject is null. Does this make sense?
		return underlyingObject != null ? underlyingObject.isBound() : unidirectionBinding != null;
	}
	
	@Override
	public void setValue(T value) {
		if (underlyingObject != null) {
			underlyingObject.setValue(value);
		}
	}
}
