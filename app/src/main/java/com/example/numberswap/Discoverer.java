package com.example.numberswap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Discoverer {
    public static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    public static final String SERVICE_ID="6969";
    private ConnectionsClient connectionsClient;
    private Context context;
    TextView textView;
    private static final String deviceName = "com.example.numberswap";

    public Discoverer(Context context) {
        this.context = context;
        connectionsClient = Nearby.getConnectionsClient(context);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                final byte[] receivedBytes = payload.asBytes();

                String message = new String(receivedBytes, StandardCharsets.UTF_8);
                textView.setText(message);
                Log.d("moja", "Recieved  ");
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
    public void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.d("moja", "We're discovering!");// We're discovering!
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d("moja", "We're  unable to start discovering\n"+e.getMessage());// We're unable to start discovering.
                        });
    }
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.
                    Nearby.getConnectionsClient(context)
                            .requestConnection(deviceName, endpointId, connectionLifecycleCallback)
                            .addOnSuccessListener(
                                    (Void unused) -> {
                                        // We successfully requested a connection. Now both sides
                                        // must accept before the connection is established.
                                        Log.d("moja", "requested a connection");
                                    })
                            .addOnFailureListener(
                                    (Exception e) -> {
                                        Log.d("moja", "failed connection \n"+ e.getMessage() ); // Nearby Connections failed to request the connection.
                                    });
                }

                @Override
                public void onEndpointLost(@NonNull String endpointId) {
                    // A previously discovered endpoint has gone away.
                }
            };
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                    @Override
                    public void onConnectionInitiated(@NonNull String endpointId, ConnectionInfo info){
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
                            sendPayLoad(endpointId,"hehe");// We're connected! Can now start sending and receiving data.
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
    Log.d("moja", "onFailure : Failed to send Message");
             }
        });
    }
}