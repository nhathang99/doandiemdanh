package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
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
    String ten, lop;
    Button csv;
    StringBuilder data;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dssv);
        tensv = findViewById(R.id.txt_tensv);
        //lấy mã môn
        csv = findViewById(R.id.btncsv);

        Courses = new courses();
        User_class = new user_class();
        users = new User();


        listView = (ListView) findViewById(R.id.listviewsv);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("user_class");
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list1 = getIntent().getExtras().getStringArrayList("uid");
        lop = getIntent().getExtras().get("class").toString();
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

                        list.add(user.getMs()+" "+a);
                    }
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export(view);
            }
        });
    }
    public void export (View view){
        //generate data
        data = new StringBuilder();
        data.append(lop+"\n");
        data.append("Mssv,Name");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("user");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (list1.contains(ds.getKey())){
                        String a = user.getMs();
                        data.append("\n" + a + "," + user.getFullName());
                    }
                }
                try {
                    //saving the file into device
                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    Context context = getApplicationContext();
                    File filelocation = new File(getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.doandiemdanh.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
