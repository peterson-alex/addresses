package com.Projects.AddressProject;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface RuleRepository extends MongoRepository<RuleModel, String> {

    public RuleModel findByCountryName(String countryName);
    public List<RuleModel> getAllRules(); // retrieves all rules from database

}
