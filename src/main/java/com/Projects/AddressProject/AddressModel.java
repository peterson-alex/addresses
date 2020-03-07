package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;

import java.util.LinkedHashMap;
import java.util.Map;

public class AddressModel {

    // fields
    @Id
    public String id;

    public String country;
    public String ISO2;
    public String ISO3;
    public LinkedHashMap<String, Object> Address; // all address fields except for country

    // getters, setters, constructor below ...

    // Default constructor
    public AddressModel(){

    }

    // Constructor
    public AddressModel(String country, String iso2, String iso3, LinkedHashMap<String, Object> Address) {
        this.country = country;
        this.ISO2 = iso2;
        this.ISO3 = iso3;
        this.Address = Address;
    }

}
