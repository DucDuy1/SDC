package com.sdc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
//	List<User> findByUserName(String s);

	User findByUsername(String s);
    
	@Query("SELECT u FROM User u WHERE u.username LIKE :x")
	Page<User> searchAl(@Param("x") String s, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.id > 1")
	Page<User> search(@Param("uId") int id, Pageable pageable);
}
