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

import com.sdc.entity.Coupon;
import com.sdc.service.CouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController {
	@Autowired
	CouponService couponService;

	@GetMapping("/create")
	public String create(Model model) {
		return couponService.create(model);
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("coupon") @Valid Coupon coupon,
			@RequestParam("expired_date") String expired_date, BindingResult bindingResult) throws ParseException {
		return couponService.create(coupon, expired_date, bindingResult);
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model) {
		return couponService.update(id, model);
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Coupon coupon) {
		return couponService.update(coupon);
	}

	@GetMapping("/delete")
	public String delete(@RequestParam(value = "id") int id) {
		return couponService.delete(id);
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "sortBy", required = false) Integer sortBy,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		return couponService.search(model, name, id, sortBy, page, size);
	}
}
