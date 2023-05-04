package com.sdc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.BillItems;

public interface BillItemsRepo extends JpaRepository<BillItems, Integer> {
	@Query("SELECT u FROM BillItems u JOIN u.bill b JOIN u.product p "
			+ "WHERE b.name LIKE :x OR p.name LIKE :x AND  b.id = :bId OR p.id = :pId ")
	Page<BillItems> searchByAll(@Param("x") String name,@Param("bId") int idBill, @Param("pId") int productId, Pageable pageable);

}
