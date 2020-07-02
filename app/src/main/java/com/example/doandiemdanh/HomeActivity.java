package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;




public class HomeActivity extends AppCompatActivity {
    private Button btndiemdanh,btncreateclass,btnlist,btnprofile,btnlogout;
    private   FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static  final String USERS = "user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS).child(mAuth.getUid());

        //button2 = findViewById(R.id.button);
        //button2.setVisibility( View.INVISIBLE);
        btnprofile = findViewById(R.id.btn_profile);
        btndiemdanh = findViewById(R.id.btn_diemdanh);
        btncreateclass = findViewById(R.id.btn_createclass);
        btnlist = findViewById(R.id.btn_list);
        btnlogout = findViewById(R.id.btn_logout);

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Profile.class));
            }
        });

        if(getIntent().getExtras().get("user").equals("Sinh Vien")){
            btncreateclass.setVisibility(View.INVISIBLE);
            btnlist.setVisibility(View.INVISIBLE);
        }
        else{

        }

        if(getIntent().getExtras().get("user").equals("Giang Vien")){
            btndiemdanh.setVisibility(View.INVISIBLE);
        }
        else{

        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        btndiemdanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, DiemdanhActivity.class));
            }
        });



    }

}
