package ca.hferguson.spring.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import ca.hferguson.spring.misc.Address;
import ca.hferguson.spring.persistence.*;

@Service
public class AddressService  implements IAddressService  {

	AddressRepository repository;
	
	
	public AddressService(AddressRepository repo) {
		this.repository = repo;
	}
	
	@Override 
	public Address addAddress(Address addy) {
		AddressEntity entity = domainToEntityAddress(addy);
		repository.save(entity);
		//addy.setId(entity.getId());
		return addy;
	}
	@Override
	public Address findOne(String acctId) {
		Address addy = null;
		Optional<AddressEntity> entity = repository.findById(acctId);
		if (entity.isPresent()) {
			AddressEntity ae = entity.get();
			addy = entityToDomainAddress(ae);
		}
		return addy;
	}

	@Override
	public Iterable<Address> getAddresses() {
		
		var it = repository.findAll();
		 return StreamSupport.stream(it.spliterator(), false)
        .map(entity -> entityToDomainAddress(entity))
        .collect(Collectors.toList());
		
	}

	@Override
	public Address updateAddress(Address addy) {
		AddressEntity entity = domainToEntityAddress(addy);
		
		entity = repository.save(entity);
		return entityToDomainAddress(entity);
	}
	
	protected Address entityToDomainAddress(AddressEntity entity) {
		Address addy = null;
		if (entity != null)
			addy = new Address(entity.getAccountID(),  entity.getStreet(), entity.getCity(), entity.getStateOrProvince(), entity.getPostalCode());
		return addy;
	}
	
	protected AddressEntity domainToEntityAddress(Address addy) {
		AddressEntity entity = new AddressEntity(addy.getAccountID(), addy.getStreet(), addy.getCity(), addy.getStateOrProvince(), addy.getPostalCode());
		return entity;
	}
	
}
