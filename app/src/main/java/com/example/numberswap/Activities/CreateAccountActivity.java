package com.example.numberswap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.numberswap.R;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email, phone, password, confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setViews();
        setListeners();
    }

    private void setListeners() {

    }

    private void setViews() {
    }

}