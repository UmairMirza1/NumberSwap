package com.example.numberswap.JavaClasses;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.numberswap.Interface.IBusinessCardDAO;
import com.example.numberswap.Util.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class BusinessCard implements Serializable {

    private String Email;
    private String phoneNumber;
    private String DateOfBirth;

    private String FullName;

    private Bitmap image;

    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public IBusinessCardDAO getDao() {
        return dao;
    }

    private IBusinessCardDAO dao;

    public void setDao(IBusinessCardDAO dao) {
        this.dao = dao;
    }

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

    public void save(){
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("id", Integer.toString(Id));
        attributes.put("email", Email);
        attributes.put("phoneNumber", phoneNumber);
        attributes.put("dateOfBirth", DateOfBirth);
        attributes.put("fullName", FullName);
        attributes.put("image", Utility.getStringFromImage(image));
        dao.addBusinessCard(attributes);
    }

    public void LoadAll(){

        ArrayList<Hashtable<String,String>> AllCards = dao.getAllBusinessCards();



    }
}
