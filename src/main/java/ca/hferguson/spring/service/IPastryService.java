package ca.hferguson.spring.service;

import org.springframework.stereotype.Service;

import ca.hferguson.spring.bean.Item;


@Service
public interface IPastryService {
	Iterable<Item> getProducts();
	
	Item findOne(String sku);
}
