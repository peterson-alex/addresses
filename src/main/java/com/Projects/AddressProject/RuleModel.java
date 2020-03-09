package com.Projects.AddressProject;

import org.apache.tomcat.jni.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

public class RuleModel {

    // fields
    @Id
    public String id;

    public String country; // full name of country, aka 'United States'
    public List<AddressFieldModel> AddressFieldList;

    // getters, setters, constructors below

    // default constructor
    public RuleModel() {

    }

    // constructor
    public RuleModel(String country, List<AddressFieldModel> AddressFieldList) {
        this.country = country;
        this.AddressFieldList = AddressFieldList; // TODO - validate this before assigned
    }


}

