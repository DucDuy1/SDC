package com.sdc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sdc.entity.BillItems;
import com.sdc.service.BillItemsService;

@Controller
@RequestMapping("/billitems")
public class BillItemsController {
	@Autowired
	BillItemsService billItemsService;

	@GetMapping("/create")
	public String create(Model model) {
		return billItemsService.create(model);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute BillItems billitems, BindingResult blindingResult, Model model) {
		return billItemsService.create(billitems, blindingResult);
	}

	@GetMapping("/update") // ?id=12
	public String update(@RequestParam("id") int id, Model model) {
		return billItemsService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute BillItems billitems) {
		return billItemsService.update(billitems);
	}

	@GetMapping("/delete") // ?id=12
	public String delete(@RequestParam("id") int billItemsId) {
		return billItemsService.delete(billItemsId);// url maping
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "billItemsId", required = false) Integer billItemsId,
			@RequestParam(name = "idBill", required = false) Integer idBill,
			@RequestParam(name = "productId", required = false) Integer productId,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		return billItemsService.search(model, name, billItemsId, idBill, productId, sortBy, page, size);
	}
}
