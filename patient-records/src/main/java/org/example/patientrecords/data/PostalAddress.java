package org.example.patientrecords.data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PostalAddress implements Serializable {

    private String street;
    private String number;
    private String specifier;
    private String postalCode;
    private String postOffice;
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSpecifier() {
        return specifier;
    }

    public void setSpecifier(String specified) {
        this.specifier = specified;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
