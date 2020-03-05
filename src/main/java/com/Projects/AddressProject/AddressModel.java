package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;
import java.util.Map;

public class AddressModel {

    // fields
    @Id
    private String id;

    private String country;
    private String iso2;
    private String iso3;
    private Map<String, Object> Address; // all address fields except for country

    // Default constructor
    public AddressModel(){

    }

    // Constructor
    public AddressModel(String country, String iso2, String iso3, Map<String, Object> Address) {
        this.country = country;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.Address = Address;
    }

}
