package com.sdc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer> {
	//Page<Bill> findByAgeGreaterThanEqual(Date from);

	@Query("SELECT u FROM Bill u JOIN u.user r " + "WHERE r.username = :rName")
	Page<Bill> searchByNameUser(@Param("rName") String nameUser, Pageable pageable);

	@Query("SELECT u FROM Bill u JOIN u.user r " + "WHERE r.id = :rId" + " AND u.buyDate >= :from "
			+ " AND u.buyDate <= :to AND u.name LIKE :x")
	Page<Bill> searchByAll(@Param("rId") int userId, @Param("from") Date from, @Param("to") Date to,
			@Param("x") String name, Pageable pageable);

	@Query("SELECT u FROM Bill u WHERE u.buyDate >= :date")
	List<Bill> searchByDate(@Param("date") Date date);

	@Query("SELECT count(b.id) AS sl , MONTH(buyDate) AS thang FROM Bill b GROUP BY MONTH(buyDate)")
	List<Object[]> statisticalBillByMonth();

	@Query("SELECT count(b.id) AS sl , u.username AS username FROM Bill b JOIN b.user u GROUP BY u.username")
	List<Object[]> statisticalByUser();
}
