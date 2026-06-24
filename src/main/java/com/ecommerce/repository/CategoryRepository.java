package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.Category;
import java.util.List;




public interface CategoryRepository extends JpaRepository<Category, Long> {
       Category findByCategoryId(String categoryId);
       List<Category>findByLevel(Integer level);
}
