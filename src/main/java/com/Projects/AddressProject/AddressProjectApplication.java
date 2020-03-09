package com.Projects.AddressProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AddressProjectApplication {

	@Autowired private RuleRepository ruleRepository;
	@Autowired private AddressRepository addressRepository;
	// @Autowired private AddressSearchRepository searchRepository;

	public static void main(String[] args) {

		// ApplicationContext context = new AnnotationConfigApplicationContext(RestClientConfig.class);

		SpringApplication.run(AddressProjectApplication.class, args);
	}

}
