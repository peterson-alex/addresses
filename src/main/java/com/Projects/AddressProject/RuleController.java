package com.Projects.AddressProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RuleController {

    @Autowired
    private RuleRepository ruleRepository;

    // Returns the rule associated with the country name
    @GetMapping("/api/rule")
    public RuleModel getRuleModelByCountry(@RequestParam(value = "country") String country) {
        return ruleRepository.findByCountry(country);
    }

    // Retrieves every address rule in database
    @GetMapping("/api/rule/all")
    public List<RuleModel> getAllRuleModels() {
        return ruleRepository.findAll();
    }

    // Retrieves address rule for specified id
    @GetMapping("api/rule/id")
    public Optional<RuleModel> getRuleModelById(@RequestParam(value = "id") String id) {
        return ruleRepository.findById(id);
    }

    // Posts a new rule to the database
    @PostMapping("api/rule")
    public RuleModel createRule(@RequestBody RuleModel ruleModel)
    {
        ruleRepository.save(ruleModel);
        return ruleModel;
    }
}
