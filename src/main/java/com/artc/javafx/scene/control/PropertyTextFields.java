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
package com.artc.javafx.scene.control;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import com.artc.javafx.scene.control.PropertyTextField.ValueConverter;

public class PropertyTextFields {
	
	public static class IntegerField extends PropertyTextField<Integer, Property<Integer>> {
		public IntegerField() {
			super(new SimpleObjectProperty<Integer>(), new IntegerStringConverter());
		}
	}

	public static class NumericIntegerField extends PropertyTextField<Number, IntegerProperty> {
		public NumericIntegerField() {
			super(new SimpleIntegerProperty(), new NumberConverter<Integer>(new IntegerStringConverter()){
				@Override 
				protected Integer fromNumber(Number type) {
					return type.intValue();
				}
			});
		}
	}
	
	public static class LongField extends PropertyTextField<Long, Property<Long>> {
		public LongField() {
			super(new SimpleObjectProperty<Long>(), new LongStringConverter());
		}
	}
	
	public static class NumericLongField extends PropertyTextField<Number, LongProperty> {
		public NumericLongField() {
			super(new SimpleLongProperty(), new NumberConverter<Long>(new LongStringConverter()){
				@Override 
				protected Long fromNumber(Number type) {
					return type.longValue();
				}
			});
		}
	}
	
	// ============== Adapter for special numeric properties =============
	protected abstract static class NumberConverter<T extends Number> implements ValueConverter<Number> {
		private final StringConverter<T> stringConverter;
		
		public NumberConverter(StringConverter<T> stringConverter) {
			this.stringConverter = stringConverter;
		}

		@Override
		public boolean canParse(String string) {
			try {
				stringConverter.fromString(string);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		@Override
		public Number fromString(String string) {
			return stringConverter.fromString(string);
		}

		@Override
		public String toString(Number type) {
			return stringConverter.toString(fromNumber(type));
		}
		
		protected abstract T fromNumber(Number type);
	}
}
