package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="address_search")
public class AddressSearchModel {

    @Id
    public String id;

    // fields
    public String mongoDBId;
    public String country;
    public String ISO2;
    public String ISO3;
    public String fullAddress; // full text of address except for country

    // constructor, getters and setters below ...

    // default constructor
    public AddressSearchModel() {

    }

    // constructor
    public AddressSearchModel(String mongoDBId, String country, String ISO2, String ISO3, String fullAddress) {
        this.mongoDBId = mongoDBId;
        this.country = country;
        this.ISO2 = ISO2;
        this.ISO3 = ISO3;
        this.fullAddress = fullAddress;
    }
}
