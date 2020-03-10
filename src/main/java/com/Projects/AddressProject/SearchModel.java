package com.Projects.AddressProject;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="address_search")
public class SearchModel {

    @Id
    public String id;

    // fields
    public String mongoDBId;

    // @Field(type= FieldType.Keyword)
    public String country;

    public String fullAddress; // full text of address except for country

    // constructor, getters and setters below ...

    // default constructor
    public SearchModel() {

    }

    // constructor
    public SearchModel(String mongoDBId, String country, String fullAddress) {
        this.mongoDBId = mongoDBId;
        this.country = country;
        this.fullAddress = fullAddress;
    }
}
