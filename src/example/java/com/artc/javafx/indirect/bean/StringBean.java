package com.artc.javafx.indirect.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.artc.javafx.indirect.bean.getter.StringPropertyGetter;

public class StringBean {
	public static final StringPropertyGetter<StringBean> GET_A_PROPERTY = new StringPropertyGetter<StringBean>() {
		@Override
		public StringProperty get(StringBean bean) {
			return bean.aProperty;
		}
	};
	public static final StringPropertyGetter<StringBean> GET_B_PROPERTY = new StringPropertyGetter<StringBean>() {
		@Override
		public StringProperty get(StringBean bean) {
			return bean.bProperty;
		}
	};
	
	private final SimpleStringProperty aProperty;
	private final SimpleStringProperty bProperty;
	
	public StringBean(String a, String b) {
		this.aProperty = new SimpleStringProperty(a);
		this.bProperty = new SimpleStringProperty(b);
	}
	
	public SimpleStringProperty getAProperty() {
		return aProperty;
	}
	
	public SimpleStringProperty getBProperty() {
		return bProperty;
	}
}