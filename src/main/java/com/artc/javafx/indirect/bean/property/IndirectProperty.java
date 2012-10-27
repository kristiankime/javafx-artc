package com.artc.javafx.indirect.bean.property;

import javafx.beans.property.Property;

import com.artc.javafx.indirect.IndirectObject;

public interface IndirectProperty<T> extends Property<T>, IndirectObject<Property<T>> {	
	
}