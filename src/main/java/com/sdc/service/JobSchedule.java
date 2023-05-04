package com.sdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sdc.repository.BillRepo;

@Component
public class JobSchedule {
	@Autowired
	BillRepo billRepo;

	@Scheduled(fixedDelay = 5000)
	public void searchByDate() {
		int size = billRepo.findAll().size();
		System.out.println("Co don hang moi" + size);
		// Date date = billRepo.searchByDate(date).date();
//		Date buyDate = new Date();
//		long timeMilli = buyDate.getTime();
//		Date searchByDate = new Date(timeMilli - (300000));
//		System.out.println("bill: " + timeMilli);
	}
}
