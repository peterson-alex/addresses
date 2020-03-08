package com.Projects.AddressProject;

// Nested class used to model the fields
public class AddressFieldModel {

    // fields
    public String name; // name of field
    public String format; // regex expression
    public boolean required; // true if field

    // default constructor
    public AddressFieldModel() {
    }

    public AddressFieldModel(String name, String format, boolean required) {
        this.name = name;
        this.format = format;
        this.required = required;
    }
}
