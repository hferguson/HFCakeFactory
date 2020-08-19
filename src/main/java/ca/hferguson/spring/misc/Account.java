package ca.hferguson.spring.misc;

import lombok.Data;

@Data
public class Account {

	private final String uid;	// email address
	private final String firstName;
	private final String lastName;
	private final String passwd;
	
	// We will leave this in the domain model
	// in Entity model, we will take it out.
	// Only the Membership service (sign-up etc) will
	// attempt to put the two together
	private Address address;
	
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		
		return retVal;
	}
	@Override
	public int hashCode() {
		int retVal = super.hashCode();
		
		return retVal;
	}
}
