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
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String[] REQUIRED_PERMISSIONS;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    };
        }
    }

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    EditText text;
    Button send,receive;
    TextView receivedText;

    ArrayList<String> permissions;

//    private final int requestCodePermissions = 1001;
//
//    String [] askForPermissions =
//            {Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.BLUETOOTH,
//        Manifest.permission.BLUETOOTH_ADMIN,
//        Manifest.permission.ACCESS_WIFI_STATE,
//        Manifest.permission.CHANGE_WIFI_STATE,
//        Manifest.permission.ACCESS_FINE_LOCATION};

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.input);
        send = findViewById(R.id.button);
        receive = findViewById(R.id.button2);
        receivedText = findViewById(R.id.receivedText);

//        permissions = new ArrayList<>();
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        permissions.add(Manifest.permission.BLUETOOTH);
//        permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
//        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
//        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
       // permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);

//        statusCheck();
//        if(allPermissionsGranted())
//        {
//            Log.d("moja", "All Permissions are Granted!");
//            Intent intent = new Intent(this,Discoverer.class);
//           // startActivity(intent);
//        }
//        else
//        {
//            ActivityCompat.requestPermissions(this,askForPermissions,requestCodePermissions);
//            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
//        }
        //Intent intent = new Intent(this,Discoverer.class);
        //startActivity(intent);

        if (!hasPermissions(this, getRequiredPermissions())) {
            if (Build.VERSION.SDK_INT < 23) {
                ActivityCompat.requestPermissions(
                        this, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
            } else {
                requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
            }
        }



        send.setOnClickListener(v->
        {
            Log.d("moja", "clicked");
            Advertiser advertiser = new Advertiser(this,text.getText().toString());
            advertiser.startAdvertising();


        });
        receive.setOnClickListener(v->
        {
            Intent intent = new Intent(this,Discoverer.class);
            startActivity(intent);
        });
    }
    private boolean allPermissionsGranted()
    {
        for (String permission : permissions)
        {
            if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "False"+permission, Toast.LENGTH_LONG).show();
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
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            int i = 0;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Log.d("moja", "Failed to request the permission " + permissions[i]);
                    Toast.makeText(this, " "+ permissions[i]+" Not Granted", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                i++;
            }
            recreate();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}