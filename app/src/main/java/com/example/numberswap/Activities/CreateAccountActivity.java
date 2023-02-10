package com.example.numberswap.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numberswap.DB.BusinessCardDB;
import com.example.numberswap.Interface.IBusinessCardDAO;
import com.example.numberswap.JavaClasses.BusinessCard;
import com.example.numberswap.R;
import com.hbb20.CountryCodePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email, phone, name;
    TextView dob;
    ImageView displayPicture;
    CountryCodePicker countryCodePicker;
    String selectedCode;
    Button create,create1;
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
        create = findViewById(R.id.create);
        create1 = findViewById(R.id.create1);

        displayPicture.setOnClickListener(v->
        {
           imageChooser();
        });

        dob.setOnClickListener(v->
        {
         pickDate();
        });

        create.setOnClickListener(v->
        {
            if(TextUtils.isEmpty(name.getText().toString())) {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(phone.getText().toString())) {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(email.getText().toString())) {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }
            else if(displayPicture.getDrawable() == null)
            {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }
            else
            {
              create.setVisibility(View.GONE);
              create1.setVisibility(View.VISIBLE);
            }

        });
        create1.setOnClickListener(v->
        {
            BusinessCard businessCard = new BusinessCard(email.getText().toString(),
                    phone.getText().toString(),
                    dob.getText().toString());
            IBusinessCardDAO db = new BusinessCardDB(this);
            businessCard.setDao(db);
            Bitmap bm=((BitmapDrawable)displayPicture.getDrawable()).getBitmap();
            businessCard.setImage(bm);
            businessCard.setFullName(name.getText().toString());
            businessCard.save();

        });
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
    private void pickDate()
    {
        final Calendar c = Calendar.getInstance();
        int years = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // on below line we are setting date to our text view.
                if(years<year)
                {
                    Toast.makeText(CreateAccountActivity.this, "Year Can't be Greater than Current Year!", Toast.LENGTH_SHORT).show();
                }
                else if(month<(monthOfYear) && year==years)
                {
                    Toast.makeText(CreateAccountActivity.this, "Month can't be Greater than Current Month!", Toast.LENGTH_SHORT).show();
                }
                else if(day<dayOfMonth && (month == monthOfYear) && year == years)
                {
                    Toast.makeText(CreateAccountActivity.this, "Date Can't be Greater Than Today's Date", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dob.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                }

            }
        },years, month, day);
        datePickerDialog.show();
    }
    private boolean isFilled() {
            if(TextUtils.isEmpty(name.getText().toString())) {
            return false;
        }
            if(TextUtils.isEmpty(phone.getText().toString())) {
            return false;
        }
            if(TextUtils.isEmpty(email.getText().toString())) {
            return false;
        }
        if(displayPicture.getDrawable() == null)
        {
            return false;
        }

        return true;
    }
}