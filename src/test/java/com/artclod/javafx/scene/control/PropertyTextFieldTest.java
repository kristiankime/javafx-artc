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
package com.artclod.javafx.scene.control;

import static junit.framework.Assert.assertEquals;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import org.junit.Test;

import com.artclod.javafx.scene.control.PropertyTextField;

public class PropertyTextFieldTest {

	@Test(expected = NullPointerException.class)
	public void constructor_throws_if_property_is_null() throws Exception {
		new PropertyTextField<>(null, new IntegerStringConverter());
	}

	@Test(expected = NullPointerException.class)
	public void constructor_throws_if_converter_is_null() throws Exception {
		new PropertyTextField<>(new SimpleObjectProperty<Integer>(), (StringConverter<Integer>) null);
	}

	@Test
	public void constructor_text_is_initially_set_to_the_converters_toString_of_the_valueProperty() throws Exception {
		PropertyTextField<Integer, SimpleObjectProperty<Integer>> field = new PropertyTextField<>(new SimpleObjectProperty<Integer>(1010), new IntegerStringConverter());

		assertEquals(1010, field.valueProperty().getValue().intValue());
	}

	@Test
	public void setting_text_does_not_change_value_immediately() throws Exception {
		PropertyTextField<Integer, SimpleObjectProperty<Integer>> field = new PropertyTextField<>(new SimpleObjectProperty<Integer>(1010), new IntegerStringConverter());

		field.textProperty().setValue("1111");

		assertEquals(1010, field.valueProperty().getValue().intValue());
		assertEquals("1111", field.textProperty().getValue());
	}

	@Test
	public void after_an_updateValue_is_triggered_the_valueProperty_is_updated_if_the_text_can_be_parsed() throws Exception {
		PropertyTextField<Integer, SimpleObjectProperty<Integer>> field = new PropertyTextField<>(new SimpleObjectProperty<Integer>(1010), new IntegerStringConverter());

		field.textProperty().setValue("1111");
		field.updateValue();

		assertEquals(1111, field.valueProperty().getValue().intValue());
		assertEquals("1111", field.textProperty().getValue());
	}

	@Test
	public void after_an_updateValue_is_triggered_the_textProperty_is_reverted_if_the_new_text_cant_be_parsed() throws Exception {
		PropertyTextField<Integer, SimpleObjectProperty<Integer>> field = new PropertyTextField<>(new SimpleObjectProperty<Integer>(1010), new IntegerStringConverter());

		field.textProperty().setValue("this can't be parsed to an integer");
		field.updateValue();

		assertEquals(1010, field.valueProperty().getValue().intValue());
		assertEquals("1010", field.textProperty().getValue());
	}

	@Test
	public void setting_the_valueProperty_immediately_changes_the_textProperty() throws Exception {
		PropertyTextField<Integer, SimpleObjectProperty<Integer>> field = new PropertyTextField<>(new SimpleObjectProperty<Integer>(1010), new IntegerStringConverter());

		field.valueProperty().setValue(10);

		assertEquals(10, field.valueProperty().getValue().intValue());
		assertEquals("10", field.textProperty().getValue());
	}

}
