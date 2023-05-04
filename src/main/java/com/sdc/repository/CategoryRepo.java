package com.sdc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	@Query("SELECT u FROM Category u WHERE u.name LIKE :x")
	Page<Category> searchAll(@Param("x") String s, Pageable pageable);
}
