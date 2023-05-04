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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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

	public String create(@ModelAttribute BillItems billitems, BindingResult blindingResult, Model model) {
		if (blindingResult.hasErrors()) {
			return "billitems/create";
		}
		billItemsRepo.save(billitems);
		return "redirect:/billitems/search";
	}

	public String update(@RequestParam("id") int id, Model model) {
		BillItems billitems = billItemsRepo.getById(id);
		model.addAttribute("billitems", billitems);
		List<Bill> bills = billRepo.findAll();
		model.addAttribute("bills", bills);
		List<Product> products = productRepo.findAll();
		model.addAttribute("products", products);
		return "billitems/update.html";
	}

	public String update(@ModelAttribute BillItems billitems) {
		BillItems oldOne = billItemsRepo.getById(billitems.getBillItemsId());
		billItemsRepo.save(oldOne);
		return "redirect:/billitems/search";
	}

	public String delete(@RequestParam("id") int billItemsId) {
		billItemsRepo.deleteById(billItemsId);
		return "redirect:/billitems/search";
	}

	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "billItemsId", required = false) Integer billItemsId,
			@RequestParam(name = "idBill", required = false) Integer idBill,
			@RequestParam(name = "productId", required = false) Integer productId,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
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
		Sort sort = Sort.by("billItemsId").ascending();
		if (sortBy != null && sortBy.equals("name")) {
			sort = Sort.by("name").ascending();
		} else if (sortBy != null && sortBy.equals("idBill")) {
			sort = Sort.by("idBill").ascending();
		} else if (sortBy != null && sortBy.equals("productId")) {
			sort = Sort.by("productId").ascending();
		}
		Pageable pageable = PageRequest.of(page, size, sort); // phân trang
		if (name != null && !name.isEmpty()) {
			Page<BillItems> pageBillItems = billItemsRepo.searchByAll("%" + name + "%", productId, idBill, pageable);
			model.addAttribute("list", pageBillItems.toList());
			model.addAttribute("totalPage", pageBillItems.getTotalPages());
		} else if (billItemsId != null) {
			BillItems billItems = billItemsRepo.findById(billItemsId).orElse(null);
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
		model.addAttribute("billItemsId", billItemsId == null ? "" : billItemsId);
		model.addAttribute("idBill", idBill == null ? "" : idBill);
		model.addAttribute("productId", productId == null ? "" : productId);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "billitems/search";
	}
}