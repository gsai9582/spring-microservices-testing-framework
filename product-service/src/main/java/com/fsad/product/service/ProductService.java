package com.fsad.product.service;

import com.fsad.product.domain.Product;
import com.fsad.product.dto.ProductRequest;
import com.fsad.product.dto.ProductResponse;
import com.fsad.product.exception.ResourceNotFoundException;
import com.fsad.product.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        Product saved = repository.save(product);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return toResponse(repository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        repository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}

