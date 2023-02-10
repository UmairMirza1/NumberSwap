package com.example.numberswap.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.numberswap.R;
import com.hbb20.CountryCodePicker;

import java.io.FileNotFoundException;
import java.io.IOException;

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

        displayPicture.setOnClickListener(v->
        {
           imageChooser();
        });
//        setViews();
//        setListeners();
    }

    private void setListeners() {

    }

    private void setViews() {
    }
    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        displayPicture.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
}