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

package com.artc.javafx.collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;

import org.junit.Test;

import com.artc.javafx.indirect.beans.getter.StringPropertyGetter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BeanObservableListTest {

	@Test
	public void adding_a_bean_adds_listeners_to_the_specified_properties() {
		TestBean testBean = mock(TestBean.class);
		StringProperty first = mock(StringProperty.class);
		StringProperty second = mock(StringProperty.class);
		when(testBean.getFirst()).thenReturn(first);
		when(testBean.getSecond()).thenReturn(second);
		
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST);
		list.add(testBean);
		
		verify(first).addListener(any(ChangeListener.class));
		verifyZeroInteractions(second);
	}

	@Test
	public void list_fires_event_if_bean_updates() {
		TestBean testBean1 = new TestBean("1", "1");
		TestBean testBean2 = new TestBean("2", "2");
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST, TestBean.SECOND);
		list.add(testBean1);
		list.add(testBean2);

		ListChangeListener mock = mock(ListChangeListener.class);
		list.addListener(mock);

		testBean1.getFirst().set("c");

		// LATER this test can be made more specific when the BeanObservableList
		// fires the right events
		verify(mock, atLeastOnce()).onChanged(any(Change.class));
	}

	@Test
	public void removing_a_bean_removes_listeners_to_the_specified_properties() {
		TestBean testBean = mock(TestBean.class);
		StringProperty first = mock(StringProperty.class);
		StringProperty second = mock(StringProperty.class);
		when(testBean.getFirst()).thenReturn(first);
		when(testBean.getSecond()).thenReturn(second);
		
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST);
		list.add(testBean);
		list.remove(testBean);
		
		verify(first).addListener(any(ChangeListener.class));
		verify(first).removeListener(any(ChangeListener.class));
		verifyZeroInteractions(second);
	}
	
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

		testBean1.getFirst().set("c");

		verifyZeroInteractions(mock);
	}
	
	
	@Test
	public void even_after_list_is_permuted_proper_events_are_fired() {
		TestBean testBean2 = new TestBean("2", "2");
		TestBean testBean1 = new TestBean("1", "1");
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST, TestBean.SECOND);
		list.add(testBean2);
		list.add(testBean1);
		ListChangeListener mock = mock(ListChangeListener.class);
		FXCollections.sort(list);
		list.remove(testBean2);
		
		list.addListener(mock);
		testBean1.getFirst().set("c");
		
		// LATER this test can be made more specific when the BeanObservableList
		// fires the right events
		verify(mock, atLeastOnce()).onChanged(any(Change.class));
	}
 
	@Test
	public void even_after_list_is_permuted_improper_events_are_not_fired() {
		TestBean testBean2 = new TestBean("2", "2");
		TestBean testBean1 = new TestBean("1", "1");
		BeanObservableList<TestBean> list = BeanObservableList.create(TestBean.FIRST, TestBean.SECOND);
		list.add(testBean2);
		list.add(testBean1);
		ListChangeListener mock = mock(ListChangeListener.class);
		FXCollections.sort(list);
		list.remove(testBean1);

		list.addListener(mock);
		testBean1.getFirst().set("c");
		
		// LATER this test can be made more specific when the BeanObservableList
		// fires the right events
		verify(mock, atMost(0)).onChanged(any(Change.class));
	}
	
	// ===  Support ===
	private static class TestBean implements Comparable<TestBean> {
		public static final StringPropertyGetter<TestBean> FIRST = new StringPropertyGetter<TestBean>() {
			@Override
			public StringProperty get(TestBean bean) {
				return bean.getFirst();
			}
		};
		public static final StringPropertyGetter<TestBean> SECOND = new StringPropertyGetter<TestBean>() {
			@Override
			public StringProperty get(TestBean bean) {
				return bean.getSecond();
			}
		};

		private final StringProperty first;
		private final StringProperty second;

		private TestBean(String first, String second) {
			this.first = new SimpleStringProperty(first);
			this.second = new SimpleStringProperty(second);
		}

		public StringProperty getFirst() {
			return first;
		}

		public StringProperty getSecond() {
			return second;
		}

		@Override
		public int compareTo(TestBean o) {
			return first.getValue().compareTo(o.first.getValue());
		}
	}
}
