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

package com.artc.javafx.indirect.beans;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import com.artc.javafx.indirect.IndirectObject;

public class IndirectBean<B> extends BaseIndirectBean<Property<B>, B> implements IndirectObject<B> {
	public static <B> IndirectBean<B> create(B bean) {
		return new IndirectBean<B>(bean);
	}
	
	public static <B> IndirectBean<B> create(Property<B> beanChannel) {
		return new IndirectBean<B>(beanChannel);
	}
	
	public IndirectBean(B bean) {
		this(new SimpleObjectProperty<B>(bean));
	}
	
	public IndirectBean(Property<B> beanChannel) {
		super(beanChannel);
	}
	
	public void setBean(B bean) {
		setUnderlyingObject(bean);
	}
	
	@Override
	public void setUnderlyingObject(B underlyingObject) {
		beanChannel.setValue(underlyingObject);
	}
}
