package com.example.safetyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class friends extends AppCompatActivity {


    String fullname,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_phone = findViewById(R.id.edit_number);
        Button btn = findViewById(R.id.btn_submit);
        DAOcontact dao = new DAOcontact();
        Button btn_list = findViewById(R.id.btn_list);
        fullname = getIntent().getStringExtra("fullname");
        phone = getIntent().getStringExtra("phone");




        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),contactlist.class));
                finish();
            }
        });




        btn.setOnClickListener(v ->
        {
                users con = new users(edit_name.getText().toString(), edit_phone.getText().toString());


                dao.add(con).addOnSuccessListener(suc ->
                {
                        Toast.makeText(this, "Contact Added.", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });

            });


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(friends.this, MainActivity.class);
        startActivity(intent);
    }
}