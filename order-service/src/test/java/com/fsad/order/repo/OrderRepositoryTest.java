package com.fsad.order.repo;

import com.fsad.order.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Test
    void saveFindDelete() {
        Order o = new Order();
        o.setProductId(1L);
        o.setQuantity(4);

        Order saved = repository.save(o);
        assertNotNull(saved.getId());

        Order found = repository.findById(saved.getId()).orElseThrow();
        assertEquals(1L, found.getProductId());
        assertEquals(4, found.getQuantity());

        repository.deleteById(saved.getId());
        assertTrue(repository.findById(saved.getId()).isEmpty());
    }
}

