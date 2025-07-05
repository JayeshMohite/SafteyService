package com.example.safetyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView verifymsg, welcomemsg, friends, voice, profile,note;
    ImageButton friendsbtn, voicebtn, profilebtn;
    Button verifybtn;
    FirebaseAuth auth;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        auth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.button4);
        verifymsg = findViewById(R.id.textView);
        welcomemsg = findViewById(R.id.textView3);
        verifybtn = findViewById(R.id.button5);
        friendsbtn = findViewById(R.id.imageButton3);
        voicebtn = findViewById(R.id.imageButton4);
        profilebtn = findViewById(R.id.imageButton5);
        friends = findViewById(R.id.textView2);
        voice = findViewById(R.id.textView4);
        profile = findViewById(R.id.textView5);
        note=findViewById(R.id.textView14);

        if (!auth.getCurrentUser().isEmailVerified()) {
            verifybtn.setVisibility(View.VISIBLE);
            verifymsg.setVisibility(View.VISIBLE);
            welcomemsg.setVisibility(View.VISIBLE);
            friends.setVisibility(View.GONE);
            voice.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            friendsbtn.setVisibility(View.GONE);
            voicebtn.setVisibility(View.GONE);
            profilebtn.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
        }






        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                        verifybtn.setVisibility(View.GONE);
                        verifymsg.setVisibility(View.GONE);
                        welcomemsg.setVisibility(View.GONE);
                        friends.setVisibility(View.VISIBLE);
                        voice.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.VISIBLE);
                        friendsbtn.setVisibility(View.VISIBLE);
                        voicebtn.setVisibility(View.VISIBLE);
                        profilebtn.setVisibility(View.VISIBLE);
                        note.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        friendsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), friends.class));
                finish();
            }
        });

        voicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), voice.class));
                finish();
            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), profile.class));
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this
                , Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 100);
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit.")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

