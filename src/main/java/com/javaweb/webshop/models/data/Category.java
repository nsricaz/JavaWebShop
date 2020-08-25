package com.javaweb.webshop.models.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;



import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcategories;
  
    @Size(min=2,message="Title must be at least 2 characters long")
    private String name;
   
    private String slug;
    private String sorting;
    
    

}