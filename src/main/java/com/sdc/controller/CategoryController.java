package com.sdc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sdc.entity.Category;
import com.sdc.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	@GetMapping("/create")
	// @Secured(value = { "ROLE ADMIN" })
	@PreAuthorize("hasRole('ROLE_ADMIN')") // phân role theo hàm
	public String create(Model model, HttpServletRequest request) {// sicurity theo hàm
		return categoryService.create(model, request);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("category") @Valid Category category, BindingResult blindingResult) {
		return categoryService.create(category, blindingResult);
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model) {
		return categoryService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Category category) {
		return categoryService.update(category);
	}

	@GetMapping("/delete")
	public String delete(@RequestParam(value = "id") int id) {
		return categoryService.delete(id);
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		return categoryService.search(model, name, id, sortBy, page, size);
	}
}
