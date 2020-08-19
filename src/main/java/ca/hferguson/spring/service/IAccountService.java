package ca.hferguson.spring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ca.hferguson.spring.misc.Account;

@Service
public interface IAccountService {

	public Account addAccount(Account user);
	public Account findOne(String acctID);
	public Iterable<Account> getAccounts();
	public UserDetails findUsername(String username);
}
