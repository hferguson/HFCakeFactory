package ca.hferguson.spring.service;

import java.util.Optional;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ca.hferguson.spring.bean.Item;
import ca.hferguson.spring.persistence.ItemEntity;
import ca.hferguson.spring.persistence.PastryRepository;


@Service
@Component
public class PastryService implements IPastryService {

	@Autowired
	PastryRepository repository;
	
	public PastryService(PastryRepository repo) {
		this.repository = repo;
	}
	
	@Override
	public Item findOne(String sku) {
		Item item = null;
		Optional<ItemEntity> dbItem = repository.findById(sku);
		if (!dbItem.isEmpty()) {
			ItemEntity entity = dbItem.get();
			item = entityToDomainItem(entity);
		}
		return item;
	}
	@Override
	public Iterable<Item> getProducts() {
		 var it = repository.findAll();
		 return StreamSupport.stream(it.spliterator(), false)
         .map(entity -> entityToDomainItem(entity))
         .collect(Collectors.toList());


	}
	
	private Item entityToDomainItem(ItemEntity entity) {
		Item item = null;
		if (entity != null)
			item = new Item(entity.getSku(), entity.getTitle(), entity.getPrice(), entity.getDescription(), entity.getImage());
		return item;
	}

}
