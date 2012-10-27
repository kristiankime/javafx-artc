package com.artc.javafx.indirect.bean.property;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import javafx.beans.property.SimpleObjectProperty;

import org.junit.Test;

public class IndirectyPropertyDelegateTest {
	@Test
	public void constructor_with_null_everything_returns_null() {
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		
		assertNull(indirectObservableValue.getBean());
		assertNull(indirectObservableValue.getValue());
		assertNull(indirectObservableValue.getName());
		assertNull(indirectObservableValue.getUnderlyingObject());
	}
	
	@Test
	public void setUnderlyingObject_starting_with_nothing_switches_which_object_is_bound_unidirectionally() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bind(bindProperty);
		assertEquals("b", underlyingProperty.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty);
		bindProperty.set("d");
		assertEquals("d", underlyingProperty.get());
	}
	
	@Test
	public void setUnderlyingObject_starting_with_nothing_switches_which_object_is_bound_bidirectionally() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("b");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bindBidirectional(bindProperty);
		assertEquals("b", underlyingProperty.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty);
		bindProperty.set("d");
		assertEquals("d", underlyingProperty.get());
	}

	@Test
	public void setUnderlyingObject_switches_which_object_is_bound_unidirectionally() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bind(bindProperty);
		assertEquals("c", underlyingProperty.get());
		assertEquals("b", underlyingProperty2.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty2);
		bindProperty.set("d");
		assertEquals("c", underlyingProperty.get());
		assertEquals("d", underlyingProperty2.get());
	}
	
	@Test
	public void setUnderlyingObject_switches_which_object_is_bound_bidirectionally() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bindBidirectional(bindProperty);
		assertEquals("c", underlyingProperty.get());
		assertEquals("b", underlyingProperty2.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty2);
		bindProperty.set("d");
		assertEquals("c", underlyingProperty.get());
		assertEquals("d", underlyingProperty2.get());
	}
	
	@Test
	public void unbind_works() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
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
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
		assertFalse(indirectObservableValue.isBound());
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bind(bindProperty);
		
		assertTrue(indirectObservableValue.isBound());
	}
	
	@Test
	public void setValue_with_null_underlying_object_has_no_effect() {
		IndirectProperty<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		indirectObservableValue.setValue("a");
		
		assertNull(indirectObservableValue.getValue());
	}
	
	@Test
	public void setUnderlyingObject_starting_with_null_switches_which_object_binds() {
		SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
		IndirectProperty<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		bindProperty.bind(indirectObservableValue);
		indirectObservableValue.setValue("a");
		assertNull(bindProperty.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty2);
		indirectObservableValue.setValue("b");
		assertEquals("b", bindProperty.get());
	}
	
	@Test
	public void setUnderlyingObject_switches_which_object_binds() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>();
		SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>();
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		bindProperty.bind(indirectObservableValue);
		indirectObservableValue.setValue("a");
		assertEquals("a", bindProperty.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty2);
		indirectObservableValue.setValue("b");
		assertEquals("b", bindProperty.get());
	}
	
	@Test
	public void setUnderlyingObject_switches_which_object_is_bidirectionallyBound() {
		SimpleObjectProperty<String> underlyingProperty = new SimpleObjectProperty<String>("a");
		SimpleObjectProperty<String> underlyingProperty2 = new SimpleObjectProperty<String>("b");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(underlyingProperty);
		
		SimpleObjectProperty<String> bindProperty = new SimpleObjectProperty<String>("c");
		indirectObservableValue.bindBidirectional(bindProperty);
		assertEquals("c", underlyingProperty.get());
		assertEquals("b", underlyingProperty2.get());
		
		indirectObservableValue.setUnderlyingObject(underlyingProperty2);
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
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		assertNull(indirectObservableValue.getName());
	}
	
	@Test
	public void getName_with_underlyingObject_returns_its_name() {
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(new Object(), "name");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(objectProperty);
		
		assertEquals("name", indirectObservableValue.getName());
	}
	
	@Test
	public void getBean_with_null_returns_null() {
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(null);
		assertNull(indirectObservableValue.getBean());
	}
	
	@Test
	public void getBean_with_underlyingObject_returns_its_bean() {
		Object bean = new Object();
		SimpleObjectProperty<String> objectProperty = new SimpleObjectProperty<String>(bean, "name");
		IndirectyPropertyDelegate<String> indirectObservableValue = IndirectyPropertyDelegate.create(objectProperty);
		
		assertEquals(bean, indirectObservableValue.getBean());
	}
}
