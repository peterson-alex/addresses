package com.Projects.AddressProject;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "rule", path = "rule")
public interface RuleRepository extends MongoRepository<RuleModel, String> {

    public RuleModel findByCountry(@Param("country") String country);
    public List<RuleModel> getAllRules(); // retrieves all rules from database

}
