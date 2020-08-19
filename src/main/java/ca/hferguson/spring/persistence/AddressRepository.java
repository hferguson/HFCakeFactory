package ca.hferguson.spring.persistence;

import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressEntity, String> {

}
