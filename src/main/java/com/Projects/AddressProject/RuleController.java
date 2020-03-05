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

    // Returns rule associated with iso2
    @GetMapping("api/rule")
    public RuleModel getRuleModelByISO2(@RequestParam(value = "iso2") String iso2) {
        return ruleRepository.findByISO2(iso2);
    }

    // Returns the rule associated with iso3
    @GetMapping("api/rule")
    public RuleModel getRuleModelByISO3(@RequestParam(value = "iso3") String iso3) {
        return ruleRepository.findByISO3(iso3);
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
