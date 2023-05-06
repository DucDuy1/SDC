package com.sdc.service;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.sdc.controller.UserController;
import com.sdc.entity.User;
import com.sdc.repository.UserRepo;

@Service
public class UserService {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserRepo userRepo;

	public String create(Model model) {
		model.addAttribute("user", new User());
		return "user/create";
	}

	public String create(User user, BindingResult blindingResult) {
		if (blindingResult.hasErrors()) {
			return "user/create";
		}
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user);
		return "redirect:/user/search";
	}

	public String update(int id, Model model) {
		User user = userRepo.getById(id);
		model.addAttribute("user", user);
		return "user/update.html";
	}

	public String update(User user) {
		userRepo.save(user);
		return "redirect:/user/search";
	}

	public String delete(HttpServletRequest req, int id) {
		userRepo.deleteById(id);
		return "redirect:/user/search";
	}

	public String search(Model model, String username, Integer id, Integer sortBy, Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		Sort sort = Sort.by("id").ascending();
		if (sortBy != null && sortBy.equals("username")) {
			sort = Sort.by("username").ascending();
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		if (username != null && !username.isEmpty()) {
			Page<User> pageUser = userRepo.searchAl("%" + username + "%", pageable);
			model.addAttribute("list", pageUser.toList());
			model.addAttribute("totalPage", pageUser.getTotalPages());
		} else if (id != null) {
			User user = userRepo.findById(id).orElse(null);
			if (user != null) {
				model.addAttribute("list", Arrays.asList(user));
			} else
				logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<User> pageUser = userRepo.findAll(pageable);
			model.addAttribute("list", pageUser.toList());
			model.addAttribute("totalPage", pageUser.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("username", username == null ? "" : username);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "user/search";
	}
}
