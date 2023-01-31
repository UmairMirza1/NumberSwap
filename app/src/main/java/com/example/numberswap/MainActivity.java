package com.example.numberswap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText text;
    Button send,receive;
    TextView receivedText;

    ArrayList<String> permissions;

    private final int requestCodePermissions = 1001;

    String [] askForPermissions =
            {Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.input);
        send = findViewById(R.id.button);
        receive = findViewById(R.id.button2);
        receivedText = findViewById(R.id.receivedText);

        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.BLUETOOTH);
        permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        statusCheck();
        if(allPermissionsGranted())
        {
            Log.d("moja", "All Permissions are Granted!");
        }
        else
        {
            ActivityCompat.requestPermissions(this,askForPermissions,requestCodePermissions);
        }

        send.setOnClickListener(v->
        {
            Advertiser advertiser = new Advertiser(this,text.getText().toString());
            advertiser.startAdvertising();


        });
        receive.setOnClickListener(v->
        {
            Discoverer discoverer = new Discoverer(this);
            discoverer.startDiscovery();
            discoverer.setTextView(receivedText);
        });
    }
    private boolean allPermissionsGranted()
    {
        for (String permission : permissions)
        {
            if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Turn it on to start Discovering")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}