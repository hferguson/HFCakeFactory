package ca.hferguson.spring.persistence;


import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, String> {

		
}
