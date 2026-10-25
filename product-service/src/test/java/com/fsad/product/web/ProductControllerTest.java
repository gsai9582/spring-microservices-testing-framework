package com.fsad.product.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsad.product.dto.ProductRequest;
import com.fsad.product.dto.ProductResponse;
import com.fsad.product.exception.ResourceNotFoundException;
import com.fsad.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService service;

    @Test
    void getAll_ok() throws Exception {
        when(service.getAllProducts()).thenReturn(
                List.of(new ProductResponse(1L, "A", 1.0))
        );

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("A"));
    }

    @Test
    void addProduct_created() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("Phone");
        req.setPrice(999.0);

        when(service.addProduct(any(ProductRequest.class))).thenReturn(new ProductResponse(1L, "Phone", 999.0));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Phone"));
    }

    @Test
    void addProduct_validationError_returns400() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("");
        req.setPrice(null);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Bad Request"))
                .andExpect(jsonPath("fieldErrors").isArray());
    }

    @Test
    void getById_notFound_returns404_json() throws Exception {
        when(service.getProductById(10L)).thenThrow(new ResourceNotFoundException("Product not found: 10"));

        mockMvc.perform(get("/products/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(containsString("Product not found")))
                .andExpect(jsonPath("error").value("Not Found"));
    }
}

