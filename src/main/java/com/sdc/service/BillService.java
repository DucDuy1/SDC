package com.sdc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.sdc.controller.BillController;
import com.sdc.entity.Bill;
import com.sdc.entity.ThongKe;
import com.sdc.entity.ThongKeTheoNguoiMua;
import com.sdc.entity.User;
import com.sdc.repository.BillRepo;
import com.sdc.repository.CouponRepo;
import com.sdc.repository.UserRepo;

@Service
public class BillService {
	private static Logger logger = LoggerFactory.getLogger(BillController.class);

	@Autowired
	BillRepo billRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	CouponRepo couponRepo;

	@Autowired
	MailService mailService;

	public String create(Model model) {
		List<User> users = userRepo.findAll(); // quan hệ user để chọn
		model.addAttribute("users", users);
		model.addAttribute("bill", new Bill());
		return "bill/create";
	}

	public String create(Bill bill, String buy_date, String couponCode, BindingResult blindingResult, Model model)
			throws ParseException {
		if (blindingResult.hasErrors()) {
			List<User> users = userRepo.findAll();
			model.addAttribute("users", users);
			return "bill/create";
		}
		bill.setBuyDate(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		bill.setBuyDate(sdf.parse(buy_date));
		long expiredDay = couponRepo.findByCouponCode(couponCode).getExpiredDate().getTime();
		if (expiredDay >= bill.getBuyDate().getTime()) {
			bill.setDiscount(couponRepo.findByCouponCode(couponCode).getDiscountAmount());
			bill.setCouponCode(couponRepo.findByCouponCode(couponCode).getCouponCode());
		}
		int payment = bill.getTotalPay() - bill.getTotalPay() * bill.getDiscount() / 100;
		bill.setTotalPay(payment);
		// lên lịch quét để gửi về email xem có đơn hàng
		long currentMilis = new Date().getTime();
		Date date = new Date(currentMilis - 5 * 60 * 1000);
		List<Bill> billList = billRepo.searchByDate(date);
		billRepo.save(bill);
		User user = userRepo.getById(bill.getUser().getId());
		mailService.sendEmail(user.getEmailUser(), "Hoa don", "ban da mua hang thanh cong");
		return "redirect:/bill/search";
	}

	public String update(int id, Model model) {
		Bill bill = billRepo.getById(id);
		model.addAttribute("bill", bill);
		List<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "bill/update.html";
	}

	public String update(Bill bill) {
		billRepo.save(bill);
		return "redirect:/bill/search";
	}

	public String delete(int id) {
		billRepo.deleteById(id);
		return "redirect:/bill/search";// url maping
	}

	public String search(Model model, String name, Integer userId, String nameUser, String fromDate, String toDate,
			Integer page, Integer size) throws ParseException {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 3;
		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		if (nameUser != null && nameUser != null && !nameUser.isEmpty()) {
			Page<Bill> pageBill = billRepo.searchByNameUser(nameUser, pageable);
			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());
		} else if (name != null && userId != null && fromDate != null && toDate != null && !fromDate.trim().isEmpty()
				&& !toDate.trim().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Page<Bill> pageBill = billRepo.searchByAll(userId, sdf.parse(fromDate), sdf.parse(toDate), name, pageable);
			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());
		} else {
			Page<Bill> pageBill = billRepo.findAll(pageable);
			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("userId", userId == null ? "" : userId);
		model.addAttribute("nameUser", nameUser == null ? "" : nameUser);
		model.addAttribute("fromDate", fromDate == null ? "" : fromDate);
		model.addAttribute("toDate", toDate == null ? "" : toDate);
		List<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "bill/search";
	}

	public String thongKe(Model model) throws ParseException {
		List<Object[]> list = billRepo.thongKeTheoThang();
		List<ThongKe> thongKes = new ArrayList<ThongKe>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objects : list) {
				ThongKe thongKe = new ThongKe();
				thongKe.setSoLuong(Integer.parseInt((objects[0]).toString()));
				thongKe.setThang(Integer.parseInt((objects[1]).toString()));
				thongKes.add(thongKe);
			}
		}
		model.addAttribute("thongKes", thongKes);
		return "bill/thongke";
	}

	public String thongKe1(Model model) throws ParseException {
		List<Object[]> list = billRepo.thongKeTheoNguoiMua();
		List<ThongKeTheoNguoiMua> thongKeTheoNguoiMuas = new ArrayList<ThongKeTheoNguoiMua>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objects : list) {
				ThongKeTheoNguoiMua thongKeTheoNguoiMua = new ThongKeTheoNguoiMua();
				thongKeTheoNguoiMua.setSoLuong(Integer.parseInt((objects[0]).toString()));
				thongKeTheoNguoiMua.setNguoiMua(((objects[1]).toString()));
				thongKeTheoNguoiMuas.add(thongKeTheoNguoiMua);
			}
		}
		model.addAttribute("thongKeTheoNguoiMuas", thongKeTheoNguoiMuas);
		return "bill/thongKe1";
	}
}
