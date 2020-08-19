package ca.hferguson.spring.misc;

import lombok.*;

@Data
@AllArgsConstructor
public class Address {
	
	private String accountID;
	private String street;
	private String city;
	private String stateOrProvince;
	private String postalCode;
	
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		Address addr = null;
		if (obj instanceof Address)
			addr = (Address)obj;
		else
			return false;
		if (this.getStreet().equalsIgnoreCase(addr.getStreet())) {
			if (this.getCity().equalsIgnoreCase(addr.getCity())) {
				if (this.getStateOrProvince().equalsIgnoreCase(addr.getStateOrProvince()))
					retVal = true;
			}
		}
		return retVal;
	}
	
	@Override 
	public int hashCode() {
		int retVal = super.hashCode();
		
		return retVal;
	}
}
