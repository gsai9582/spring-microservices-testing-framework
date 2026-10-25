package com.fsad.order.service;

import com.fsad.order.client.ProductClient;
import com.fsad.order.client.dto.ProductDto;
import com.fsad.order.domain.Order;
import com.fsad.order.dto.OrderRequest;
import com.fsad.order.dto.OrderResponse;
import com.fsad.order.exception.RemoteServiceException;
import com.fsad.order.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderService service;

    @Test
    void createOrder_success() {
        OrderRequest req = new OrderRequest();
        req.setProductId(1L);
        req.setQuantity(3);

        when(productClient.getProductById(1L)).thenReturn(new ProductDto(1L, "Phone", 100.0));
        when(repository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(10L);
            return o;
        });

        OrderResponse resp = service.createOrder(req);
        assertEquals(10L, resp.getId());
        assertEquals(1L, resp.getProductId());
        assertEquals(3, resp.getQuantity());

        verify(repository).save(any(Order.class));
    }

    @Test
    void createOrder_productServiceFailure_throwsRemoteServiceException() {
        OrderRequest req = new OrderRequest();
        req.setProductId(1L);
        req.setQuantity(3);

        when(productClient.getProductById(1L)).thenThrow(new RuntimeException("timeout"));

        assertThrows(RemoteServiceException.class, () -> service.createOrder(req));
        verify(repository, never()).save(any(Order.class));
    }
}

