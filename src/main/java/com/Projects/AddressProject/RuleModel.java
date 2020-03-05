package com.Projects.AddressProject;

import org.apache.tomcat.jni.Address;
import org.springframework.data.annotation.Id;

import java.util.List;

public class RuleModel {

    // fields
    @Id
    public String id;

    public String country; // full name of country, aka 'United States'
    public String ISO2; // two-letter country code, aka 'US'
    public String ISO3; // three letter country code, aka 'USA'
    public List<AddressFieldModel> AddressFieldList;

    // default constructor
    public RuleModel() {

    }

    // constructor
    public RuleModel(String country, String iso2, String iso3, List<AddressFieldModel> AddressFieldList) {
        this.country = country;
        this.ISO2 = iso2;
        this.ISO3 = iso3;
        this.AddressFieldList = AddressFieldList; // TODO - validate this before assigned
    }

    // Nested class used to model the fields
    public class AddressFieldModel {

        // fields
        public String Name; // name of field
        public String Format; // a regex expression used to determine whether field is correct format or not
        public boolean required; // true if field

        // default constructor
        public AddressFieldModel() {
        }
    }
}

