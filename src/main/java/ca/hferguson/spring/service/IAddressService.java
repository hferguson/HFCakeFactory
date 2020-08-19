package ca.hferguson.spring.service;



import org.springframework.stereotype.Service;

import ca.hferguson.spring.misc.Address;

@Service
public interface IAddressService {

	public Address addAddress(Address addy);
	public Address findOne(String uid);
	public Iterable<Address> getAddresses();
	public Address updateAddress(Address addy);

}
