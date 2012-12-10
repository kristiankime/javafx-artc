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

package com.artc.javafx.indirect.beans;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import com.artc.javafx.indirect.IndirectObject;
import com.artc.javafx.indirect.UncontrolledIndirectObject;
import com.artc.javafx.indirect.beans.getter.Getter;
import com.artc.javafx.indirect.beans.property.IndirectProperty;
import com.artc.javafx.indirect.beans.property.IndirectyPropertyDelegate;
import com.artc.javafx.indirect.collections.IndirectObservableList;
import com.artc.javafx.indirect.collections.IndirectObservableListDelegate;

public class BaseIndirectBean<BC extends ObservableValue<B>, B> implements UncontrolledIndirectObject<B> {
	protected final BC beanChannel;
	protected final BooleanProperty nonNullBean;
	protected final Set<IndirectBeanPropertySyncer<?, B>> propertySyncers;
	
	public BaseIndirectBean(BC beanChannel) {
		if (beanChannel == null)
			throw new NullPointerException("bean channel cannot be null");
		
		this.beanChannel = beanChannel;
		this.nonNullBean = new SimpleBooleanProperty(beanChannel.getValue() != null);
		this.propertySyncers = new HashSet<IndirectBeanPropertySyncer<?, B>>();
		
		this.beanChannel.addListener(new ChangeListener<B>() {
			@Override
			public void changed(ObservableValue<? extends B> observable, B oldValue, B newValue) {
				onBeanChange(newValue);
			}
		});
	}
	
	public <I extends IndirectObject<T>, T> I getIndirect(I indirect, Getter<T, B> getter){
		addPropertySyncher(IndirectBeanPropertySyncer.create(indirect, getter));
		return indirect;
	}

	public <T> IndirectProperty<T> getIndirectProperty(Getter<? extends Property<T>, B> getter) {
		IndirectyPropertyDelegate<T> indirectyProperty = new IndirectyPropertyDelegate<T>();
		addPropertySyncher(IndirectBeanPropertySyncer.create(indirectyProperty, getter));
		return indirectyProperty;
	}
	
	public <T> IndirectBean<T> getIndirectFixedBean(Getter<T, B> getter) {
		IndirectBean<T> indirectBean = new IndirectBean<T>();
		addPropertySyncher(IndirectBeanPropertySyncer.create(indirectBean, getter));
		return indirectBean;
	}
	
	public <T> IndirectBean<T> getIndirectPropertyBean(Getter<? extends Property<T>, B> getter) {
		IndirectProperty<T> beanChannel = getIndirectProperty(getter);
		return new IndirectBean<T>(beanChannel);
	}

	public <T> IndirectObservableList<T> getIndirectList(Getter<? extends ObservableList<T>, B> getter) {
		IndirectObservableListDelegate<T> indirectObservableList = new IndirectObservableListDelegate<T>();
		addPropertySyncher(IndirectBeanPropertySyncer.create(indirectObservableList, getter));
		return indirectObservableList;
	}
	
	public void addPropertySyncher(IndirectBeanPropertySyncer<?, B> propertySyncer) {
		propertySyncers.add(propertySyncer);
		propertySyncer.sync(beanChannel.getValue());
	}
	
	public void removePropertySyncher(IndirectBeanPropertySyncer<?, B> propertySyncer) {
		propertySyncers.remove(propertySyncer);
		propertySyncer.sync(null);
	}
	
	private void onBeanChange(B newBean) {
		this.nonNullBean.set(newBean != null);
		for (IndirectBeanPropertySyncer<?, B> propertySyncer : propertySyncers) {
			propertySyncer.sync(newBean);
		}
	}
	
	public BC beanChannel() {
		return beanChannel;
	}
	
	public BooleanProperty nonNullBeanProperty() {
		return nonNullBean;
	}
	
	public B getBean(){
		return getUnderlyingObject();
	}
	
	@Override
	public B getUnderlyingObject() {
		return beanChannel.getValue();
	}	
}
