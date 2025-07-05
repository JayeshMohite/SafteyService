package com.example.safetyservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.PendingIntent ;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent ;
import android.content.IntentFilter;
import android.os.Bundle ;
import android.util.Log;
import android.view.View ;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.speech.tts.Voice;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;
import java.util.List;

public class voice extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    EditText mobilephone;
    TextView message,viewphone;
    Button send,btton;
    TextView fname;
    String userID,phone;
    DatabaseReference reff;


    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textlatlong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mobilephone = findViewById(R.id.editTextPhone3);
        message = findViewById(R.id.textView13);
        send = findViewById(R.id.button9);
        fauth = FirebaseAuth.getInstance();
        userID = fauth.getCurrentUser().getUid();
        message.setVisibility(View.GONE);
        textlatlong = findViewById(R.id.textlatlong);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btton=findViewById(R.id.service);
        textlatlong.setVisibility(View.GONE);
        mobilephone.setVisibility(View.GONE);




        btton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int NOTIFICATION_ID = ( int ) System. currentTimeMillis () ;
                if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
                    Intent buttonIntent = new Intent(voice.this, notify.class);
                    buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
                    PendingIntent btPendingIntent = PendingIntent.getBroadcast(voice.this, 0, buttonIntent, 0);
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "SafetyServices", importance);
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel);



                    Intent intent = new Intent(voice.this, voice.class);
                    intent.putExtra("fromNotification", "book_ride");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Intent intentConfirm = new Intent(voice.this, notify.class);
                    intentConfirm.setAction("CONFIRM");
                    intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    Intent intentCancel = new Intent(voice.this, notify.class);
                    intentCancel.setAction("CANCEL");
                    intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    PendingIntent pendingIntent = PendingIntent.getActivity(voice.this, 0 , intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    PendingIntent pendingIntentConfirm = PendingIntent.getBroadcast(voice.this, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT);

                    PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(voice.this, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            mBuilder.setSmallIcon(R.drawable.ic_notify)
                            .setContentTitle("Are You in Emergency Situation ?")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    assert mNotificationManager != null;
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(voice.this
                        , Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(voice.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(voice.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions(voice.this
                            , new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                            , 100);
                }

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(voice.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();

                }

            }
        });
    }



    private void sendMessage() {
        String sphone = mobilephone.getText().toString().trim();
        String smessage = message.getText().toString().trim();


        if (!sphone.equals("") && !smessage.equals("")) {



            Toast.makeText(getApplicationContext()
                    , "SMS sent successfully", Toast.LENGTH_LONG).show();
        } else {


        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            Toast.makeText(getApplicationContext()
                    , "Permission Denied", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(voice.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(voice.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() >0 ){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textlatlong.setText(
                                    String.format(
                                            "Latitude : %s \n Longitude : %s",
                                            latitude,
                                            longitude
                                    )
                            );
                            String lat = String.valueOf(latitude);
                            String lon = String.valueOf(longitude);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        List<retrivedlist> universityList = new ArrayList<>();
                                        retrivedlist list = data.getValue(retrivedlist.class);
                                        universityList.add(list);



                                        String sphone = mobilephone.getText().toString().trim();
                                        SmsManager smsManager = SmsManager.getDefault();
                                        StringBuffer smsBody = new StringBuffer();
                                        String uri = "http://maps.google.com/?q=" + lat + "," + lon;
                                        smsBody.append(Uri.parse(uri));
                                        smsBody.append(".");
                                        smsBody.append("\n Help Needed !!!! Contact to given location as soon as possible !!");


                                        smsManager.sendTextMessage(list.getPhone(), null, smsBody.toString(), null, null);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            }
                        }
                    }
                ,Looper.getMainLooper());



    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(voice.this, MainActivity.class);
        startActivity(intent);
    }

}