package contactmanager.presentation;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MultipleSelectionModel;

import com.artclod.javafx.indirect.beans.UncontrolledIndirectBean;
import com.artclod.javafx.indirect.beans.property.IndirectProperty;
import com.artclod.javafx.indirect.collections.IndirectObservableListAndSelection;
import com.artclod.javafx.indirect.collections.IndirectObservableListAndSelectionDelegate;

import contactmanager.model.Contact;
import contactmanager.model.ContactManager;

public class ContactManagerPresentation {
	private final ContactManager contactManager;
	
	private final IndirectObservableListAndSelection<Contact> contactSelection;
	private final UncontrolledIndirectBean<Contact> contact;
	private final IndirectProperty<String> firstName;
	private final IndirectProperty<String> lastName;
	private final IndirectProperty<Boolean> fictional;
	
	private final EventHandler<ActionEvent> add;
	private final EventHandler<ActionEvent> remove;
	
	public ContactManagerPresentation(ContactManager contactManager) {
		this.contactManager = contactManager;
		
		this.contactSelection = IndirectObservableListAndSelectionDelegate.<Contact> create(contactManager.getContacts());
		this.contactSelection.selectionModel().select(0);
		
		this.contact = new UncontrolledIndirectBean<Contact>(contactSelection.selectionModel().selectedItemProperty());
		this.firstName = contact.getIndirectProperty(Contact.FIRST_NAME);
		this.lastName = contact.getIndirectProperty(Contact.LAST_NAME);
		this.fictional = contact.getIndirectProperty(Contact.FICTIONAL);
		
		this.add = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				add();
			}
		};
		
		this.remove = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				remove();
			}
		};
	}
	
	private void remove() {
		Contact selectedItem = contactSelection.selectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		contactManager.removeContact(selectedItem);
	}
	
	private void add() {
		Contact newContact = contactManager.createNewContact();
		contactSelection.selectionModel().select(newContact);
	}
	
	public EventHandler<ActionEvent> getAdd() {
		return add;
	}
	
	public EventHandler<ActionEvent> getRemove() {
		return remove;
	}
	
	public Property<String> getLastName() {
		return lastName;
	}
	
	public Property<String> getFirstName() {
		return firstName;
	}
	
	public Property<Boolean> getFictional() {
		return fictional;
	}
	
	public ObservableList<Contact> getContactList() {
		return contactSelection;
	}
	
	public MultipleSelectionModel<Contact> getSelectedContact() {
		return contactSelection.selectionModel();
	}
}
