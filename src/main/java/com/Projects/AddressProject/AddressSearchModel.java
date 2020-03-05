package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;

/**Used to map to Elasticsearch entries **/
public class AddressSearchModel {

    @Id
    private String id;

    // fields
    private String mongoDBId;
    private String country;
    private String ISO2;
    private String ISO3;
    private String fullAddress; // full text of address except for country

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
