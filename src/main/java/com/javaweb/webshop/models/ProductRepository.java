package com.javaweb.webshop.models;

import java.util.List;

import com.javaweb.webshop.models.data.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    
    Product findBySlug(String slug);

 Page<Product> findAll (Pageable pagable);

 List<Product> findAllByCategoryId(String categoryId, Pageable pagable);

 long countByCategoryId(String categoryId);

}