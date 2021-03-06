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
package com.artclod.javafx.swap.beans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import com.artclod.javafx.swap.Ref;
import com.artclod.javafx.swap.Swap;
import com.artclod.javafx.swap.beans.getter.Getter;
import com.artclod.javafx.swap.beans.property.PropertyRef;
import com.artclod.javafx.swap.beans.property.ValueRef;
import com.artclod.javafx.swap.collections.ObservableListRef;

public interface BeanRef<B> extends Ref<B> {

	public <T> PropertyRef<T> createPropertyRef(Getter<? extends Property<T>, B> getter);

	public <T> ValueRef<T> createValueRef(Getter<? extends T, B> getter);

	public <T> ObservableListRef<T> createListRef(Getter<? extends ObservableList<T>, B> getter);

	public <T> BeanRef<T> createBeanRefFromProperty(Getter<? extends Property<T>, B> getter);

	public <T> BeanRef<T> createBeanRefFromValue(Getter<T, B> getter);

	public <I extends Swap<T>, T> I addSwap(I swap, Getter<T, B> getter);

	public void addPropertySyncher(BeanSwapPropertySyncer<?, B> propertySyncer);

	public void removePropertySyncher(BeanSwapPropertySyncer<?, B> propertySyncer);

	/**
	 * This is an alternative to {@link #getRefObject()} method with a more specific name
	 * 
	 * @return the Bean which this object is a {@link Ref} for
	 */
	public B getBean();
	
	public ObservableValue<B> beanChannel();

	public BooleanProperty nonNullBeanProperty();

}