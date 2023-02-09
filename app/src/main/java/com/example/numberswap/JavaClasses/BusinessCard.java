package com.example.numberswap.JavaClasses;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

public class BusinessCard implements Serializable {

    private String Email;
    private String phoneNumber;
    private String DateOfBirth;

    private String FullName;

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    private Bitmap Image;

    //private ImageView imageView;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

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
