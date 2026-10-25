package com.fsad.product.service;

import com.fsad.product.domain.Product;
import com.fsad.product.dto.ProductRequest;
import com.fsad.product.dto.ProductResponse;
import com.fsad.product.exception.ResourceNotFoundException;
import com.fsad.product.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void addProduct_success() {
        ProductRequest req = new ProductRequest();
        req.setName("Phone");
        req.setPrice(999.0);

        Product saved = new Product(1L, req.getName(), req.getPrice());
        when(repository.save(any(Product.class))).thenReturn(saved);

        ProductResponse resp = service.addProduct(req);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Phone", resp.getName());
        assertEquals(999.0, resp.getPrice());
        verify(repository).save(any(Product.class));
    }

    @Test
    void getProductById_notFound_throws() {
        when(repository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getProductById(123L));
        verify(repository).findById(123L);
    }

    @Test
    void updateProduct_success() {
        Long id = 1L;
        Product existing = new Product(id, "Old", 10.0);

        ProductRequest req = new ProductRequest();
        req.setName("New");
        req.setPrice(20.0);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductResponse resp = service.updateProduct(id, req);
        assertEquals(id, resp.getId());
        assertEquals("New", resp.getName());
        assertEquals(20.0, resp.getPrice());
    }

    @Test
    void deleteProduct_notFound_throws() {
        when(repository.existsById(10L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProduct(10L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getAllProducts_mapsResponses() {
        List<Product> products = List.of(
                new Product(1L, "A", 1.0),
                new Product(2L, "B", 2.0)
        );
        when(repository.findAll()).thenReturn(products);

        List<ProductResponse> resp = service.getAllProducts();
        assertEquals(2, resp.size());
        assertEquals("A", resp.get(0).getName());
        assertEquals("B", resp.get(1).getName());
    }
}

