package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaoLopActivity extends AppCompatActivity {
   private EditText MaMonHoceditText,TenMonHoceditText;
   private Button taolopbutton;
   private Spinner gio;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    ArrayAdapter<String> dataAdapter;
    private FirebaseAuth mAuth;
    private static  final String USER ="user";
    private static  final String COURSES="Courses";
    private courses courses1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_tao_lop);
        MaMonHoceditText = findViewById(R.id.MaMonHoc_editText);
        TenMonHoceditText= findViewById(R.id.TenMonHoc_editText);
        taolopbutton = findViewById(R.id.taolop_button);
        gio = findViewById(R.id.gio);
        ArrayList<String>  gios = new ArrayList<>();
        gios.add("7:30-11:30");
        gios.add("13:00-16:00");

        dataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item, gios);
        dataAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        // attaching data adapter to spinner
        gio.setAdapter(dataAdapter);
        gio.setHorizontalScrollBarEnabled(true);

        taolopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = MaMonHoceditText.getText().toString();
                String b = TenMonHoceditText.getText().toString();
                if (a.isEmpty() || b.isEmpty()) {
                    Toast.makeText(TaoLopActivity.this, "Mã Môn học hoặc tên môn học còn trống", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("mamonhoc", a);
                    map.put("tenmonhoc", b);
                    map.put("startime", gio.getSelectedItemPosition());
                    FirebaseDatabase.getInstance().getReference().child("courses").push().setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("abc", "onComplete: ");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("abc", "onFailure " + e.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TaoLopActivity.this, "Tạo lớp thành công",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




    }
}
