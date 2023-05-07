package com.sdc.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.sdc.controller.ProductController;
import com.sdc.entity.Category;
import com.sdc.entity.Product;
import com.sdc.repository.CategoryRepo;
import com.sdc.repository.ProductRepo;

@Service
public class ProductService {
	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	ProductRepo productRepo;

	@Autowired
	CategoryRepo categoryRepo;

	public String create(Model model) {
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("product", new Product());
		return "product/create";
	}

	public String create(Product product, MultipartFile file, BindingResult blindingResult) {
		if (blindingResult.hasErrors()) {
			return "product/create";
		}
		// lưu lại file vào 1 folder, sau đó lấy url save to db
		if (file != null && file.getSize() > 0) {
			try {
				final String folder = "/Applications";
				String originFilename = file.getOriginalFilename();
				File newFile = new File(folder + "/" + originFilename);
				// coppy noi dung file upload vao file new
				file.transferTo(newFile);
				// lưu lại url flie vao db
				product.setImageURL(originFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		productRepo.save(product);
		return "redirect:/product/search";
	}

	public void download(String imageURL, HttpServletResponse response) {
		final String folder = "/Applications";
		File file = new File(folder + "/" + imageURL);
		if (file.exists()) {
			try {
				Files.copy(file.toPath(), response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String update(int id, Model model) {
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		Product product = productRepo.getById(id);
		model.addAttribute("product", product);
		return "product/update.html";
	}

	public String update(Product product) {
		productRepo.save(product);
		return "redirect:/product/search";
	}

	public String delete(int id) {
		productRepo.deleteById(id);
		return "redirect:/product/search";
	}

	public String search(Model model, String name, Integer id, Integer categoryId, String categoryName, Integer sortBy,
			Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		Sort sort = Sort.by("id").ascending();
		if (sortBy != null && sortBy.equals("name")) {
			sort = Sort.by("name").ascending();
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		if (categoryId != null ) {
			Page<Product> pageProduct = productRepo.searchByIdCategory(categoryId, pageable);
			model.addAttribute("list", pageProduct.toList());
			model.addAttribute("totalPage", pageProduct.getTotalPages());
		} else if (name != null && !name.isEmpty()) {
			Page<Product> pageProduct = productRepo.searchAll("%" + name + "%", pageable);
			model.addAttribute("list", pageProduct.toList());
			model.addAttribute("totalPage", pageProduct.getTotalPages());
		} else if (id != null) {
			Product product = productRepo.findById(id).orElse(null);
			if (product != null) {
				model.addAttribute("list", Arrays.asList(product));
			} else
				logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<Product> pageProduct = productRepo.findAll(pageable);
			model.addAttribute("list", pageProduct.toList());
			model.addAttribute("totalPage", pageProduct.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("categoryId", categoryId == null ? "" : categoryId);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("categoryName", categoryName == null ? "" : categoryName);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "product/search";
	}
}
