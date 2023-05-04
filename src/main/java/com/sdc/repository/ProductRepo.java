package com.sdc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	Product findById(String s);

	@Query("SELECT u FROM Product u WHERE u.name LIKE :x")
	Page<Product> searchAll(@Param("x") String s, Pageable pageable);

	@Query("SELECT u FROM Product u WHERE u.id = uId ")
	Page<Product> searchById(@Param("uId") Long id, Pageable pageable);

	@Query("SELECT u FROM Product u JOIN u.category c " + "WHERE c.id = :cId")
	Page<Product> searchByCategory(@Param("cId") int categoryId, Pageable pageable);

	@Query("SELECT u FROM Product u JOIN u.category c " + "WHERE c.name = :cName")
	Page<Product> searchByNameCategory(@Param("cName") String categoryName, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.id = pId ")
	Product get(Long pId);
}
