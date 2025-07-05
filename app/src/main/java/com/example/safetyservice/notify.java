package com.example.safetyservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.StringCharacterIterator;

public class notify extends BroadcastReceiver {


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        if (intent.getAction().equalsIgnoreCase("CONFIRM")) {

                            Bundle locationlat= intent.getExtras();
                            if(locationlat != null){
                                if(locationlat.containsKey("lat")){
                                    Object value = locationlat.get("lat");
                                }
                            }

                                        SmsManager smsManager = SmsManager.getDefault();
                                        StringBuffer smsBody = new StringBuffer();
                                        String uri = "http://maps.google.com/?q=";
                                        smsBody.append(Uri.parse(uri));
                                        smsBody.append(".");
                                        smsBody.append("\n Help Needed !!!! Contact to given location as soon as possible !!");

                                        smsManager.sendTextMessage("9769023847", null, smsBody.toString(), null, null);


                                        Toast.makeText(context, "Sent SMS", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }


