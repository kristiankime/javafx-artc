package contactmanager.model;

import javafx.collections.ObservableList;

import com.artc.javafx.indirect.collection.BeanObservableList;

public class ContactManager {
	private final ObservableList<Contact> contacts;
	
	public ContactManager(Contact... contacts) {
		this.contacts = BeanObservableList.create(Contact.FIRST_NAME, Contact.LAST_NAME);
		this.contacts.addAll(contacts);
		if(this.contacts.size() < 1){
			throw new IllegalArgumentException("Must have at least one contact");
		}
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
		if (contacts.size() > 1) {
			contacts.remove(contact);
		}
	}
}
