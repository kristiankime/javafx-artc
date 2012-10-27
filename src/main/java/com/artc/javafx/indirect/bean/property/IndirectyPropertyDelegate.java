package com.artc.javafx.indirect.bean.property;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class IndirectyPropertyDelegate<T> implements IndirectProperty<T> {
	private final Property<T> delegate;
	private Property<T> underlyingObject;
	private ObservableValue<? extends T> unidirectionBinding = null;
	
	public static <T> IndirectyPropertyDelegate<T> create() {
		return new IndirectyPropertyDelegate<T>();
	}
	
	public static <T> IndirectyPropertyDelegate<T> create(Property<T> underlyingProperty) {
		IndirectyPropertyDelegate<T> ret = new IndirectyPropertyDelegate<T>();
		ret.setUnderlyingObject(underlyingProperty);
		return ret;
	}
	
	public IndirectyPropertyDelegate() {
		this.delegate = new SimpleObjectProperty<T>();
	}
	
	@Override
	public void setUnderlyingObject(Property<T> newUnderlyingObject) {
		// Unbind everything in preparation for the change
		if (underlyingObject != null) {
			delegate.unbindBidirectional(underlyingObject);
		}
		delegate.unbind();

		// Make the change
		underlyingObject = newUnderlyingObject;
		
		if (underlyingObject != null) {
			// Rebind if the new objects exists
			delegate.bindBidirectional(underlyingObject);
			if (unidirectionBinding != null) {
				delegate.bind(unidirectionBinding);
			}
		} else {
			// The value is fixed at null if the underlyingObject is null
			delegate.setValue(null);
		}
	}
	
	@Override
	public Property<T> getUnderlyingObject() {
		return underlyingObject;
	}
	
	@Override
	public Object getBean() {
		return underlyingObject != null ? underlyingObject.getBean() : null;
	}
	
	@Override
	public String getName() {
		return underlyingObject != null ? underlyingObject.getName() : null;
	}
	
	@Override
	public void bind(ObservableValue<? extends T> observable) {
		if (underlyingObject != null) {
			delegate.bind(observable);
		}
		unidirectionBinding = observable;
	}
	
	@Override
	public void unbind() {
		delegate.unbind();
		unidirectionBinding = null;
	}
	
	@Override
	public void setValue(T value) {
		// The value is fixed at null if the underlyingObject is null
		if (underlyingObject != null) {
			delegate.setValue(value);
		}
	}
	
	// ================ below here are delegate calls ================
	@Override
	public void addListener(ChangeListener<? super T> listener) {
		delegate.addListener(listener);
	}
	
	@Override
	public void addListener(InvalidationListener listener) {
		delegate.addListener(listener);
	}
	
	@Override
	public void bindBidirectional(Property<T> other) {
		delegate.bindBidirectional(other);
	}
	
	@Override
	public T getValue() {
		return delegate.getValue();
	}
	
	@Override
	public boolean isBound() {
		return delegate.isBound();
	}
	
	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		delegate.removeListener(listener);
	}
	
	@Override
	public void removeListener(InvalidationListener listener) {
		delegate.removeListener(listener);
	}
	
	@Override
	public void unbindBidirectional(Property<T> other) {
		delegate.unbindBidirectional(other);
	}
}
