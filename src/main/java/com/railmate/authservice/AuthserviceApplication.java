package com.railmate.authservice;

import com.railmate.authservice.model.*;
import com.railmate.authservice.repository.RoleRepository;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

	/*  FIX  use new RoleName enum & single-arg constructor. */
//	@Bean
//	CommandLineRunner initRoles(RoleRepository roleRepo) {
//		return args -> {
//			for (RoleName rn : RoleName.values()) {
//				roleRepo.findByName(rn)
//						.orElseGet(() -> roleRepo.save(new Role(rn)));
//			}
//		};
//	}
}
