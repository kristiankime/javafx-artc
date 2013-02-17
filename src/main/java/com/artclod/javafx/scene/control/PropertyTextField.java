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

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * This object adds an additional property (called the valueProperty) to a
 * TextField. This is intended to be the typed "real" value of the TextField. It
 * gets it's value set when the user is done editing (focus lost or the ENTER
 * key is hit). If the text string is parsable at that point the valueProperty
 * is set with the parsed version of the current text otherwise the text is
 * reset back to the last valid value.
 * 
 * Note: this class registers a listener for onKeyReleased to check to see if
 * the ENTER key is pressed. If you override this listener the ENTER key
 * functionality will no longer work.
 * 
 * This implementation is similar to the number formatter idea here but is more
 * general: http://java.dzone.com/articles/javafx-numbertextfield-and
 * 
 * Other ideas about how to handle this are suggested in these posts if this
 * design is not desirable.
 * 
 * Limiting the String to a regex: http://fxexperience.com/2012/02/restricting-input-on-a-textfield/
 * http://fxexperience.com/2012/02/moneyfield/
 * 
 */
public class PropertyTextField<T, P extends Property<T>> extends TextField {
	private final ValueConverter<T> valueConverter;
	private final P valueProperty;

	public PropertyTextField(P valueProperty, StringConverter<T> valueConverter) {
		this(valueProperty, new StringConverterAdapter<T>(valueConverter));
	}
	
	public PropertyTextField(P valueProperty, ValueConverter<T> valueConverter) {
		if(valueConverter == null)
			throw new NullPointerException("valueConverter was null");
		if(valueProperty == null)
			throw new NullPointerException("valueProperty was null");
		this.valueConverter = valueConverter;
		this.valueProperty = valueProperty;
		textProperty().setValue(valueConverter.toString(valueProperty.getValue()));
		// Listeners for focus lost and enter key that update the valueProperty
		onKeyReleasedProperty().setValue(new EnterKeyListener());
		focusedProperty().addListener(new FocusLostListener());
		// if the value updates change the text as well
		valueProperty().addListener(new PropertyChangedListener());
	}

	public P valueProperty() {
		return valueProperty;
	}

	// Visible for testing
	protected void updateValue() {
		String newValue = textProperty().getValue();
		if (valueConverter.canParse(newValue)) {
			valueProperty.setValue(valueConverter.fromString(newValue));
		} else {
			textProperty().setValue(valueConverter.toString(valueProperty.getValue()));
		}
	}

	private void updateText(T newValue) {
		textProperty().setValue(valueConverter.toString(newValue));
	}
	
	// ========================== Listeners ==========================
	private class EnterKeyListener implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent keyEvent) {
			if (keyEvent.getCode().equals(KeyCode.ENTER))
				updateValue();
		}
	}

	private class FocusLostListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (!newValue)
				updateValue();
		}
	}
	
	private class PropertyChangedListener implements ChangeListener<T> {
		@Override
		public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
			updateText(newValue);
		}
	};

	// ========================== External Interfaces / Classes ==========================
	public static interface ValueConverter<T> {
		public abstract boolean canParse(String string);

		public abstract T fromString(String string);

		public abstract String toString(T type);
	}

	public static class StringConverterAdapter<T> implements ValueConverter<T> {
		private final StringConverter<T> stringConverter;

		public StringConverterAdapter(StringConverter<T> stringConverter) {
			if(stringConverter == null)
				throw new NullPointerException("stringConverter was null");
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
		public T fromString(String string) {
			return stringConverter.fromString(string);
		}

		@Override
		public String toString(T type) {
			return stringConverter.toString(type);
		}
	}
}
