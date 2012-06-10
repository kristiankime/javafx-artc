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

package com.artc.javafx.indirect.collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;

import org.junit.Test;

import com.artc.javafx.indirect.bean.getter.StringPropertyGetter;


public class BeanObservableListTest {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void list_fires_event_if_bean_updates() {
		TestBean testBean1 = new TestBean("1", "1");
		TestBean testBean2 = new TestBean("2", "2");
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST, TestBean.SECOND);
		list.add(testBean1);
		list.add(testBean2);
		
		ListChangeListener mock = mock(ListChangeListener.class);
		list.addListener(mock);
		
		TestBean.FIRST.get(testBean1).set("c");

		// LATER this test can be made more specific when the BeanObservableList fires the right events
		verify(mock, atLeastOnce()).onChanged(any(Change.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void list_does_not_fires_event_if_bean_is_removed_then_updated() {
		TestBean testBean1 = new TestBean("1", "1");
		TestBean testBean2 = new TestBean("2", "2");
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST, TestBean.SECOND);
		list.add(testBean1);
		list.add(testBean2);
		
		list.remove(testBean1);
		
		ListChangeListener mock = mock(ListChangeListener.class);
		list.addListener(mock);
		
		TestBean.FIRST.get(testBean1).set("c");
		
		verifyZeroInteractions(mock);
	}
	
	private static class TestBean {
		public static final StringPropertyGetter<TestBean> FIRST = new StringPropertyGetter<BeanObservableListTest.TestBean>() {
			@Override
			public StringProperty get(TestBean bean) {
				return bean.first;
			}
		};
		public static final StringPropertyGetter<TestBean> SECOND = new StringPropertyGetter<BeanObservableListTest.TestBean>() {
			@Override
			public StringProperty get(TestBean bean) {
				return bean.second;
			}
		};
		
		private final StringProperty first;
		private final StringProperty second;
		
		private TestBean(String first, String second) {
			this.first = new SimpleStringProperty(first);
			this.second = new SimpleStringProperty(second);
		}
	}
}
