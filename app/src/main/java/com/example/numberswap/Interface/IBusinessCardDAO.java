package com.example.numberswap.Interface;

import java.util.ArrayList;
import java.util.Hashtable;

public interface IBusinessCardDAO {

    void addBusinessCard(Hashtable<String, String> attributes);

    void updateBusinessCard(Hashtable<String, String> attributes);

    void deleteBusinessCard(String id);

    Hashtable<String, String> getBusinessCard(String id);

    ArrayList<Hashtable<String, String>> getAllBusinessCards();


}
