

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.tbee.javafx.scene.layout.MigPane;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent applicationView = createApplication();
		
		Scene scene = new Scene(applicationView, 800, 400);
		stage.setScene(scene);
		stage.show();
	}
	
	public Parent createApplication() {
		MigPane applicationView = new MigPane("fill");
		applicationView.add(new Label("test"), "grow");
		return applicationView;
	}
}
