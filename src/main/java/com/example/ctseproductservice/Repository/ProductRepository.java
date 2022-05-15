package com.example.ctseproductservice.Repository;

import com.example.ctseproductservice.Model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory (String category);


}
