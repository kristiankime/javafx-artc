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

package com.artclod.javafx.swap.beans.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import com.artclod.javafx.Releasable;
import com.artclod.javafx.swap.beans.BeanSwap;

public class SimpleBeanSwap<B> extends BaseBeanSwap<Property<B>, B> implements BeanSwap<B>, Releasable {
	
	public static <B> SimpleBeanSwap<B> create() {
		return new SimpleBeanSwap<B>();
	}
	
	public static <B> SimpleBeanSwap<B> create(B bean) {
		return new SimpleBeanSwap<B>(bean);
	}
	
	public static <B> SimpleBeanSwap<B> create(Property<B> beanChannel) {
		return new SimpleBeanSwap<B>(beanChannel);
	}
	
	public SimpleBeanSwap() {
		this(new SimpleObjectProperty<B>(null));
	}
	
	public SimpleBeanSwap(B bean) {
		this(new SimpleObjectProperty<B>(bean));
	}
	
	public SimpleBeanSwap(Property<B> beanChannel) {
		super(beanChannel);
	}
	
	public void setBean(B bean) {
		swap(bean);
	}
	
	@Override
	public void swap(B underlyingObject) {
		beanChannel.setValue(underlyingObject);
	}
	
	@Override
	public void release() {
		beanChannel.setValue(null);
	}

}
