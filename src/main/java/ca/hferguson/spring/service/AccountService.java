package ca.hferguson.spring.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ca.hferguson.spring.misc.*;
import ca.hferguson.spring.persistence.*;

@Service
public class AccountService implements IAccountService {
	private AccountRepository repository;
	
	public AccountService(AccountRepository repo) {
		this.repository = repo;
	}
	public Account addAccount(Account user) {
		
		repository.save(domainToEntityAccount(user));
		return user;
	}
	public Account findOne(String acctID) {
		Account acct = null;
		Optional<AccountEntity> entity = repository.findById(acctID);
		if (entity.isPresent()) {
			acct = entityToDomainAccount(entity.get());
		}
		return acct;
	}
	public Iterable<Account> getAccounts() {
		var it = repository.findAll();
		 return StreamSupport.stream(it.spliterator(), false)
       .map(entity -> entityToDomainAccount(entity))
       .collect(Collectors.toList());
	}
	
	@Override
	public UserDetails findUsername(String username) {
		UserDetails user = null;
		Optional<AccountEntity> acct = repository.findById(username);
		if (acct.isPresent()) {
			user = acct.get();
		}
		return user;
	}
	protected AccountEntity domainToEntityAccount(Account acct) {
		AccountEntity entity = new AccountEntity(acct.getUid(), acct.getFirstName(), acct.getLastName(), acct.getPasswd());
		
		return entity;
	}
	
	protected Account entityToDomainAccount(AccountEntity entity) {
		Account acct = new Account(entity.getUid(), entity.getFirstName(), entity.getLastName(), entity.getPasswd());
		return acct;
	}
	
}
