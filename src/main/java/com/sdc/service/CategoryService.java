package com.sdc.service;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.sdc.entity.Category;
import com.sdc.repository.CategoryRepo;

@Service
public class CategoryService {
	private static Logger logger = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	CategoryRepo categoryRepo;

	public String create(Model model, HttpServletRequest request) {
		User currentUser = (User) SecurityContextHolder.getContext() // User của sicurity
				.getAuthentication().getPrincipal();
		System.out.println(currentUser.getUsername());
		if (request.isUserInRole("ADMIN")) {
			System.out.println("ADMIN");
		}
		model.addAttribute("category", new Category());
		return "category/create";
	}

	public String create(@ModelAttribute("category") @Valid Category category, BindingResult blindingResult) {
		if (blindingResult.hasErrors()) {
			return "category/create";
		}
		categoryRepo.save(category);
		return "redirect:/category/search";
	}

	public String update(@RequestParam("id") int id, Model model) {
		Category category = categoryRepo.getById(id);
		model.addAttribute("category", category);
		return "category/update.html";
	}

	public String update(@ModelAttribute Category category) {
		Category oldOne = categoryRepo.getById(category.getId());
		;
		oldOne.setName(category.getName());
		categoryRepo.save(oldOne);
		return "redirect:/category/search";
	}

	public String delete(@RequestParam(value = "id") int id) {
		categoryRepo.deleteById(id);
		return "redirect:/category/search";
	}

	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		// Sort sort = Sort.by("id").ascending();
//		if (sortBy != null && sortBy.equals("name")) {
//			sort = Sort.by("name").ascending();
//		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		if (name != null && !name.isEmpty()) {
			Page<Category> pageCategory = categoryRepo.searchAll("%" + name + "%", pageable);
			model.addAttribute("list", pageCategory.toList());
			model.addAttribute("totalPage", pageCategory.getTotalPages());
		} else if (id != null) {
			Category category = categoryRepo.findById(id).orElse(null);
			if (category != null) {
				model.addAttribute("list", Arrays.asList(category));
			} else
				logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<Category> pageCategory = categoryRepo.findAll(pageable);
			model.addAttribute("list", pageCategory.toList());
			model.addAttribute("totalPage", pageCategory.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "category/search";
	}
}
