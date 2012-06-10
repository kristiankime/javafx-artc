package contactmanager.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import org.tbee.javafx.scene.layout.MigPane;

import contactmanager.model.Contact;
import contactmanager.presentation.ContactManagerPresentation;

public class ContactManagerView {
	private final MigPane surface;
	private final ListView<Contact> contactList;
	private final TextField firstName;
	private final TextField lastName;
	private final CheckBox fictional;
	
	private final Button add;
	private final Button remove;
	
	public ContactManagerView(ContactManagerPresentation presenter) {
		contactList = new ListView<Contact>(presenter.getContactList());
		contactList.setSelectionModel(presenter.getSelectedContact());
		firstName = new TextField();
		firstName.textProperty().bindBidirectional(presenter.getFirstName());
		lastName = new TextField();
		lastName.textProperty().bindBidirectional(presenter.getLastName());
		fictional = new CheckBox();
		fictional.selectedProperty().bindBidirectional(presenter.getFictional());
		add = new Button("add");
		add.setOnAction(presenter.getAdd());
		remove = new Button("remove");
		remove.setOnAction(presenter.getRemove());
		
		this.surface = new MigPane("wrap 2");
		buildSurface();
	}
	
	private void buildSurface() {
		surface.add(new Label("Contacts"), "span 2, growx");
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(contactList);
		surface.add(scrollPane, "span 2, grow");
		surface.add(add);
		surface.add(remove);
		
		surface.add(new Label("Detail"), "span 2, growx");
		surface.add(new Label("First Name"));
		surface.add(firstName, "growx");
		surface.add(new Label("Last Name"));
		surface.add(lastName, "growx");
		surface.add(new Label("Type"));
		surface.add(fictional, "growx");
	}
	
	public Node getNode() {
		return surface;
	}
}
