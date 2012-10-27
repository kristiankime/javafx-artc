package contactmanager;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.tbee.javafx.scene.layout.MigPane;

import contactmanager.model.Contact;
import contactmanager.model.ContactManager;
import contactmanager.presentation.ContactManagerPresentation;
import contactmanager.view.ContactManagerView;

public class LaunchContactManagerApplication extends Application {
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
		// Model
		ContactManager contactManager = new ContactManager(new Contact());
		
		// Presentation
		ContactManagerPresentation contactManagerPresentation = new ContactManagerPresentation(contactManager);
		
		// View
		ContactManagerView contactManagerView = new ContactManagerView(contactManagerPresentation);
		
		// Framing
		MigPane applicationView = new MigPane("fill");
		applicationView.add(contactManagerView.getNode(), "grow");
		return applicationView;
	}
}
