package com.Projects.AddressProject;


import com.sun.jna.WString;
import org.apache.lucene.analysis.CharArrayMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private AddressSearchRepository searchRepository;



    // Dependency injection
    public AddressController(AddressRepository addressRepository, RuleRepository ruleRepository,
                             AddressSearchRepository searchRepository) {
        this.addressRepository = addressRepository;
        this.ruleRepository = ruleRepository;
        this.searchRepository = searchRepository;
    }

    // returns the address specified by the id
    @GetMapping("/address/{id}")
    public Optional<AddressModel> getAddressById(@PathVariable String id) {
        return addressRepository.findById(id);
    }

    // Facilitates bulk posting of addresses
    @PostMapping("address/bulk")
    public boolean postBulkAddresses(@RequestBody AddressModelWrapper addressModelWrapper){

        List<AddressModel> validAddresses = new LinkedList<>();

        System.out.println("Now validating addresses... ");

        int validationCount = 0;
        for(AddressModel addressModel : addressModelWrapper.getAddressModelList()) {
            RuleModel ruleModel = ruleRepository.findByCountry(addressModel.country);

            // check if address is valid - if it is, add to list
            if (addressIsValid(addressModel, ruleModel)) {
                validAddresses.add(addressModel);
            }
            validationCount++;
            System.out.println("Validating " + validationCount);
        }

        System.out.println("Valid addresses: " + validAddresses.size());
        System.out.println("Now saving addresses to mongo ... ");

        // all addresses saved to mongo
        List<AddressModel> postedAddressList = addressRepository.saveAll(validAddresses);
        System.out.println("Finished saving addresses to Mongo!");

        List<SearchModel> searchModelList = new LinkedList<>();
        System.out.println("Now creating search models ... ");

        // obtain all addresses to save to Elasticsearch
        int transformCount = 0;
        for(AddressModel postedAddress : postedAddressList) {
            // get mongoDBID and full address
            SearchModel searchModel = new SearchModel(postedAddress.id,
                    postedAddress.country,
                    getFullAddress(postedAddress));

            searchModelList.add(searchModel);
            transformCount++;
            System.out.println("Transforming " + transformCount);
        }

        System.out.println("Finished creating search models!");
        System.out.println("Now inserting search models to elasticsearch ... ");

        searchRepository.saveAll(searchModelList);
        System.out.println("Finished saving search models to elasticsearch!");

        return true;
    }

    // posts a single address to the service.
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
            SearchModel searchModel = new SearchModel(mongoID,
                    postedAddressModel.country,
                    fullAddress);

            // forward address search model to elasticsearch repository
            searchRepository.save(searchModel);


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

        int length = addressModel.Address.size();
        int count = 1;
        for (Map.Entry<String, Object> entry : addressModel.Address.entrySet()) {
            String s = (String)entry.getValue();

            // add to search string if s not null, empty, or blank
            if (s != null && !s.isEmpty() && !s.isBlank()) {
                stringBuilder.append(s);

                // only add space if not last term
                if (count < length) {
                    stringBuilder.append(" ");
                }
            }
            count++;
        }

        return stringBuilder.toString();
    }
}
