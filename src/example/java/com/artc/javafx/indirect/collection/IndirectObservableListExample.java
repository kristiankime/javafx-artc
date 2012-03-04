package com.artc.javafx.indirect.collection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class IndirectObservableListExample extends Application {
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
		final ObservableList<String> oneTwo = FXCollections.observableArrayList("one", "two");
		final ObservableList<String> abc = FXCollections.observableArrayList("a", "b", "c");
		final IndirectObservableList<String> indirectList = IndirectObservableList.create(oneTwo);
		
		ListView<String> listView = new ListView<String>(indirectList);
		
		Button useListOne = new Button("use one");
		useListOne.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectList.setUnderlyingObject(oneTwo);
			}
		});
		
		Button useListTwo = new Button("use two");
		useListTwo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectList.setUnderlyingObject(abc);
			}
		});
		
		final TextField newItemField = new TextField();
		Button addNewItem = new Button("add item");
		addNewItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				indirectList.add(newItemField.getText());
			}
		});

		
		MigPane surface = new MigPane("fill, wrap 2");
		surface.add(new Label("Items"), "spanx 2");
		surface.add(listView, "spanx 2");
		surface.add(useListOne);
		surface.add(useListTwo);
		surface.add(newItemField);
		surface.add(addNewItem);
		return surface;
	}

}
