package ca.hferguson.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ca.hferguson.spring.misc.*;
import ca.hferguson.spring.persistence.*;

@DataJpaTest
public class TestMemberService {

	@Autowired
    TestEntityManager testEntityManager;
	
	@Autowired
	private AddressRepository addressRepository;
	private IAddressService addyService;
	
	@Autowired
	private AccountRepository accountRepository;
	private IAccountService acctService;
	
	private IMemberService service;
	
	@BeforeEach
    void setup() {
		this.addyService = new AddressService(addressRepository);
		this.acctService = new AccountService(accountRepository);
        this.service = new MemberService(this.acctService, this.addyService);
    }
	
	@Test
	void addNewAccount() {
		String email = "hugh@bell.net";
		Account acct = buildAccount(email);
		saveAccount(acct);
		Account acctSaved = service.findAccount(email);
		assertThat(acctSaved != null);
		Address addrSaved = acctSaved.getAddress();
		assertThat(addrSaved != null);
		assertThat(addrSaved.getAccountID() == acctSaved.getUid());
		assertThat(email.equals(acctSaved.getUid()));
		assertThat(service.listAccounts().size() == 1);
		//assertThat(service.listAccounts().)
	}
	
	@Test 
	void updateAddress() {
		String email = "hugh@bell.net";
		Account acct = buildAccount(email);
		Address addy = acct.getAddress();
		addy.setStreet("185 Ontario St.");
		addy.setCity("Kingston");
		addy.setPostalCode("K7L 2Y7");
		acct.setAddress(addy);
		saveAccount(acct);
		
		Account acctSaved = service.findAccount(email);
		assertThat(acctSaved.getAddress().getCity().contentEquals("Kingston"));
		assertThat(service.listAccounts().size() == 1);
	}
	private Account buildAccount(String email) {
		Account acct = new Account(email, "Hugh", "Ferguson", "newPass123");
		Address addr = new Address(email, "117 Goulburn Ave", "Ottawa","ON", "K1N 8C9");
		acct.setAddress(addr);
		return acct;
	}
	private void saveAccount(Account acct) {
		AccountEntity uEntity = new AccountEntity(acct.getUid(), acct.getFirstName(), acct.getLastName(), acct.getPasswd());
		AddressEntity aEntity = null;
		Address addy = acct.getAddress();
		if (addy != null)
			aEntity = new AddressEntity(addy.getAccountID(), addy.getStreet(), addy.getCity(), addy.getStateOrProvince(), addy.getPostalCode());
		testEntityManager.persistAndFlush(uEntity);
		if (aEntity != null)
			testEntityManager.persistAndFlush(aEntity);
		
	}
}
