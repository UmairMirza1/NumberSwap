package com.example.numberswap;

import static android.content.Context.BLUETOOTH_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Advertiser extends Activity {
    String message;
    ConnectionsClient connectionsClient;
    Context context;

    public static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    public static final String SERVICE_ID = "6969";
    private static String deviceName = "com.example.numberswap";

    ArrayList<Devices> listOfDevices = new ArrayList<>();

    public void setListOfDevices(ArrayList<Devices> listOfDevices) {
        this.listOfDevices = listOfDevices;
    }

    public Advertiser(Context context, String message) {
        this.context = context;
        connectionsClient = Nearby.getConnectionsClient(context);
        this.message = message;
    }

    public Advertiser(Context context, ArrayList<Devices> listOfDevices) {
        this.context = context;
        this.listOfDevices = listOfDevices;
        Toast.makeText(context, "Number of Devices : "+listOfDevices.size(), Toast.LENGTH_SHORT).show();
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            final byte[] receivedBytes = payload.asBytes();
            String message = new String(receivedBytes, StandardCharsets.UTF_8);
            Log.d("moja", "Recieved Message: " + message);
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
        }
    };

    public void startAdvertising() {

        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            context.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        }
        deviceName = (manager.getAdapter().getName());

        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        deviceName, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.d("moja", "We're advertising!");// We're advertising!
                            Toast.makeText(context, "We're advertising!", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(context, "We're advertising!", Toast.LENGTH_SHORT).show();
                            Log.d("moja", "We were unable to start advertising.\n"+e.getMessage());   // We were unable to start advertising.
                        });
    }
//    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
//            new EndpointDiscoveryCallback() {
//                @Override
//                public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
//                    // An endpoint was found. We request a connection to it.
//                    deviceNames.add(info.getEndpointName());
//                    Nearby.getConnectionsClient(context)
//                            .requestConnection(deviceName, endpointId, connectionLifecycleCallback)
//                            .addOnSuccessListener(
//                                    (Void unused) -> {
//                                        // We successfully requested a connection. Now both sides
//                                        // must accept before the connection is established.
//                                        Log.d("moja", "checking for end point");
//                                    })
//                            .addOnFailureListener(
//                                    (Exception e) -> {
//                                        // Nearby Connections failed to request the connection.
//                                        Log.d("moja", "Error checking for end point\n "+ e.getMessage());
//                                    });
//                }
//                @Override
//                public void onEndpointLost(@NonNull String endpointId) {
//                    // A previously discovered endpoint has gone away.
//                }
//            };
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                    new AlertDialog.Builder(context)
                            .setTitle("Accept connection to " + connectionInfo.getEndpointName())
                            .setMessage("Confirm the code matches on both devices: " + connectionInfo.getAuthenticationDigits())
                            .setPositiveButton(
                                    "Accept",
                                    (DialogInterface dialog, int which) ->
                                            // The user confirmed, so we can accept the connection.
                                            Nearby.getConnectionsClient(context)
                                                    .acceptConnection(endpointId, mPayloadCallback))
                            .setNegativeButton(
                                    android.R.string.cancel,
                                    (DialogInterface dialog, int which) ->
                                            // The user canceled, so we should reject the connection.
                                            Nearby.getConnectionsClient(context).rejectConnection(endpointId))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.
                            Log.d("moja", "Sending Payload");
                            sendPayLoad(endpointId,message);
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
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
                    Log.d("moja", "Disconnected");
                }
            };
    public void sendPayLoad(final String endPointId, String data) {
        Payload bytesPayload = Payload.fromBytes(data.getBytes(StandardCharsets.UTF_8));
        connectionsClient.sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("moja", "Payload Sent");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    private void checkForBluetooth()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(Advertiser.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

    }
}
