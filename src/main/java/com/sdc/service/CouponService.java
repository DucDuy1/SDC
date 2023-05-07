package com.sdc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

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
import org.springframework.web.bind.annotation.PostMapping;

import com.sdc.controller.CouponController;
import com.sdc.entity.Coupon;
import com.sdc.repository.CouponRepo;

@Service
public class CouponService {
	private static Logger logger = LoggerFactory.getLogger(CouponController.class);
	@Autowired
	CouponRepo couponRepo;

	public String create(Model model) {
		model.addAttribute("coupon", new Coupon());
		return "coupon/create";
	}

	public String create(Coupon coupon, String expired_date, BindingResult bindingResult) throws ParseException {
		if (bindingResult.hasErrors()) {
			return "coupon/create";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		coupon.setExpiredDate(dateFormat.parse(expired_date));
		couponRepo.save(coupon);
		return "redirect:/coupon/search";
	}

	public String update(int id, Model model) {
		Coupon coupon = couponRepo.getById(id);
		model.addAttribute("coupon", coupon);
		return "coupon/update.html";
	}

	public String update(Coupon coupon, String expired_date)throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		coupon.setExpiredDate(dateFormat.parse(expired_date));
		couponRepo.save(coupon);
		return "redirect:/coupon/search";
	}

	public String delete(int id) {
		couponRepo.deleteById(id);
		return "redirect:/coupon/search";
	}

	public String search(Model model, String name, Integer id, Integer sortBy, Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		Sort sort = Sort.by("id").ascending();
		if (sortBy != null && sortBy.equals("name")) {
			sort = Sort.by("name").ascending();
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		if (name != null && !name.isEmpty()) {
			Page<Coupon> pageCoupon = couponRepo.searchAll("%" + name + "%", pageable);
			model.addAttribute("list", pageCoupon.toList());
			model.addAttribute("totalPage", pageCoupon.getTotalPages());
		} else if (id != null) {
			Coupon coupon = couponRepo.findById(id).orElse(null);
			if (coupon != null) {
				model.addAttribute("list", Arrays.asList(coupon));
			} else
				// log
				logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<Coupon> pageCoupon = couponRepo.findAll(pageable);
			model.addAttribute("list", pageCoupon.toList());
			model.addAttribute("totalPage", pageCoupon.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sortBy", sortBy == null ? "" : sortBy);
		return "coupon/search";
	}
}
