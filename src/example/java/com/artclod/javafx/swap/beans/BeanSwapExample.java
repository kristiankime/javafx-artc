package com.artclod.javafx.swap.beans;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.tbee.javafx.scene.layout.MigPane;

import com.artclod.javafx.swap.beans.impl.SimpleBeanSwap;

public class BeanSwapExample extends Application {
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
		final StringBean one = new StringBean("one a", "one b");
		final StringBean two = new StringBean("two a", "two b");
		final SimpleBeanSwap<StringBean> beanSwap = new SimpleBeanSwap<StringBean>(one);
		
		TextField aField = new TextField();
		aField.textProperty().bindBidirectional(beanSwap.createPropertyRef(StringBean.GET_A_PROPERTY));
		TextField bField = new TextField();
		bField.textProperty().bindBidirectional(beanSwap.createPropertyRef(StringBean.GET_B_PROPERTY));
		
		Button useBeanOne = new Button("use one");
		useBeanOne.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				beanSwap.setBean(one);
			}
		});
		
		Button useBeanTwo = new Button("use two");
		useBeanTwo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				beanSwap.setBean(two);
			}
		});
		
		MigPane surface = new MigPane("fill, wrap 2");
		surface.add(new Label("a :"));
		surface.add(aField);
		surface.add(new Label("b :"));
		surface.add(bField);
		surface.add(useBeanOne);
		surface.add(useBeanTwo);

		return surface;
	}

}
