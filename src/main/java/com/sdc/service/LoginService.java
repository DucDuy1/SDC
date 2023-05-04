package com.sdc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sdc.entity.User;
import com.sdc.repository.UserRepo;

//SINGLETON BEEN
@Service
public class LoginService implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User st = userRepo.findByUsername(username);
		if (st == null) {
			throw new UsernameNotFoundException("not found");
		}
		List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
		for (String role : st.getRoles()) {
			list.add(new SimpleGrantedAuthority(role));
		}
		// user cua security
		org.springframework.security.core.userdetails.User currentUser = new org.springframework.security.core.userdetails.User(
				username, st.getPassword(), list);
		return currentUser;
	}
}
