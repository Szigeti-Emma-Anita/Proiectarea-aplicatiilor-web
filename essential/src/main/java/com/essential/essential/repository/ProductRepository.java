package com.essential.essential.repository;

import com.essential.essential.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop4ByOrderByIdDesc();

    List<Product> findByGenderIgnoreCase(String gender);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByGenderIgnoreCaseAndCategoryIgnoreCase(String gender, String category);

    @Query("select distinct p.gender from Product p where p.gender is not null order by p.gender")
    List<String> findDistinctGenders();

    @Query("select distinct p.category from Product p where p.category is not null order by p.category")
    List<String> findDistinctCategories();
}
