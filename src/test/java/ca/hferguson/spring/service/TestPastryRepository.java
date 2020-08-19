package ca.hferguson.spring.service;


import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ca.hferguson.spring.bean.Item;
import ca.hferguson.spring.persistence.ItemEntity;
import ca.hferguson.spring.persistence.PastryRepository;

@DataJpaTest
public class TestPastryRepository {
	@Autowired
    TestEntityManager testEntityManager;

    @Autowired
    PastryRepository itemRepository;

    private IPastryService catalogService;

    @BeforeEach
    void setup() {
        this.catalogService = new PastryService(this.itemRepository);
    }
	
    
    
    @Test
    @DisplayName("returns data from the database")
    void returnsDataFromDatabase() {
        String expectedTitle = "Victoria Sponge";
        saveTestItem(expectedTitle, BigDecimal.valueOf(5.55));

        Iterable<Item> items = catalogService.getProducts();

        org.assertj.core.api.Assertions.assertThat(items).anyMatch(item -> expectedTitle.equals(item.getTitle()));
    }

    private void saveTestItem(String title, BigDecimal price) {
        ItemEntity itemEntity = new ItemEntity("test-sku", title, price, "", "");
        

        testEntityManager.persistAndFlush(itemEntity);
    }
    
}
