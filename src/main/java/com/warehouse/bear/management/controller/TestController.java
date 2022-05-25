package com.warehouse.bear.management.controller;


import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.services.WarehouseAuthService;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private WarehouseUserRepository userRepository;

	@Autowired
	private WarehouseAuthService warehouseAuthService;

	@Autowired
	private WarehouseMailUtil warehouseMailUtil;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/moderator")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@Autowired
	WarehouseImageUserRepository imageRepository;

	@GetMapping
	@Scheduled(cron = "0 17 22 * * *")
	public void getText() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Enabling scheduling...." +strDate);
		LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		List<WarehouseUser> users = userRepository.getAllWarehouseUserPasswordReminder(localDate);
		System.out.println("users: " + users.stream().map(WarehouseUser::getUserId).collect(Collectors.toList()));
		Map<String, Object> model = new HashMap<>();
		model.put("name", users.get(0).getFullname().toUpperCase());
		model.put("userId", users.get(0).getUserId().toUpperCase());
		model.put("days", 2);
		warehouseMailUtil.warehouseSendMail(users.get(0), model, WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_PASSWORD_REMINDER);
		users.stream().map(warehouseUser -> {

			String emailUser = warehouseUser.getEmail();
			// Have passwordReminder like yyyy-MM-dd, and want only the day
			int passwordReminderDayUser = new Integer(warehouseUser.getPasswordReminder().toString().split("-")[2]);
			int nowDay = new Integer(strDate.toString().split("-")[2]);
			int reminderDay = nowDay - passwordReminderDayUser;
			if(reminderDay == 0) {
				warehouseAuthService.activateOrDisabledUser(warehouseUser.getUserId());
			} else {
				Map<String, Object> modelg = new HashMap<>();
				model.put("name", warehouseUser.getFullname().toUpperCase());
				model.put("userId", warehouseUser.getUserId().toUpperCase());
				model.put("days", reminderDay);
				warehouseMailUtil.warehouseSendMail(warehouseUser, model, WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_PASSWORD_REMINDER);
			}
			return null;
		});
	}

}