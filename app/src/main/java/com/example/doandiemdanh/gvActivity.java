package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gvActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayList<String> list1;
    ArrayAdapter<String> adapter;

    courses Courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gv);

        Courses = new courses();
        listView = (ListView) findViewById(R.id.lisview);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("courses");
        list = new ArrayList<>();
        list1 = new ArrayList();
        adapter = new ArrayAdapter<String>(this,R.layout.classgv_info,R.id.txt_mamon,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Courses = ds.getValue(courses.class);
                    list.add(Courses.getmamonhoc().toString()+",  "+Courses.gettenmonhoc().toString());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                final String result = selectedItem.substring(0, selectedItem.indexOf(","));

                DatabaseReference ref = database.getReference("user_class");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            user_class User_class = ds.getValue(user_class.class);
                            if (User_class.getMamon().equals(result)){
                                list1.add(User_class.getUserid());
                            }
                        }
                        Intent intent = new Intent(gvActivity.this, dssvActivity.class);
                        Bundle bundle = new Bundle();
                        //bundle.putString("class",result);
                        bundle.putStringArrayList("uid",list1);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }



}
