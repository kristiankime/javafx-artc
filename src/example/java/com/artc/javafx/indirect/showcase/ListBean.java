package com.artc.javafx.indirect.showcase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.artc.javafx.indirect.beans.getter.Getter;
import com.artc.javafx.indirect.beans.getter.StringPropertyGetter;

public class ListBean {
	public static final StringPropertyGetter<ListBean> GET_A_PROPERTY = new StringPropertyGetter<ListBean>() {
		@Override
		public StringProperty get(ListBean bean) {
			return bean.aProperty;
		}
	};
	public static final StringPropertyGetter<ListBean> GET_B_PROPERTY = new StringPropertyGetter<ListBean>() {
		@Override
		public StringProperty get(ListBean bean) {
			return bean.bProperty;
		}
	};
	public static final Getter<ObservableList<String>, ListBean> GET_LIST_PROPERTY = new Getter<ObservableList<String> ,ListBean>() {
		@Override
		public ObservableList<String> get(ListBean bean) {
			return bean.listProperty;
		}		
	};
	
	private final SimpleStringProperty aProperty;
	private final SimpleStringProperty bProperty;
	private final ObservableList<String> listProperty;
	
	public ListBean(String a, String b, String... listValues) {
		this.aProperty = new SimpleStringProperty(a);
		this.bProperty = new SimpleStringProperty(b);
		this.listProperty = FXCollections.observableArrayList(listValues);
	}
	
	public SimpleStringProperty getAProperty() {
		return aProperty;
	}
	
	public SimpleStringProperty getBProperty() {
		return bProperty;
	}

	public ObservableList<String> getListProperty() {
		return listProperty;
	}
}