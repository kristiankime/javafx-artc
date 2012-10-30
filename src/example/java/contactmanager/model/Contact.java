package contactmanager.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.artc.javafx.indirect.beans.getter.BooleanPropertyGetter;
import com.artc.javafx.indirect.beans.getter.IntegerPropertyGetter;
import com.artc.javafx.indirect.beans.getter.StringPropertyGetter;

public class Contact {
	public static StringPropertyGetter<Contact> FIRST_NAME = new StringPropertyGetter<Contact>() {
		@Override
		public StringProperty get(Contact bean) {
			return bean.firstName;
		}
	};
	public static StringPropertyGetter<Contact> LAST_NAME = new StringPropertyGetter<Contact>() {
		@Override
		public StringProperty get(Contact bean) {
			return bean.lastName;
		}
	};
	public static BooleanPropertyGetter<Contact> FICTIONAL = new BooleanPropertyGetter<Contact>() {
		@Override
		public BooleanProperty get(Contact bean) {
			return bean.fictional;
		}
	};
	public static IntegerPropertyGetter<Contact> AGE = new IntegerPropertyGetter<Contact>() {
		@Override
		public IntegerProperty get(Contact bean) {
			return bean.age;
		}
	};
	
	private final StringProperty firstName = new SimpleStringProperty("first");
	private final StringProperty lastName = new SimpleStringProperty("last");
	private final BooleanProperty fictional = new SimpleBooleanProperty(true);
	private final IntegerProperty age = new SimpleIntegerProperty(25);
	
	public Contact() {
	}
	
	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public boolean isFictional() {
		return fictional.get();
	}
	
	public void setFictional(boolean fictional) {
		this.fictional.set(fictional);
	}
	
	public int getAge() {
		return age.get();
	}
	
	public void setAge(int age) {
		this.age.set(age);
	}
	
	@Override
	public String toString() {
		return firstName.get() + " " + lastName.get();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
}