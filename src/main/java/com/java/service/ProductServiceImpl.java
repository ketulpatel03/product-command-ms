package com.java.service;

import com.java.dto.ProductEvent;
import com.java.entity.Product;
import com.java.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Value("${cqrs.product.event.kafka.producer.topic}")
    private String productEventTopic;

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductServiceImpl(ProductRepository productRepository,
                              KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Product addProduct(ProductEvent productEvent) {
        Product product = productRepository.save(productEvent.getProduct());
        ProductEvent addProductEvent = new ProductEvent("addProduct", product);
        kafkaTemplate.send(productEventTopic, addProductEvent);
        return product;
    }

    @Override
    public Product updateProduct(ProductEvent productEvent, Long id) {
        Optional<Product> productToUpdate = productRepository.findById(id);
        if (productToUpdate.isPresent()) {
            Product product = productToUpdate.get();
            product.setName(productEvent.getProduct().getName());
            product.setDescription(productEvent.getProduct().getDescription());
            product.setPrice(productEvent.getProduct().getPrice());
            Product productSaved = productRepository.save(product);
            ProductEvent savedProductEvent = new ProductEvent("updateProduct", productSaved);
            kafkaTemplate.send(productEventTopic, savedProductEvent);
            return productSaved;
        }
        return null;
    }
}
