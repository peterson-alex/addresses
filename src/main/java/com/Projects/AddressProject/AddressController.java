package com.Projects.AddressProject;


import com.sun.jna.WString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<AddressModel> getAddressById(@RequestParam(value = "id") String id) {
        return addressRepository.findById(id);
    }

    // posts an address to the service.
    @PostMapping("/address")
    public AddressModel postNewAddress(@RequestBody AddressModel addressModel) {

        // get rule model for that country
        RuleModel ruleModel = ruleRepository.findByCountry(addressModel.country);

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

    //
    private boolean addressIsValid(AddressModel addressModel, RuleModel ruleModel) {
        // TODO - need to implement this method
        return true;
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
