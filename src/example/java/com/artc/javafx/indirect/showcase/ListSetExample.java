package com.artc.javafx.indirect.showcase;

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

import com.artc.javafx.indirect.bean.IndirectBean;
import com.artc.javafx.indirect.bean.property.IndirectProperty;
import com.artc.javafx.indirect.collection.IndirectObservableList;

public class ListSetExample extends Application {
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

		// Indirects
		final IndirectBean<ListBean> indirectBean = IndirectBean.create(one);
		final IndirectObservableList<String> indirectList = indirectBean.getIndirect(new IndirectObservableList<String>(), ListBean.GET_LIST_PROPERTY);
		IndirectProperty<String> indirectPropertyA = indirectBean.getIndirectProperty(ListBean.GET_A_PROPERTY);
		IndirectProperty<String> indirectPropertyB = indirectBean.getIndirectProperty(ListBean.GET_B_PROPERTY);
		
		// Components
		TextField aField = new TextField();
		aField.textProperty().bindBidirectional(indirectPropertyA);
		TextField bField = new TextField();
		bField.textProperty().bindBidirectional(indirectPropertyB);
		ListView<String> listView = new ListView<String>(indirectList);
		final TextField newItemField = new TextField();
		
		// Action buttons
		Button useBeanOne = new Button("use bean one");
		useBeanOne.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectBean.setBean(one);
			}
		});
		Button useBeanTwo = new Button("use bean two");
		useBeanTwo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectBean.setBean(two);
			}
		});
		Button addNewItem = new Button("add item");
		addNewItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectList.add(newItemField.getText());
			}
		});
		Button setItemTwo = new Button("set item 2");
		setItemTwo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectList.set(1, "SET");
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
		surface.add(setItemTwo);
		return surface;
	}

}
