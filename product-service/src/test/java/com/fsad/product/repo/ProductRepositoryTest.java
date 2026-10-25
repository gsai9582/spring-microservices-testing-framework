package com.fsad.product.repo;

import com.fsad.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void saveAndFindAndDelete() {
        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1200.0);

        Product saved = repository.save(p);
        assertNotNull(saved.getId());

        Product found = repository.findById(saved.getId()).orElseThrow();
        assertEquals("Laptop", found.getName());
        assertEquals(1200.0, found.getPrice());

        repository.deleteById(saved.getId());
        assertTrue(repository.findById(saved.getId()).isEmpty());
    }
}

