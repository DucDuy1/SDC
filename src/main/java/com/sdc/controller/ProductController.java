package com.sdc.controller;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

import com.sdc.entity.Product;
import com.sdc.repository.ProductRepo;
import com.sdc.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	ProductRepo productRepo;

	@GetMapping("/create")
	public String create(Model model) {
		return productService.create(model);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("product") @Valid Product product,
			@RequestParam(name = "file", required = false) MultipartFile file, BindingResult blindingResult) {
		return productService.create(product, file, blindingResult);
	}

	@GetMapping("/download") // ?imageURL
	public void download(@RequestParam("imageURL") String imageURL, HttpServletResponse response) {
		productService.download(imageURL, response);
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model) {
		return productService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Product product) {
		return productService.update(product);
	}

	@GetMapping("/delete") // ?id=12
	public String delete(@RequestParam(value = "id") int id) {
		return productService.delete(id);
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "categoryId", required = false) Integer categoryId,
			@RequestParam(name = "categoryName", required = false) String categoryName,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		return productService.search(model, name, id, categoryId, categoryName, sortBy, page, size);
	}
}
