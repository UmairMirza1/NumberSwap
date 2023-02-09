package com.example.numberswap.DB;

import android.content.Context;

import com.example.numberswap.Interface.IBusinessCardDAO;

public class BusinessCardDB implements IBusinessCardDAO {

    private Context context;

    public BusinessCardDB(Context context) {
        this.context = context;
    }


}
