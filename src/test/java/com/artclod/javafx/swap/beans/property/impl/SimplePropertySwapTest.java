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

package com.artclod.javafx.swap.beans.property.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import javafx.beans.property.SimpleObjectProperty;

import org.junit.Test;

import com.artclod.javafx.swap.beans.property.PropertySwap;
import com.artclod.javafx.swap.beans.property.impl.SimplePropertySwap;

public class SimplePropertySwapTest {
	@Test
	public void constructor_with_null_everything_returns_null() {
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(null);

		assertNull(propertySwap.getBean());
		assertNull(propertySwap.getValue());
		assertNull(propertySwap.getName());
		assertNull(propertySwap.getRefObject());
	}

	@Test
		public void swapRefObject_starting_with_nothing_switches_which_object_is_bound_unidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(null);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			propertySwap.bind(bindProperty);
			assertEquals("b", underlyingProperty.get());
	
			propertySwap.swapRefObject(underlyingProperty);
			bindProperty.set("d");
			assertEquals("d", underlyingProperty.get());
		}

	@Test
		public void swapRefObject_starting_with_nothing_switches_which_object_is_bound_bidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(null);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			propertySwap.bindBidirectional(bindProperty);
			assertEquals("b", underlyingProperty.get());
	
			propertySwap.swapRefObject(underlyingProperty);
			bindProperty.set("d");
			assertEquals("d", underlyingProperty.get());
		}

	@Test
		public void swapRefObject_switches_which_object_is_bound_unidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			propertySwap.bind(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
	
			propertySwap.swapRefObject(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
		}

	@Test
		public void swapRefObject_switches_which_object_is_bound_bidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			propertySwap.bindBidirectional(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
	
			propertySwap.swapRefObject(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
		}

	@Test
	public void unbind_works() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);

		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		propertySwap.bind(bindProperty);
		assertEquals("c", underlyingProperty.get());

		propertySwap.unbind();
		bindProperty.set("d");
		assertEquals("d", bindProperty.get());
		assertEquals("c", propertySwap.getValue());
	}

	@Test
	public void isBound_works() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);

		assertFalse(propertySwap.isBound());

		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		propertySwap.bind(bindProperty);

		assertTrue(propertySwap.isBound());
	}

	@Test
	public void setValue_with_null_underlying_object_has_no_effect() {
		PropertySwap<String> propertySwap = SimplePropertySwap.create(null);
		propertySwap.setValue("a");

		assertNull(propertySwap.getValue());
	}

	@Test
		public void swapRefObject_starting_with_null_switches_which_object_binds() {
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
			PropertySwap<String> propertySwap = SimplePropertySwap.create(null);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			bindProperty.bind(propertySwap);
			propertySwap.setValue("a");
			assertNull(bindProperty.get());
	
			propertySwap.swapRefObject(underlyingProperty2);
			propertySwap.setValue("b");
			assertEquals("b", bindProperty.get());
		}

	@Test
		public void swapRefObject_switches_which_object_binds() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>();
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			bindProperty.bind(propertySwap);
			propertySwap.setValue("a");
			assertEquals("a", bindProperty.get());
	
			propertySwap.swapRefObject(underlyingProperty2);
			propertySwap.setValue("b");
			assertEquals("b", bindProperty.get());
		}

	@Test
		public void swapRefObject_switches_which_object_is_bidirectionallyBound() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(underlyingProperty);
	
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			propertySwap.bindBidirectional(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
	
			propertySwap.swapRefObject(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
	
			propertySwap.setValue("e");
			assertEquals("e", bindProperty.get());
			assertEquals("c", underlyingProperty.get());
			assertEquals("e", underlyingProperty2.get());
		}

	@Test
	public void getName_with_null_returns_null() {
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(null);
		assertNull(propertySwap.getName());
	}

	@Test
	public void getName_with_underlyingObject_returns_its_name() {
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(new Object(), "name");
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(objectProperty);

		assertEquals("name", propertySwap.getName());
	}

	@Test
	public void getBean_with_null_returns_null() {
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(null);
		assertNull(propertySwap.getBean());
	}

	@Test
	public void getBean_with_underlyingObject_returns_its_bean() {
		Object bean = new Object();
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(bean, "name");
		SimplePropertySwap<String> propertySwap = SimplePropertySwap.create(objectProperty);

		assertEquals(bean, propertySwap.getBean());
	}
}
