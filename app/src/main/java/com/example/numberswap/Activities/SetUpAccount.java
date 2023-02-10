package com.example.numberswap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.numberswap.R;

public class SetUpAccount extends AppCompatActivity {

    ImageView addAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        addAccount = findViewById(R.id.addAccount);
        addAccount.setOnClickListener(v->
        {
            Intent intent = new Intent(SetUpAccount.this,CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}