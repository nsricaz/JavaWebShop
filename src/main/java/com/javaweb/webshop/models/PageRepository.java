package com.javaweb.webshop.models;

import java.util.List;

import com.javaweb.webshop.models.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PageRepository extends JpaRepository<Page, Integer> {
    
Page findBySlug(String slug);

List<Page> findAllByOrderBySortingAsc();

// @Modifying
// @Query("update pages u set u.sorting = ?1 where u.id = ?2")
// void setpagesSortingById(Integer sorting, Integer userId);

}