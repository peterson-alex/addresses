package com.Projects.AddressProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AddressProjectApplication {

	@Autowired
	private RuleRepository ruleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AddressProjectApplication.class, args);
	}

}
