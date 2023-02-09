package com.example.numberswap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Discoverer extends AppCompatActivity implements Adapter.DeviceInterface {

    public static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    public static final String SERVICE_ID = "6969";

    private ConnectionsClient connectionsClient;
    private Context context;

    Button button;
    TextView textView, nearbyDevices;
    ArrayList<Devices> devices = new ArrayList<>();
    ArrayList<Devices> selectedDevices = new ArrayList<>();

    private static String deviceName = "com.example.numberswap";

    CheckBox selectAllCheckBox;
    RecyclerView recyclerView;
    Adapter adapter;

    public Discoverer(Context context) {
        this.context = context;
        connectionsClient = Nearby.getConnectionsClient(context);
    }

    public Discoverer() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_devices);
        nearbyDevices = findViewById(R.id.nearbyDevices);
        selectAllCheckBox = findViewById(R.id.checkBox);
        recyclerView = findViewById(R.id.availableDevices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        button = findViewById(R.id.discoverBtn);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //function one or whatever
                startDiscovery();
            }
        });
        t1.start();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Advertiser advertiser = new Advertiser(Discoverer.this,"Hehe boi");
//                advertiser.startAdvertising();
//            }
//        });
        //startDiscovery();
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                final byte[] receivedBytes = payload.asBytes();

                String message = new String(receivedBytes, StandardCharsets.UTF_8);
//                textView.setText(message);
//                Log.d("moja", "Recieved  ");
                Toast.makeText(Discoverer.this, " " + message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s,
                                            @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                // Do something with is here...
                Log.d("moja", "PayLoad Sent noice ");
            }
        }
    };

    public void startDiscovery() {
        setDeviceName();
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(this)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Toast.makeText(this, "We're discovering!", Toast.LENGTH_SHORT).show();
                            //Log.d("moja", "We're discovering!");// We're discovering!

                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(this, "We're  unable to start discovering", Toast.LENGTH_SHORT).show();
                            //Log.d("moja", "We're  unable to start discovering\n" + e.getMessage());// We're unable to start discovering.
                        });
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {


                    // An endpoint was found. We request a connection to it.
                    if (devices == null) {
                        devices = new ArrayList<>();
                    }
                    Devices availableDevice = new Devices(endpointId, info.getEndpointName());
                    devices.add(availableDevice);
                    nearbyDevices.setText("" + devices.size() + " Devices Found");
                    adapter = new Adapter(devices, Discoverer.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    button.setOnClickListener(v ->
                    {
                        if (devices.size() == 0) {
                            Toast.makeText(Discoverer.this, "No Devices", Toast.LENGTH_SHORT).show();
                        } else {
                            if (selectedDevices.size() == 0) {
                                Toast.makeText(Discoverer.this, "No Device Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < selectedDevices.size(); i++) {
                                    Nearby.getConnectionsClient(Discoverer.this)
                                            //.requestConnection(deviceName, endpointId, connectionLifecycleCallback)
                                            .requestConnection(deviceName, selectedDevices.get(i).id, connectionLifecycleCallback)
                                            .addOnSuccessListener(
                                                    (Void unused) -> {
                                                        // We successfully requested a connection. Now both sides
                                                        // must accept before the connection is established.

                                                        Log.d("moja", "requested a connection");
                                                    })
                                            .addOnFailureListener(
                                                    (Exception e) -> {
                                                        Log.d("moja", "failed connection \n" + e.getMessage()); // Nearby Connections failed to request the connection.

                                                    });
                                }
                            }
                        }
                    });
                }

                @Override
                public void onEndpointLost(@NonNull String endpointId) {
                    // A previously discovered endpoint has gone away.
                    Log.d("moja", "onEndpointLost: ");
                   for(int i=0;i<devices.size();i++)
                   {
                       if(devices.get(i).id.equals(endpointId))
                       {
                           Toast.makeText(Discoverer.this, "Lost", Toast.LENGTH_SHORT).show();
                           devices.remove(i);
                           adapter.notifyDataSetChanged();
                           break;
                       }
                   }
                }
            };
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String endpointId, ConnectionInfo info) {
//
//                                            // The user confirmed, so we can accept the connection.
                                            Nearby.getConnectionsClient(Discoverer.this)
                                                    .acceptConnection(endpointId, mPayloadCallback);
//
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.d("moja", " id: "+endpointId);
//                            for(int i=0;i<selectedDevices.size();i++) {
                            sendPayLoad(endpointId, "noice");// We're connected! Can now start sending and receiving data.
//                                sendPayLoad(selectedDevices.get(i).id, "hehe boi");// We're connected! Can now start sending and receiving data.
//                            }
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            Toast.makeText(Discoverer.this, "Rejected", Toast.LENGTH_SHORT).show();
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            break;
                        default:
                            // Unknown status code
                    }
                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                }
            };

    public void sendPayLoad(final String endPointId, String data) {
        Payload bytesPayload = Payload.fromBytes(data.getBytes(StandardCharsets.UTF_8));
        Log.d("moja", " id to:  "+endPointId);
        Nearby.getConnectionsClient(Discoverer.this).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("moja", "Payload Sent Discover");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("moja", "onFailure : Failed to send Message");
            }
        });
    }

    @Override
    public void getCheckedDevices(Devices devicesList) {
        selectedDevices.add(devicesList);
        //Toast.makeText(Discoverer.this, "DISCOVER, Devices :  "+selectedDevices.size(), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void setDeviceName() {
        final BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        deviceName = (manager.getAdapter().getName());
    }
}