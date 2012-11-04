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

import static junit.framework.Assert.assertEquals;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import org.junit.Test;

import com.artc.javafx.indirect.beans.BaseIndirectBean;
import com.artc.javafx.indirect.beans.getter.IntegerPropertyGetter;
import com.artc.javafx.indirect.beans.property.IndirectProperty;

public class BaseIndirectBeanTest {
	@Test(expected = NullPointerException.class)
	public void constructor_throws_if_bean_channel_is_null() {
		create(null);
	}
	
	@Test
	public void indirect_properties_start_with_underlying_properties_value() {
		IntBean intBean = new IntBean(1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(new SimpleObjectProperty<IntBean>(intBean));
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		assertEquals(1, indirectOneProperty.getValue());
	}
	
	@Test
	public void nonNullBean_true_if_bean_is_non_null() {
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(new SimpleObjectProperty<IntBean>(new IntBean(1)));
		
		assertEquals(true, indirectBean.nonNullBeanProperty().getValue().booleanValue());
	}
	
	@Test
	public void nonNullBean_false_if_bean_is_null() {
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(new SimpleObjectProperty<IntBean>((IntBean) null));
		
		assertEquals(false, indirectBean.nonNullBeanProperty().getValue().booleanValue());
	}
	
	@Test
	public void indirect_properties_update_when_the_underlying_property_updates() {
		IntBean intBean = new IntBean(1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(new SimpleObjectProperty<IntBean>(intBean));
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		intBean.getOneProperty().setValue(5);
		
		assertEquals(5, indirectOneProperty.getValue());
	}
	
	@Test
	public void underlying_property_updates_when_indirect_properties_updates() {
		IntBean intBean = new IntBean(1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(new SimpleObjectProperty<IntBean>(intBean));
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		indirectOneProperty.setValue(7);
		
		assertEquals(7, intBean.getOneProperty().getValue().intValue());
	}
	
	@Test
	public void switching_the_underlying_bean_switches_which_underlying_objects_the_indirect_properties_point_to() {
		IntBean intBean1 = new IntBean(1);
		IntBean intBean2 = new IntBean(2);
		SimpleObjectProperty<IntBean> beanChannel = new SimpleObjectProperty<IntBean>(intBean1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(beanChannel);
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		beanChannel.setValue(intBean2);
		assertEquals(2, indirectOneProperty.getValue().intValue());
	}
	
	@Test
	public void switching_the_underlying_bean_setting_the_value_on_the_new_property_updates_the_indirect_one() {
		IntBean intBean1 = new IntBean(1);
		IntBean intBean2 = new IntBean(2);
		SimpleObjectProperty<IntBean> beanChannel = new SimpleObjectProperty<IntBean>(intBean1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(beanChannel);
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		beanChannel.setValue(intBean2);
		indirectOneProperty.setValue(9);
		
		assertEquals(9, intBean2.getOneProperty().getValue().intValue());
	}
	
	@Test
	public void switching_the_underlying_bean_to_null_sets_everyting_to_null() {
		IntBean intBean1 = new IntBean(1);
		SimpleObjectProperty<IntBean> beanChannel = new SimpleObjectProperty<IntBean>(intBean1);
		BaseIndirectBean<SimpleObjectProperty<IntBean>, IntBean> indirectBean = create(beanChannel);
		IndirectProperty<Number> indirectOneProperty = indirectBean.getIndirectProperty(IntBean.GET_ONE_PROPERTY);
		
		beanChannel.setValue(null);
		assertEquals(null, indirectOneProperty.getValue());
	}
	
	private static <BC extends ObservableValue<B>, B> BaseIndirectBean<BC, B> create(BC beanChannel) {
		return new BaseIndirectBean<BC, B>(beanChannel);
	}
	
	private static class IntBean {
		public static final IntegerPropertyGetter<IntBean> GET_ONE_PROPERTY = new IntegerPropertyGetter<IntBean>() {
			@Override
			public IntegerProperty get(IntBean bean) {
				return bean.oneProperty;
			}
		};
		
		private final SimpleIntegerProperty oneProperty;
		
		public IntBean(Integer one) {
			this.oneProperty = new SimpleIntegerProperty(one);
		}
		
		public SimpleIntegerProperty getOneProperty() {
			return oneProperty;
		}
	}
}
