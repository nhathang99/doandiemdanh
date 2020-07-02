package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private TextView nametextview, occupationtextview, workplacetextview,emailtextview,phonetextview;
    private ImageView userimageView, emailimageview,phoneimageview;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    private   FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static  final String USERS = "user";
    String  email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS).child(mAuth.getUid());

        Log.v("EMAILADD", userRef.orderByChild("email").equalTo(email).toString());

        workplacetextview = findViewById(R.id.workplace_textview);
        occupationtextview = findViewById(R.id.occupation_textview);
        nametextview = findViewById(R.id.name_textview);
        emailtextview = findViewById(R.id.email_textview);
        emailimageview = findViewById(R.id.email_imageview);
        phonetextview=findViewById(R.id.phone_textview);



        database =FirebaseDatabase.getInstance();
         //  userRef = database.getReference(USERS);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                Log.d("profile - id", "onDataChange: "+ mAuth.getUid());
                User user = Snapshot.getValue(User.class);
                nametextview.setText(user.getFullName());
                occupationtextview.setText(user.getProfession());
                workplacetextview.setText(user.getLocation());
                emailtextview.setText(user.getEmail());
                phonetextview.setText(user.getPhone());

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
