package com.fsad.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsad.order.dto.OrderRequest;
import com.fsad.order.dto.OrderResponse;
import com.fsad.order.exception.RemoteServiceException;
import com.fsad.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService service;

    @Test
    void createOrder_created() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setProductId(1L);
        req.setQuantity(2);

        when(service.createOrder(any(OrderRequest.class))).thenReturn(new OrderResponse(5L, 1L, 2));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(5L))
                .andExpect(jsonPath("productId").value(1L))
                .andExpect(jsonPath("quantity").value(2));
    }

    @Test
    void createOrder_validationError_returns400() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setProductId(null);
        req.setQuantity(0);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Bad Request"))
                .andExpect(jsonPath("fieldErrors").isArray());
    }

    @Test
    void createOrder_remoteFailure_returns502() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setProductId(1L);
        req.setQuantity(2);

        when(service.createOrder(any(OrderRequest.class))).thenThrow(new RemoteServiceException("Product service call failed"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("error").value("Bad Gateway"))
                .andExpect(jsonPath("message", containsString("Product service")));
    }
}

