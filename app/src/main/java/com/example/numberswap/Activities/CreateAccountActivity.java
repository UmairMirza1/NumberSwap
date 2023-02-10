package com.example.numberswap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.numberswap.R;
import com.hbb20.CountryCodePicker;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email, phone, name,dob;
    ImageView displayPicture;
    CountryCodePicker countryCodePicker;
    String selectedCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress2);
        phone = findViewById(R.id.personPhone);
        dob = findViewById(R.id.editTextDOB);
        displayPicture = findViewById(R.id.displayPicture);
        countryCodePicker = findViewById(R.id.ccp);
        selectedCode = countryCodePicker.getSelectedCountryCode();


//        setViews();
//        setListeners();
    }

    private void setListeners() {

    }

    private void setViews() {
    }

}