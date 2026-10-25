package com.fsad.order.service;

import com.fsad.order.client.ProductClient;
import com.fsad.order.domain.Order;
import com.fsad.order.dto.OrderRequest;
import com.fsad.order.dto.OrderResponse;
import com.fsad.order.exception.RemoteServiceException;
import com.fsad.order.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        try {
            // validate product existence by calling Product service
            productClient.getProductById(request.getProductId());
        } catch (RuntimeException ex) {
            throw new RemoteServiceException("Product service call failed: " + ex.getMessage());
        }

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        Order saved = repository.save(order);
        return new OrderResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }
}

