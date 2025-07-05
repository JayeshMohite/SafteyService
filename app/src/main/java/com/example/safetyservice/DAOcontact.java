package com.example.safetyservice;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class DAOcontact extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userID;



    private DatabaseReference databaseReference;
    public  DAOcontact()
    {
        
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = db.getReference(users.class.getSimpleName()).child(userID);




    }
    public Task<Void> add(users con)
    {
        return databaseReference.push().setValue(con);
    }

}
