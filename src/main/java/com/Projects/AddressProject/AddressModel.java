package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;

import java.util.LinkedHashMap;
import java.util.Map;

public class AddressModel {

    // fields
    @Id
    public String id;

    public String country;
    public LinkedHashMap<String, Object> Address; // all address fields except for country

    // getters, setters, constructor below ...

    // Default constructor
    public AddressModel(){

    }

    // Constructor
    public AddressModel(String country, LinkedHashMap<String, Object> Address) {
        this.country = country;
        this.Address = Address;
    }

}
