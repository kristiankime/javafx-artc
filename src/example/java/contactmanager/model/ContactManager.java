package contactmanager.model;

import static java.util.Arrays.asList;
import javafx.collections.ObservableList;

import com.artclod.javafx.collections.BeanObservableList;

public class ContactManager {
	private final ObservableList<Contact> contacts;

	public ContactManager(Contact... contacts) {
		this.contacts = BeanObservableList.create(asList(contacts), Contact.FIRST_NAME, Contact.LAST_NAME);
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
