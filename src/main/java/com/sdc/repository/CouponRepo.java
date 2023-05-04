package com.sdc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.Coupon;

public interface CouponRepo extends JpaRepository<Coupon, Integer>{
	Coupon findByCouponCode(String s);
	
	@Query("SELECT u FROM Coupon u WHERE u.name LIKE :x")
	Page<Coupon> searchAll(@Param("x") String s, Pageable pageable);
	@Query("SELECT u FROM Coupon u WHERE u.id = uId ")
	Page<Coupon> searchById(@Param("uId") int id, Pageable pageable);
}
