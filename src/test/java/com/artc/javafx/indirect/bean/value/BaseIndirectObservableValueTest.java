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

package com.artc.javafx.indirect.bean.value;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import org.junit.Test;

import com.artc.javafx.indirect.bean.value.BaseIndirectObservableValue;


public class BaseIndirectObservableValueTest {
	@Test
	public void constructor_getUnderlyingObject_returns_the_object_it_was_constructed_with() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		assertEquals(property, indirectObservableValue.getUnderlyingObject());
	}
	
	@Test
	public void getValue_returns_underlying_objects_get_value() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		assertEquals("a", indirectObservableValue.getValue());
	}
	
	@Test
	public void constructor_with_null_value_is_null() {
		BaseIndirectObservableValue<SimpleObjectProperty<String>, String> indirectObservableValue = BaseIndirectObservableValue.create(null);

		assertNull(indirectObservableValue.getValue());
	}
	
	@Test
	public void constructor_with_a_valueWhenUnderlyingObjectIsNull_returns_that_when_object_is_null() {
		BaseIndirectObservableValue<SimpleObjectProperty<String>, String> indirectObservableValue = BaseIndirectObservableValue.create(null, "not null");

		assertEquals("not null", indirectObservableValue.getValue());
	}
	
	@Test
	public void setUnderlyingObject_with_null_value_is_null() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		indirectObservableValue.setUnderlyingObject(null);
		
		assertNull(indirectObservableValue.getValue());
	}
	
	@Test
	public void setUnderlyingObject_switches_underlying_object() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
		
		indirectObservableValue.setUnderlyingObject(property2);
		
		assertEquals(property2, indirectObservableValue.getUnderlyingObject());
	}
	
	@Test
	public void if_the_underlying_objects_value_is_changed_an_invalidation_event_is_fired() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		InvalidationListener listener = mock(InvalidationListener.class);
		indirectObservableValue.addListener(listener);
		
		property.setValue("b");
		
		verify(listener).invalidated(indirectObservableValue);
	}
	
	@Test
	public void setUnderlyingObject_fires_invalidation_events() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		InvalidationListener listener = mock(InvalidationListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(property2);
		
		verify(listener).invalidated(indirectObservableValue);
	}
	
	@Test
	public void setUnderlyingObject_switches_which_object_fires_invalidation_events() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
		indirectObservableValue.setUnderlyingObject(property2);
		InvalidationListener listener = mock(InvalidationListener.class);
		indirectObservableValue.addListener(listener);
		
		property.setValue("This should not fire an event since property2 is the underlying object");		
		verifyZeroInteractions(listener);
		
		property2.setValue("c");
		verify(listener).invalidated(indirectObservableValue);
	}
	
	@Test
	public void if_the_underlying_objects_value_is_changed_a_change_event_fires() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);

		property.setValue("b");
		
		verify(listener).changed(indirectObservableValue, "a", "b");
	}
	
	@Test
	public void setUnderlyingObject_fires_change_events() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(property2);
		
		verify(listener).changed(indirectObservableValue, "a", "b");
	}
	
	@Test
	public void setUnderlyingObject_with_null_fires_change_events() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
	
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(null);
		
		verify(listener).changed(indirectObservableValue, "a", null);
	}
	
	@Test
	public void setUnderlyingObject_with_null_fires_change_events_if_valueWhenUnderlyingObjectIsNull_is_set_its_value_is_used() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property, "value when null");
	
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(null);
		
		verify(listener).changed(indirectObservableValue, "a", "value when null");
	}
	
	@Test
	public void setUnderlyingObject_initiall_null_fires_change_events() {
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(null);
	
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(property2);
		
		verify(listener).changed(indirectObservableValue, null, "b");
	}
	
	@Test
	public void setUnderlyingObject_initiall_null_fires_change_events_if_valueWhenUnderlyingObjectIsNull_is_set_its_value_is_used() {
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(null, "value when null");
	
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		indirectObservableValue.setUnderlyingObject(property2);
		
		verify(listener).changed(indirectObservableValue, "value when null", "b");
	}
	
	@Test
	public void setUnderlyingObject_switches_which_object_fires_change_events() {
		SimpleObjectProperty<String> property = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> property2 = new SimpleObjectProperty<String>("b");
		BaseIndirectObservableValue<SimpleObjectProperty<String>,String> indirectObservableValue = BaseIndirectObservableValue.create(property);
		indirectObservableValue.setUnderlyingObject(property2);
		@SuppressWarnings("unchecked")
		ChangeListener<String> listener = mock(ChangeListener.class);
		indirectObservableValue.addListener(listener);
		
		property.setValue("This should not fire an event since property2 is the underlying object");		
		verifyZeroInteractions(listener);
		
		property2.setValue("c");		
		verify(listener).changed(indirectObservableValue, "b", "c");
	}
}
