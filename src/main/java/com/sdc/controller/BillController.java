package com.sdc.controller;

import java.text.ParseException;

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

import com.sdc.entity.Bill;
import com.sdc.service.BillService;

@Controller
@RequestMapping("/bill")
public class BillController {
	@Autowired
	BillService billService;

	@GetMapping("/create")
	public String create(Model model) {
		return billService.create(model);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("bill") @Valid Bill bill, @RequestParam("buy_date") String buy_date,
			@RequestParam("coupon_code") String couponCode, BindingResult blindingResult, Model model)
			throws ParseException {
		return billService.create(bill, buy_date, couponCode, blindingResult, model);
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model) {
		return billService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Bill bill) {
		return billService.update(bill);
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		return billService.delete(id);// url maping
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "userId", required = false) Integer userId,
			@RequestParam(name = "nameUser", required = false) String nameUser,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws ParseException {
		return billService.search(model, name, userId, nameUser, fromDate, toDate, page, size);
	}

	@GetMapping("/thongke")
	public String thongKe(Model model) throws ParseException {
		return billService.thongKe(model);
	}

	@GetMapping("/thongKe1")
	public String thongKe1(Model model) throws ParseException {
		return billService.thongKe1(model);
	}
}
