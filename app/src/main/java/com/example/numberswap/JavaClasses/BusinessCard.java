package com.example.numberswap.JavaClasses;

import java.io.Serializable;

public class BusinessCard implements Serializable {

    private String Email;
    private String phoneNumber;
    private String DateOfBirth;


    public BusinessCard(String email, String phoneNumber, String dateOfBirth) {
        Email = email;
        this.phoneNumber = phoneNumber;
        DateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }
}
