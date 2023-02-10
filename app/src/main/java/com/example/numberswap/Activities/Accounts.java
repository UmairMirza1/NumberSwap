package com.example.numberswap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import com.example.numberswap.DB.BusinessCardDB;
import com.example.numberswap.Interface.IBusinessCardDAO;
import com.example.numberswap.R;

public class Accounts extends AppCompatActivity {

    RecyclerView recyclerView;
    Button receive,addAccount;
    IBusinessCardDAO dbInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);

        //RECYCLERVIEW CLASS
        dbInterface = new BusinessCardDB(Accounts.this);

        recyclerView = findViewById(R.id.accountRecyclerView);
        receive = findViewById(R.id.receive);
        addAccount = findViewById(R.id.addAcc);
        getAccounts();


        addAccount.setOnClickListener(v->{
            Intent intent = new Intent(Accounts.this,CreateAccountActivity.class);
            startActivity(intent);
        });


    }
    private void getAccounts()
    {
        Cursor cursor = dbInterface.loadCards();
        if(cursor.getCount() == 0)
        {
           Intent intent = new Intent(Accounts.this,SetUpAccount.class);
           startActivity(intent);
        }
    }
}