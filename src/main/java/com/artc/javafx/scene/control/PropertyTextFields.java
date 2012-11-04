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

import java.math.BigDecimal;
import java.math.BigInteger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import com.artc.javafx.scene.control.PropertyTextField.ValueConverter;

public class PropertyTextFields {	

	public static class BigIntegerField extends PropertyTextField<BigInteger, Property<BigInteger>> {
		public BigIntegerField() {
			super(new SimpleObjectProperty<BigInteger>(), new BigIntegerStringConverter());
		}
	}
	
	public static class BigDecimalField extends PropertyTextField<BigDecimal, Property<BigDecimal>> {
		public BigDecimalField() {
			super(new SimpleObjectProperty<BigDecimal>(), new BigDecimalStringConverter());
		}
	}
	
	public static class BooleanPropertyField extends PropertyTextField<Boolean, BooleanProperty> {
		public BooleanPropertyField() {
			super(new SimpleBooleanProperty(), new BooleanStringConverter());
		}
	}
	
	public static class IntegerField extends PropertyTextField<Integer, Property<Integer>> {
		public IntegerField() {
			super(new SimpleObjectProperty<Integer>(), new IntegerStringConverter());
		}
	}

	public static class IntegerPropertyField extends PropertyTextField<Number, IntegerProperty> {
		public IntegerPropertyField() {
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
	
	public static class LongPropertyField extends PropertyTextField<Number, LongProperty> {
		public LongPropertyField() {
			super(new SimpleLongProperty(), new NumberConverter<Long>(new LongStringConverter()){
				@Override 
				protected Long fromNumber(Number type) {
					return type.longValue();
				}
			});
		}
	}
	
	public static class FloatField extends PropertyTextField<Float, Property<Float>> {
		public FloatField() {
			super(new SimpleObjectProperty<Float>(), new FloatStringConverter());
		}
	}
	
	public static class FloatPropertyField extends PropertyTextField<Number, FloatProperty> {
		public FloatPropertyField() {
			super(new SimpleFloatProperty(), new NumberConverter<Float>(new FloatStringConverter()){
				@Override 
				protected Float fromNumber(Number type) {
					return type.floatValue();
				}
			});
		}
	}
	
	public static class DoubleField extends PropertyTextField<Double, Property<Double>> {
		public DoubleField() {
			super(new SimpleObjectProperty<Double>(), new DoubleStringConverter());
		}
	}
	
	public static class DoublePropertyField extends PropertyTextField<Number, DoubleProperty> {
		public DoublePropertyField() {
			super(new SimpleDoubleProperty(), new NumberConverter<Double>(new DoubleStringConverter()){
				@Override 
				protected Double fromNumber(Number type) {
					return type.doubleValue();
				}
			});
		}
	}
	
	// ============== Adapter for special numeric properties =============
	private abstract static class NumberConverter<T extends Number> implements ValueConverter<Number> {
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
