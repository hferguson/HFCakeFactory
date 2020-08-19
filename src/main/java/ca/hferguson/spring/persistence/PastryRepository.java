package ca.hferguson.spring.persistence;

import org.springframework.data.repository.CrudRepository;


public interface PastryRepository extends CrudRepository<ItemEntity, String> {

}
