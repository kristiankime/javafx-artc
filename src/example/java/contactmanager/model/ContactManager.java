package contactmanager.model;

import javafx.collections.ObservableList;

import com.artc.javafx.collections.BeanObservableList;

public class ContactManager {
	private final ObservableList<Contact> contacts;

	public ContactManager(Contact... contacts) {
		this.contacts = BeanObservableList.create(contacts, Contact.FIRST_NAME, Contact.LAST_NAME);
	}

	public Contact createNewContact() {
		Contact contact = new Contact();
		contacts.add(contact);
		return contact;
	}

	public ObservableList<Contact> getContacts() {
		return contacts;
	}

	public void removeContact(Contact contact) {
		contacts.remove(contact);
	}
}
