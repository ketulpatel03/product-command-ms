package com.java.service;

import com.java.dto.ProductEvent;
import com.java.entity.Product;

public interface ProductService {

    Product addProduct(ProductEvent productEvent);

    Product updateProduct(ProductEvent productEvent, Long id);

}
