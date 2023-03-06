package com.mk.manageuserpro;

import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;

@SpringBootApplication
public class ManageuserproApplication {
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ManageuserproApplication.class, args);

	}


}
