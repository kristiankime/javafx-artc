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

package com.artclod.javafx.swap.beans;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import com.artclod.javafx.swap.Swap;
import com.artclod.javafx.swap.CanSwap;
import com.artclod.javafx.swap.beans.getter.Getter;
import com.artclod.javafx.swap.beans.property.PropertySwap;
import com.artclod.javafx.swap.beans.property.SimplePropertySwap;
import com.artclod.javafx.swap.collections.ArrayObservableListSwap;
import com.artclod.javafx.swap.collections.ObservableListSwap;

public class BaseBeanSwap<BC extends ObservableValue<B>, B> implements CanSwap<B> {
	protected final BC beanChannel;
	protected final BooleanProperty nonNullBean;
	protected final Set<BeanSwapPropertySyncer<?, B>> propertySyncers;
	
	public BaseBeanSwap(BC beanChannel) {
		if (beanChannel == null)
			throw new NullPointerException("bean channel cannot be null");
		
		this.beanChannel = beanChannel;
		this.nonNullBean = new SimpleBooleanProperty(beanChannel.getValue() != null);
		this.propertySyncers = new HashSet<BeanSwapPropertySyncer<?, B>>();
		
		this.beanChannel.addListener(new ChangeListener<B>() {
			@Override
			public void changed(ObservableValue<? extends B> observable, B oldValue, B newValue) {
				onBeanChange(newValue);
			}
		});
	}
	
	public <I extends Swap<T>, T> I getSwap(I swap, Getter<T, B> getter){
		addPropertySyncher(BeanSwapPropertySyncer.create(swap, getter));
		return swap;
	}

	public <T> PropertySwap<T> getProperty(Getter<? extends Property<T>, B> getter) {
		SimplePropertySwap<T> indirectyProperty = new SimplePropertySwap<T>();
		addPropertySyncher(BeanSwapPropertySyncer.create(indirectyProperty, getter));
		return indirectyProperty;
	}
	
	public <T> BeanSwap<T> getBeanDirect(Getter<T, B> getter) {
		BeanSwap<T> indirectBean = new BeanSwap<T>();
		addPropertySyncher(BeanSwapPropertySyncer.create(indirectBean, getter));
		return indirectBean;
	}
	
	public <T> BeanSwap<T> getBeanProperty(Getter<? extends Property<T>, B> getter) {
		PropertySwap<T> beanChannel = getProperty(getter);
		return new BeanSwap<T>(beanChannel);
	}

	public <T> ObservableListSwap<T> getList(Getter<? extends ObservableList<T>, B> getter) {
		ArrayObservableListSwap<T> indirectObservableList = new ArrayObservableListSwap<T>();
		addPropertySyncher(BeanSwapPropertySyncer.create(indirectObservableList, getter));
		return indirectObservableList;
	}
	
	public void addPropertySyncher(BeanSwapPropertySyncer<?, B> propertySyncer) {
		propertySyncers.add(propertySyncer);
		propertySyncer.sync(beanChannel.getValue());
	}
	
	public void removePropertySyncher(BeanSwapPropertySyncer<?, B> propertySyncer) {
		propertySyncers.remove(propertySyncer);
		propertySyncer.sync(null);
	}
	
	private void onBeanChange(B newBean) {
		this.nonNullBean.set(newBean != null);
		for (BeanSwapPropertySyncer<?, B> propertySyncer : propertySyncers) {
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
		return getSwap();
	}
	
	@Override
	public B getSwap() {
		return beanChannel.getValue();
	}	
}
