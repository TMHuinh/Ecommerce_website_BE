package com.example.productservice.repository;

import com.example.productservice.entity.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends MongoRepository<ProductType, String> {
}
