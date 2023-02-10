package com.example.numberswap.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.numberswap.R;
import com.hbb20.CountryCodePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

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

        dob.setOnClickListener(v->
        {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
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