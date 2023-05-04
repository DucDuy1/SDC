package com.sdc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sdc.entity.User;
import com.sdc.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/create")
	public String create(Model model) {
		return userService.create(model);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("user") @Valid User user, BindingResult blindingResult) {
		return userService.create(user, blindingResult);
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model) {
		return userService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute User user) {
		return userService.update(user);
	}

	@GetMapping("/delete")
	public String delete(HttpServletRequest req, @RequestParam("id") int id) {
		return userService.delete(req, id);
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		return userService.search(model, username, id, sortBy, page, size);
	}
}
