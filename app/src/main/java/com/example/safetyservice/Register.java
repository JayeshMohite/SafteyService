package com.example.safetyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText Fullname, Email, Password, Phonenumber;
    Button registeruser,gotologin;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        Fullname = findViewById(R.id.editTextTextPersonName);
        Email = findViewById(R.id.editTextTextEmailAddress2);
        Password = findViewById(R.id.editTextTextPassword2);
        Phonenumber = findViewById(R.id.editTextPhone);
        registeruser = findViewById(R.id.button2);
        gotologin = findViewById(R.id.button3);

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        registeruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String fullname = Fullname.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String phonenumber = Phonenumber.getText().toString();


                if(fullname.isEmpty()){
                    Fullname.setError("Full name is required!");
                    return;
                }

                if(email.isEmpty()){
                    Email.setError("Email Address is required!");
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Please provide a valid email address!");
                    return;
                }

                if(password.isEmpty()){
                    Password.setError("Password is required!");
                    return;
                }

                if(password.length() < 6){
                    Password.setError("Min password length should be 6 character!");
                    return;
                }

                if(phonenumber.isEmpty()){
                    Phonenumber.setError("Phone Number is required!");
                    return;
                }


                Toast.makeText(Register.this, "Registered Successfully!",Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fstore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("fullname",fullname);
                        user.put("email",email);
                        user.put("phonenumber",phonenumber);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSuccess: user profile is created for"+ userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onFailure : "+e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }



                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });

    }



}