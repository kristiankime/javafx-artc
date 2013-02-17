package contactmanager.presentation;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MultipleSelectionModel;

import com.artclod.javafx.swap.beans.impl.SimpleBeanRef;
import com.artclod.javafx.swap.beans.property.PropertyRef;
import com.artclod.javafx.swap.collections.ObservableListAndSelectionSwap;
import com.artclod.javafx.swap.collections.impl.ArrayObservableListAndSelectionSwap;

import contactmanager.model.Contact;
import contactmanager.model.ContactManager;

public class ContactManagerPresentation {
	private final ContactManager contactManager;
	
	private final ObservableListAndSelectionSwap<Contact> contactSelection;
	private final SimpleBeanRef<Contact> contact;
	private final PropertyRef<String> firstName;
	private final PropertyRef<String> lastName;
	private final PropertyRef<Boolean> fictional;
	
	private final EventHandler<ActionEvent> add;
	private final EventHandler<ActionEvent> remove;
	
	public ContactManagerPresentation(ContactManager contactManager) {
		this.contactManager = contactManager;
		
		this.contactSelection = ArrayObservableListAndSelectionSwap.<Contact> create(contactManager.getContacts());
		this.contactSelection.selectionModel().select(0);
		
		this.contact = new SimpleBeanRef<Contact>(contactSelection.selectionModel().selectedItemProperty());
		this.firstName = contact.getProperty(Contact.FIRST_NAME);
		this.lastName = contact.getProperty(Contact.LAST_NAME);
		this.fictional = contact.getProperty(Contact.FICTIONAL);
		
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
