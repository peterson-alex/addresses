package com.Projects.AddressProject;


import com.sun.jna.WString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private AddressSearchRepository addressSearchRepository;

    // Dependency injection
    public AddressController(AddressRepository addressRepository, RuleRepository ruleRepository,
                             AddressSearchRepository addressSearchRepository) {
        this.addressRepository = addressRepository;
        this.ruleRepository = ruleRepository;
        this.addressSearchRepository = addressSearchRepository;
    }

    // returns the address specified by the id
    @GetMapping("/address/{id}")
    public Optional<AddressModel> getAddressById(@PathVariable String id) {
        return addressRepository.findById(id);
    }

    // posts an address to the service.
    @PostMapping("/address")
    public AddressModel postNewAddress(@RequestBody AddressModel addressModel) {

        // get rule model for that country
        RuleModel ruleModel = ruleRepository.findByCountry(addressModel.country);
        System.out.println("Rule model for country " + ruleModel.country + " selected");

        // if address is valid, post to database elasticsearch
        if (addressIsValid(addressModel, ruleModel)) {

            // post address to address repository
            AddressModel postedAddressModel = addressRepository.save(addressModel);

            // get mongoDB id and full address from postedAddressModel
            String mongoID = postedAddressModel.id;
            String fullAddress = getFullAddress(postedAddressModel);

            // create AddressSearchModel
            AddressSearchModel addressSearchModel = new AddressSearchModel(mongoID,
                    postedAddressModel.country,postedAddressModel.ISO2, postedAddressModel.ISO3,
                    fullAddress);

            // forward address search model to elasticsearch repository
            addressSearchRepository.save(addressSearchModel);

            return postedAddressModel;
        }

        return addressModel;
    }

    // Ensure that address is valid according to address rules
    private boolean addressIsValid(AddressModel addressModel, RuleModel ruleModel) {

        System.out.println("Inside address checker");


        // check country
        if (!addressModel.country.equals(ruleModel.country)) {
            return false;
        }
        System.out.println("Country valid!");

        // check iso2
        if (!addressModel.ISO2.equals(ruleModel.ISO2)) {
            System.out.println(ruleModel.ISO2);
            System.out.println(addressModel.ISO2);
            System.out.println("ISO2 not valid!");
            return false;
        }
        System.out.println("ISO2 valid!");

        // check iso3
        if (!addressModel.ISO3.equals(ruleModel.ISO3)) {
            return false;
        }
        System.out.println("ISO3 valid!");

        // loop through address fields of address model and validate each
        for (Map.Entry<String, Object> field : addressModel.Address.entrySet()) {

            // check if field name in rule model
            if (!fieldInRuleModel(field.getKey(), ruleModel)) {
                System.out.println("Field name not in rule model!");
                return false;
            }
            System.out.println("Field name in rule model!");

            // get regex pattern
            String regex = getFieldRegexPattern(field.getKey(), ruleModel);
            System.out.println(regex);

            // check if field matches regex in rule model
            if (!Pattern.compile(regex).matcher((String)field.getValue()).matches()) {
                System.out.println((String)field.getValue());
                System.out.println("Regex does not match!");
                return false;
            }
            System.out.println("Regex matches");

        }

        // loop through address fields of rule model. If field required,
        // check that field is in address model and not null
        for (AddressFieldModel fieldModel : ruleModel.AddressFieldList) {
            if (fieldModel.required) {
                if (!addressModel.Address.containsKey(fieldModel.name)){
                    System.out.println("Address does not contain required field " + fieldModel.name);
                    return false;
                }
                System.out.println("Address contains required field " + fieldModel.name);

                // value must be present - cannot be null
                if (addressModel.Address.get(fieldModel.name) == null) {
                    System.out.println("Address field " + fieldModel.name +  " is null!");
                    return false;
                }
                System.out.println("Address field " + fieldModel.name +  " is not null!");
            }
        }
        System.out.println("All checks pass, address is valid!");

        // all checks passed
        return true;
    }

    // check if field name is present in rule model
    private boolean fieldInRuleModel(String field, RuleModel ruleModel) {

        for (AddressFieldModel fieldModel : ruleModel.AddressFieldList) {
            if (fieldModel.name.equals(field)) {
                return true;
            }
        }
        return false;
    }

    // returns the regex pattern for the given field
    private String getFieldRegexPattern(String field, RuleModel ruleModel) {
        for (AddressFieldModel fieldModel : ruleModel.AddressFieldList) {
            if (fieldModel.name.equals(field)) {
                return fieldModel.format;
            }
        }

        // field not in rulemodel
        return null;
    }

    private String getFullAddress(AddressModel addressModel) {

        // concatenate all strings
        StringBuilder stringBuilder = new StringBuilder();

        // iterate over address fields and add to search query
        for (Map.Entry<String, Object> entry : addressModel.Address.entrySet()) {
            String s = (String)entry.getValue();

            // add to search string if s not null, empty, or blank
            if (s != null && !s.isEmpty() && !s.isBlank()) {
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}
