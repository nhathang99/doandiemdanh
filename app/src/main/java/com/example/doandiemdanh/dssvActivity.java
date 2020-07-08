package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dssvActivity extends AppCompatActivity {
    TextView tensv;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayList<String> list1;
    ArrayAdapter<String> adapter;

    User users;
    user_class User_class;
    courses Courses;
    String ten;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dssv);
        tensv = findViewById(R.id.txt_tensv);
        //lấy mã môn


        Courses = new courses();
        User_class = new user_class();
        users = new User();


        listView = (ListView) findViewById(R.id.listviewsv);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("user_class");
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list1 = getIntent().getExtras().getStringArrayList("uid");
        adapter = new ArrayAdapter<String>(this,R.layout.dssv_info,R.id.txt_tensv,list);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("user");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (list1.contains(ds.getKey())){
                        String a = user.getFullName();
                        list.add(a);
                    }
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
