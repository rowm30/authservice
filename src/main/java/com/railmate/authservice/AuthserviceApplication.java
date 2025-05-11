package com.railmate.authservice;

import com.railmate.authservice.model.Role;
import com.railmate.authservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepo) {
		return args -> {
			List<String> defaultRoles = List.of("USER", "ADMIN");

			for (String roleName : defaultRoles) {
				roleRepo.findByName(roleName)
						.orElseGet(() -> roleRepo.save(new Role(null, roleName)));
			}
		};
	}

}
