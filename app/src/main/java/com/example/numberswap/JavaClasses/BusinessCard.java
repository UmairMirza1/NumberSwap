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
    private IBusinessCardDAO dao;

    public BusinessCard(String email, String phoneNumber, String dateOfBirth) {
        Email = email;
        this.phoneNumber = phoneNumber;
        DateOfBirth = dateOfBirth;
    }

    public BusinessCard( IBusinessCardDAO dao ){
        Email = "";
        phoneNumber = "";
        DateOfBirth = "";
        FullName = "";
        image = null;
        Id = 0;
        this.dao = dao;

    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }

    public IBusinessCardDAO getDao() {
        return dao;
    }
    public void setDao(IBusinessCardDAO dao) {
        this.dao = dao;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }


    public String getFullName() {
        return FullName;
    }
    public void setFullName(String fullName) {
        FullName = fullName;
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
       // attributes.put("id", Integer.toString(Id));
        attributes.put("name", FullName);
        attributes.put("email", Email);
        attributes.put("phone", phoneNumber);
        attributes.put("DOB", DateOfBirth);
        attributes.put("image", Utility.getStringFromImage(image));
        dao.addBusinessCard(attributes);
    }

    public  ArrayList<BusinessCard> LoadAllCards(){
        ArrayList<Hashtable<String,String>> AllCards = dao.getAllBusinessCards();

        ArrayList<BusinessCard> AllBusinessCards = new ArrayList<>();
        for ( Hashtable<String,String> card : AllCards){
            BusinessCard businessCard = new BusinessCard(card.get("email"), card.get("phone"), card.get("DOB"));
            businessCard.setFullName(card.get("name"));
            businessCard.setImage(Utility.getImageFromString(card.get("image")));
            businessCard.setDao(dao);
            AllBusinessCards.add(businessCard);
        }
        return AllBusinessCards;

    }
}
