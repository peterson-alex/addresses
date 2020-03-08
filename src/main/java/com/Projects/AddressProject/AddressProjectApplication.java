package com.Projects.AddressProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class AddressProjectApplication {

	@Autowired private RuleRepository ruleRepository;
	@Autowired private AddressRepository addressRepository;

	public static void main(String[] args) {

		SpringApplication.run(AddressProjectApplication.class, args);
	}

}
