package com.sdc.service;

import java.util.Arrays;
import java.util.List;

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

import com.sdc.controller.BillItemsController;
import com.sdc.entity.Bill;
import com.sdc.entity.BillItems;
import com.sdc.entity.Product;
import com.sdc.repository.BillItemsRepo;
import com.sdc.repository.BillRepo;
import com.sdc.repository.ProductRepo;

@Service
public class BillItemsService {
	private static Logger logger = LoggerFactory.getLogger(BillItemsController.class);

	@Autowired
	BillItemsRepo billItemsRepo;

	@Autowired
	BillRepo billRepo;

	@Autowired
	ProductRepo productRepo;

	public String create(Model model) {
		List<Bill> bills = billRepo.findAll();
		model.addAttribute("bills", bills);
		List<Product> products = productRepo.findAll();
		model.addAttribute("products", products);
		model.addAttribute("billitems", new BillItems());
		return "billitems/create";
	}

	public String create(BillItems billitems, BindingResult blindingResult) {
		if (blindingResult.hasErrors()) {
			return "billitems/create";
		}
		billItemsRepo.save(billitems);
		return "redirect:/billitems/search";
	}

	public String update(int id, Model model) {
		BillItems billitems = billItemsRepo.getById(id);
		model.addAttribute("billitems", billitems);
		List<Bill> bills = billRepo.findAll();
		model.addAttribute("bills", bills);
		List<Product> products = productRepo.findAll();
		model.addAttribute("products", products);
		return "billitems/update.html";
	}

	public String update(BillItems billitems) {
		billItemsRepo.save(billitems);
		return "redirect:/billitems/search";
	}

	public String delete(int id) {
		billItemsRepo.deleteById(id);
		return "redirect:/billitems/search";
	}

	public String search(Model model, String name, Integer id, Integer idBill, Integer productId,
			Integer sortBy, Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		List<Bill> bills = billRepo.findAll();
		model.addAttribute("bills", bills);
		List<Product> products = productRepo.findAll();
		model.addAttribute("products", products);
		Sort sort = Sort.by("id").ascending();
		if (sortBy != null && sortBy.equals("name")) {
			sort = Sort.by("name").ascending();
		} else if (sortBy != null && sortBy.equals("idBill")) {
			sort = Sort.by("idBill").ascending();
		} else if (sortBy != null && sortBy.equals("productId")) {
			sort = Sort.by("productId").ascending();
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		if (name != null && !name.isEmpty()) {
			Page<BillItems> pageBillItems = billItemsRepo.searchByAll("%" + name + "%", productId, idBill, pageable);
			model.addAttribute("list", pageBillItems.toList());
			model.addAttribute("totalPage", pageBillItems.getTotalPages());
		} else if (id != null) {
			BillItems billItems = billItemsRepo.findById(id).orElse(null);
			if (billItems != null) {
				model.addAttribute("list", Arrays.asList(billItems));
			} else
				// log
				logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<BillItems> pageBillItems = billItemsRepo.findAll(pageable);
			model.addAttribute("list", pageBillItems.toList());
			model.addAttribute("totalPage", pageBillItems.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("idBill", idBill == null ? "" : idBill);
		model.addAttribute("productId", productId == null ? "" : productId);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "billitems/search";
	}
}