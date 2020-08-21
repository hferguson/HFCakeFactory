package ca.hferguson.spring.service;

import java.util.*;
import org.springframework.stereotype.Component;
import ca.hferguson.spring.misc.*;

@Component
public class MemberService implements IMemberService {

	private IAddressService addressService;
	private IAccountService accountService;
	
	
	public MemberService(IAccountService acctSvc, IAddressService addrSvc) {
		this.accountService = acctSvc;
		this.addressService = addrSvc;
	}
	
	@Override
	public Account addAccount(Account acct) {
		if (this.accountService.addAccount(acct) != null) {
			if (acct.getAddress() != null)
				this.addressService.addAddress(acct.getAddress());
		}
		return acct;
	}
	
	@Override
	public Account findAccount(String uid) {
		Account acct = accountService.findOne(uid);
		if (acct != null)
			acct.setAddress(findAddress(uid));
		return acct;
	}
	
	@Override
	public Address findAddress(String uid) {
		return addressService.findOne(uid);
	}
	@Override
	public Collection<Account> listAccounts() {
		Collection<Account> accounts = new ArrayList<Account>();
		Iterator<Account> iter = this.accountService.getAccounts().iterator();
		while (iter.hasNext()) {
			// fetch appropriate address for each account
			// This is very long-winded, and an example of where JDBC might
			// be better as we would have been able to do a SQL join
			Account acct = iter.next();
			Address addy = addressService.findOne(acct.getUid());
			if (addy != null)
				acct.setAddress(addy);
			accounts.add(acct);
		}
		return accounts;
	}

	@Override 
	public Address updateAddress(Address addy) {
		return this.addressService.updateAddress(addy);
	}
	/*
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log("UserDetailsService.loadUserByUsername called. Username " + username);
		UserDetails uDetails = accountService.findUsername(username);
		if (uDetails == null)
			throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
		//log("retrieved username " + uDetails.getUsername());
		//log("retrieved passwd " + uDetails.getPassword());
		return uDetails;
		
		
	}*/
	
	
}
