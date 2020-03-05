package com.Projects.AddressProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.*;

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

    // Retrieves the address rule for specified iso2
    @GetMapping("api/rule/iso2")
    public RuleModel getRuleModelByISO2(@RequestParam(value = "iso2") String iso2) {
        return ruleRepository.findByISO2(iso2);
    }

    // Retrieves the address rule for specified iso3
    @GetMapping("api/rule/iso3")
    public RuleModel getRuleModelByISO3(@RequestParam(value = "iso3") String iso3) {
        return ruleRepository.findByISO3(iso3);
    }

    // Posts a new rule to the database
    @PostMapping("api/rule")
    public RuleModel createRule(@RequestBody RuleModel ruleModel)
    {
        if (ruleModelIsValid(ruleModel)){
            return ruleRepository.save(ruleModel);
        }
        return ruleModel;
    }

    // Posts a list of rules to the database
    @PostMapping("api/rules")
    public List<RuleModel> createManyRules(@RequestBody List<RuleModel> ruleModelList) {
        return ruleRepository.saveAll(ruleModelList);
    }

    // Address Rule checker. Rules must be valid to be inserted
    // into database.
    private boolean ruleModelIsValid(RuleModel ruleModel) {
        // TODO - check that country, iso2, iso3 consistent

        // check that at least one field in ruleModel.addressfields
        if (ruleModel.AddressFieldList.size() < 1) {
            return false;
        }

        // check name, format, and required fields
        int numRequiredFields = 0;
        for (RuleModel.AddressFieldModel field : ruleModel.AddressFieldList) {

            // check that name is not null
            if (field.Name == null) {
                return false;
            }

            // check that name is not blank
            if (field.Name.isBlank()) {
                return false;
            }

            // check that name is not empty
            if (field.Name.isEmpty()) {
                return false;
            }

            // check if regex is valid
            try {
                Pattern.compile(field.Format);
            } catch (PatternSyntaxException ex) {
                return false;
            }

            if (field.required) {
                numRequiredFields++;
            }
        }

        // at least one field must be required
        if (numRequiredFields < 1) {
            return false;
        }

        // inputs of rule model are valid
        return true;
    }

}
