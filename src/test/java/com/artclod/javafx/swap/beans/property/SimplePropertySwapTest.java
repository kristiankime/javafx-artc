package com.artclod.javafx.swap.beans.property;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import javafx.beans.property.SimpleObjectProperty;

import org.junit.Test;

import com.artclod.javafx.swap.beans.property.PropertySwap;
import com.artclod.javafx.swap.beans.property.SimplePropertySwap;

public class SimplePropertySwapTest {
	@Test
	public void constructor_with_null_everything_returns_null() {
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
		
		assertNull(indirectObservableValue.getBean());
		assertNull(indirectObservableValue.getValue());
		assertNull(indirectObservableValue.getName());
		assertNull(indirectObservableValue.getSwap());
	}
	
	@Test
		public void swap_starting_with_nothing_switches_which_object_is_bound_unidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			indirectObservableValue.bind(bindProperty);
			assertEquals("b", underlyingProperty.get());
			
			indirectObservableValue.swap(underlyingProperty);
			bindProperty.set("d");
			assertEquals("d", underlyingProperty.get());
		}
	
	@Test
		public void swap_starting_with_nothing_switches_which_object_is_bound_bidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			indirectObservableValue.bindBidirectional(bindProperty);
			assertEquals("b", underlyingProperty.get());
			
			indirectObservableValue.swap(underlyingProperty);
			bindProperty.set("d");
			assertEquals("d", underlyingProperty.get());
		}

	@Test
		public void swap_switches_which_object_is_bound_unidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			indirectObservableValue.bind(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
			
			indirectObservableValue.swap(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
		}
	
	@Test
		public void swap_switches_which_object_is_bound_bidirectionally() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			indirectObservableValue.bindBidirectional(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
			
			indirectObservableValue.swap(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
		}
	
	@Test
	public void unbind_works() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bind(bindProperty);
		assertEquals("c", underlyingProperty.get());
		
		indirectObservableValue.unbind();
		bindProperty.set("d");
		assertEquals("d", bindProperty.get());
		assertEquals("c", indirectObservableValue.getValue());
	}
	
	@Test
	public void isBound_works() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
		
		assertFalse(indirectObservableValue.isBound());
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bind(bindProperty);
		
		assertTrue(indirectObservableValue.isBound());
	}
	
	@Test
	public void setValue_with_null_underlying_object_has_no_effect() {
		PropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
		indirectObservableValue.setValue("a");
		
		assertNull(indirectObservableValue.getValue());
	}
	
	@Test
		public void swap_starting_with_null_switches_which_object_binds() {
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
			PropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			bindProperty.bind(indirectObservableValue);
			indirectObservableValue.setValue("a");
			assertNull(bindProperty.get());
			
			indirectObservableValue.swap(underlyingProperty2);
			indirectObservableValue.setValue("b");
			assertEquals("b", bindProperty.get());
		}
	
	@Test
		public void swap_switches_which_object_binds() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>();
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			bindProperty.bind(indirectObservableValue);
			indirectObservableValue.setValue("a");
			assertEquals("a", bindProperty.get());
			
			indirectObservableValue.swap(underlyingProperty2);
			indirectObservableValue.setValue("b");
			assertEquals("b", bindProperty.get());
		}
	
	@Test
		public void swap_switches_which_object_is_bidirectionallyBound() {
			SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
			SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
			SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(underlyingProperty);
			
			SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
			indirectObservableValue.bindBidirectional(bindProperty);
			assertEquals("c", underlyingProperty.get());
			assertEquals("b", underlyingProperty2.get());
			
			indirectObservableValue.swap(underlyingProperty2);
			bindProperty.set("d");
			assertEquals("c", underlyingProperty.get());
			assertEquals("d", underlyingProperty2.get());
			
			indirectObservableValue.setValue("e");
			assertEquals("e", bindProperty.get());
			assertEquals("c", underlyingProperty.get());
			assertEquals("e", underlyingProperty2.get());
		}
	
	@Test
	public void getName_with_null_returns_null() {
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
		assertNull(indirectObservableValue.getName());
	}
	
	@Test
	public void getName_with_underlyingObject_returns_its_name() {
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(new Object(), "name");
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(objectProperty);
		
		assertEquals("name", indirectObservableValue.getName());
	}
	
	@Test
	public void getBean_with_null_returns_null() {
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(null);
		assertNull(indirectObservableValue.getBean());
	}
	
	@Test
	public void getBean_with_underlyingObject_returns_its_bean() {
		Object bean = new Object();
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(bean, "name");
		SimplePropertySwap<String> indirectObservableValue = SimplePropertySwap.create(objectProperty);
		
		assertEquals(bean, indirectObservableValue.getBean());
	}
}
