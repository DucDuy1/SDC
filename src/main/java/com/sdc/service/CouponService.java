package com.sdc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	public String create(@ModelAttribute("coupon") @Valid Coupon coupon,
			@RequestParam("expired_Date") String expired_Date, BindingResult bindingResult) throws ParseException {
		if (bindingResult.hasErrors()) {
			return "coupon/create";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		coupon.setExpiredDate(dateFormat.parse(expired_Date));
		couponRepo.save(coupon);
		return "redirect:/coupon/search";
	}

	public String update(@RequestParam("id") int id, Model model) {
		Coupon coupon = couponRepo.getById(id);
		model.addAttribute("coupon", coupon);
		return "coupon/update.html";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Coupon coupon) {
		Coupon oldOne = couponRepo.getById(coupon.getId());
		;
		oldOne.setName(coupon.getName());
		couponRepo.save(oldOne);
		return "redirect:/coupon/search";
	}

	public String delete(@RequestParam(value = "id") int id) {
		couponRepo.deleteById(id);
		return "redirect:/coupon/search";
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
