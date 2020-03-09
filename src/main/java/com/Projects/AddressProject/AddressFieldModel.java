package com.Projects.AddressProject;

// Nested class used to model the fields
public class AddressFieldModel {

    // fields
    public String name; // name of field
    public String format; // regex expression
    public boolean discreteField; // true if field has discrete set of values ("AL", AR", ... )
    public boolean required; // true if field required

    // default constructor
    public AddressFieldModel() {
    }

    public AddressFieldModel(String name, String format, boolean discreteField, boolean required) {
        this.name = name;
        this.format = format;
        this.discreteField = discreteField;
        this.required = required;
    }
}
