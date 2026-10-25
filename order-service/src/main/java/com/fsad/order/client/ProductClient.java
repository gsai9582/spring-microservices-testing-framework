package com.fsad.order.client;

import com.fsad.order.client.dto.ProductDto;

public interface ProductClient {
    ProductDto getProductById(Long productId);
}


