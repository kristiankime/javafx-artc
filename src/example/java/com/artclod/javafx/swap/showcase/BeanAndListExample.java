package com.artclod.javafx.swap.showcase;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.tbee.javafx.scene.layout.MigPane;

import com.artclod.javafx.swap.beans.impl.SimpleBeanSwap;
import com.artclod.javafx.swap.beans.property.PropertyRef;
import com.artclod.javafx.swap.collections.ObservableListSwap;
import com.artclod.javafx.swap.collections.impl.ArrayObservableListSwap;

public class BeanAndListExample extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent applicationView = createApplication();
		
		Scene scene = new Scene(applicationView, 1200, 600);
		stage.setScene(scene);
		stage.show();
	}
	
	public Parent createApplication() {
		// Data
		final ListBean one = new ListBean("one a", "one b", "1", "2");
		final ListBean two = new ListBean("two a", "two b", "a", "b", "c");

		// Swaps
		final SimpleBeanSwap<ListBean> beanSwap = SimpleBeanSwap.create(one);
		final ObservableListSwap<String> listSwap = beanSwap.attachSwap(new ArrayObservableListSwap<String>(), ListBean.GET_LIST_PROPERTY);
		PropertyRef<String> propertyASwap = beanSwap.propertyFrom(ListBean.GET_A_PROPERTY);
		PropertyRef<String> propertyBSwap = beanSwap.propertyFrom(ListBean.GET_B_PROPERTY);
		
		// Components
		TextField aField = new TextField();
		aField.textProperty().bindBidirectional(propertyASwap);
		TextField bField = new TextField();
		bField.textProperty().bindBidirectional(propertyBSwap);
		ListView<String> listView = new ListView<String>(listSwap);
		final TextField newItemField = new TextField();
		
		// Action buttons
		Button useBeanOne = new Button("use bean one");
		useBeanOne.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				beanSwap.setBean(one);
			}
		});
		Button useBeanTwo = new Button("use bean two");
		useBeanTwo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				beanSwap.setBean(two);
			}
		});
		Button addNewItem = new Button("add item");
		addNewItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				listSwap.add(newItemField.getText());
			}
		});

		MigPane surface = new MigPane("fill, wrap 2");
		surface.add(new Label("Bean Data"), "spanx 2, center");
		surface.add(aField);
		surface.add(bField);
		surface.add(listView, "spanx 2, growx");
		surface.add(useBeanOne);
		surface.add(useBeanTwo);
		surface.add(newItemField);
		surface.add(addNewItem);
		return surface;
	}

}
