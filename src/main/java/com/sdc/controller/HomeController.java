package com.sdc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sdc.service.MailService;

@Controller
public class HomeController {
	@Autowired
	MailService mailService;

	@GetMapping("/home")
	public String home() {
		mailService.sendEmail("test@gmail.com", "test thoi", "test thoi");
		return "home.html";
	}

	@GetMapping("/register")
	public String register(HttpServletRequest request) {
		return "customer/register";
	}
}
