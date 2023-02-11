package com.example.numberswap.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numberswap.Adapter.Adapter;
import com.example.numberswap.JavaClasses.Devices;
import com.example.numberswap.R;
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

public class DiscoverFragment extends Fragment implements Adapter.DeviceInterface {
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

    public DiscoverFragment() {

    }
    public DiscoverFragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_nearby_devices, container, false);
        nearbyDevices = view.findViewById(R.id.nearbyDevices);
        selectAllCheckBox = view.findViewById(R.id.checkBox);
        recyclerView = view.findViewById(R.id.availableDevices);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        button = view.findViewById(R.id.discoverBtn);
        startDiscovery();

        return view;
    }

    public void startDiscovery() {
        setDeviceName();
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Toast.makeText(context, "We're discovering!", Toast.LENGTH_SHORT).show();
                            //Log.d("moja", "We're discovering!");// We're discovering!

                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(context, "We're  unable to start discovering", Toast.LENGTH_SHORT).show();
                            //Log.d("moja", "We're  unable to start discovering\n" + e.getMessage());// We're unable to start discovering.
                        });
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                final byte[] receivedBytes = payload.asBytes();

                String message = new String(receivedBytes, StandardCharsets.UTF_8);
//                textView.setText(message);
//                Log.d("moja", "Recieved  ");
                Toast.makeText(context, " " + message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s,
                                            @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                // Do something with is here...
                Log.d("moja", "PayLoad Received ");
            }
        }
    };
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


                    adapter = new Adapter(devices, DiscoverFragment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    button.setOnClickListener(v ->
                    {
                        if (devices.size() == 0) {
                            Toast.makeText(context, "No Devices", Toast.LENGTH_SHORT).show();
                        } else {
                            if (selectedDevices.size() == 0) {
                                Toast.makeText(context, "No Device Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < selectedDevices.size(); i++) {
                                    Nearby.getConnectionsClient(context)
                                            //.requestConnection(deviceName, endpointId, connectionLifecycleCallback)
                                            .requestConnection(deviceName, selectedDevices.get(i).getId(), connectionLifecycleCallback)
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
                    for (int i = 0; i < devices.size(); i++) {
                        if (devices.get(i).getId().contains(endpointId)) {
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
                    new AlertDialog.Builder(context)
                            .setTitle("Accept connection to " + info.getEndpointName())
                            .setMessage("Confirm the code matches on both devices: " + info.getAuthenticationDigits())
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
                Log.d("moja", " id: "+endpointId);
//                            for(int i=0;i<selectedDevices.size();i++) {
                sendPayLoad(endpointId, "hehe boi");// We're connected! Can now start sending and receiving data.
//                                sendPayLoad(selectedDevices.get(i).id, "hehe boi");// We're connected! Can now start sending and receiving data.
//                            }
                break;
            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                // The connection was rejected by one or both sides.
                Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
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
        Nearby.getConnectionsClient(context).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("moja", "Payload Sent");
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
        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        deviceName = (manager.getAdapter().getName());
    }
}
