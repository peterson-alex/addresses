package com.Projects.AddressProject;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RuleController {

    private RuleRepository ruleRepository;

    // Returns the rule associated with the country name
    @GetMapping("/api/rule")
    public RuleModel getRuleModel(@RequestParam(value = "country") String country) {
        return ruleRepository.findByCountryName(country);
    }

    @GetMapping("/api/rule")
    public List<RuleModel> getAllRuleModels() {
        return ruleRepository.findAll();
    }

    @GetMapping("api/rule/id")
    public Optional<RuleModel> getRuleModelById(@RequestParam(value = "id") String id) {
        return ruleRepository.findById(id);
    }

    @PostMapping("api/rule")
    public RuleModel createRule(@RequestBody RuleModel ruleModel)
    {
        ruleRepository.save(ruleModel);
        return ruleModel;
    }
}
