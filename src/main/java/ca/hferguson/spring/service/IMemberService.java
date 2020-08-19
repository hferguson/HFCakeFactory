package ca.hferguson.spring.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import ca.hferguson.spring.misc.Account;
import ca.hferguson.spring.misc.Address;

@Service
public interface IMemberService {

	public Account addAccount(Account acct);
	public Account findAccount(String uid);
	public Collection<Account> listAccounts();
	public Address updateAddress(Address addy);
}
