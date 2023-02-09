package com.example.numberswap.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.numberswap.Interface.IBusinessCardDAO;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class BusinessCardDB implements IBusinessCardDAO {

    private final Context Context;

    private BusinessCardDbHelper BusinessCardDBHelper;

    public BusinessCardDB(Context context) {
         this.Context = context;
        BusinessCardDBHelper = new BusinessCardDbHelper(context);
    }


    @Override
    public void addBusinessCard(Hashtable<String, String> attributes) {
        SQLiteDatabase db = BusinessCardDBHelper.getWritableDatabase();
        ContentValues content = new ContentValues();

        Enumeration<String> keys = attributes.keys();

        // TODO: Fix for image

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if(!key.equals("image")) {
                content.put(key, attributes.get(key));
            }
            else {
                content.put(key, attributes.get(key));
            }

        }

        long result = db.insert("BusinessCard", null, content);
       // System.out.println(result == -1 ? "Business Card Not Added" : "Business Card Added");
        Toast.makeText(Context.getApplicationContext(), result == -1 ? "Business Card Not Added" : "Business Card Added", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateBusinessCard(Hashtable<String, String> attributes) {
        SQLiteDatabase db = BusinessCardDBHelper.getWritableDatabase();
        ContentValues content = new ContentValues();

        Enumeration<String> keys = attributes.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            content.put(key, attributes.get(key));
        }

        String[] arguments = new String[1];
        attributes.get("id");
       long result= db.update("BusinessCard", content, "id = ?", arguments);
       //system.out.println(res == -1 ? "Business Card Not Added" : "Business Card Added");
       Toast.makeText(Context.getApplicationContext(), result == -1 ? "Business Card Not Added" : "Business Card Added", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void deleteBusinessCard(String id) {

        String[] arguments = new String[1];
        SQLiteDatabase db = BusinessCardDBHelper.getWritableDatabase();
        long result = db.delete("BusinessCard", "id = ?", arguments);
        Toast.makeText(Context.getApplicationContext(), result == -1 ? "Business Card Not Added" : "Business Card Added", Toast.LENGTH_SHORT).show();



    }

    @SuppressLint("Range")
    @Override
    public Hashtable<String, String> getBusinessCard(String id) {

        SQLiteDatabase db = BusinessCardDBHelper.getReadableDatabase();

        String query = "SELECT * FROM BusinessCard WHERE id = ?";
        String[] arguments = new String[1];
        arguments[0] = id;
        Cursor cursor = db.rawQuery(query,arguments);

        Hashtable<String,String> obj = new Hashtable<String, String>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();

            for(String col : columns){
                obj.put(col.toLowerCase(),cursor.getString(cursor.getColumnIndex(col)));
            }
        }

        return obj;




    }

    @SuppressLint("Range")
    @Override
    public ArrayList<Hashtable<String, String>> getAllBusinessCards() {


        SQLiteDatabase db = BusinessCardDBHelper.getReadableDatabase();
        String query = "SELECT * FROM BusinessCard ";
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Hashtable<String,String>> objects = new ArrayList<Hashtable<String, String>>();
        while(cursor.moveToNext()){

            Hashtable<String,String> obj = new Hashtable<String, String>();

            String [] columns = cursor.getColumnNames();

            for(String col : columns){

                obj.put(col.toLowerCase(),cursor.getString(cursor.getColumnIndex(col)));}

            objects.add(obj);
        }

        return objects;
    }
}
