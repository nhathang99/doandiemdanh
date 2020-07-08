package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DiemdanhActivity extends AppCompatActivity {
    EditText Edittextkinhdo, Edittextvido;
    TextView end, start;
    TextView tenmon;
    Button getLocation,buttondiemdanh;

    public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat,lon;
    courses Courses;
    private  boolean gps_enable = false;
    private  boolean network_enable = false;
    FirebaseAuth mAuth;
    // to generate address
    Geocoder geocoder;
    List<Address> myaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diemdanh);
        //Edittextvido = findViewById(R.id.Edittext_vido);
        //Edittextkinhdo = findViewById(R.id.Edittext_kinhdo);
        start = findViewById(R.id.start);
        getLocation = findViewById(R.id.button);
        tenmon = findViewById(R.id.tenmon);
        end = findViewById(R.id.end);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMylocation();
            }
        });
        checklocationPermission();

        String ten = getIntent().getExtras().get("class").toString();

        tenmon.setText(ten);
        mAuth = FirebaseAuth.getInstance();
        setDataToView();
        }

    class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            try {
                diemDanh(location);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lat=""+location.getLatitude();
            lon = "" +location.getLongitude();
            //Edittextvido.setText(lat);
            //Edittextkinhdo.setText(lon);



            geocoder = new Geocoder(DiemdanhActivity.this, Locale.getDefault());
            locationManager.removeUpdates(locationListener);


            try {
                myaddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
           // String Textviewdiachi1 = myaddress.get(0).getAddressLine(0);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
    public void getMylocation(){
        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex)
        {


        }
        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){

        }

        if(!gps_enable && !network_enable)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DiemdanhActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Không lấy được địa chỉ ");
            builder.create().show();
        }

        Toast.makeText(DiemdanhActivity.this, "run"+gps_enable + network_enable,
                Toast.LENGTH_SHORT).show();

        if(gps_enable){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {

            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            }
        }
        if(network_enable)
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

        }
    }
    private boolean checklocationPermission(){
        int location = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            List<String> listPermission = new ArrayList<>();

            if(location!= PackageManager.PERMISSION_GRANTED){
             listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(location2!= PackageManager.PERMISSION_GRANTED){
                listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if(!listPermission.isEmpty()){
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]),1);
            }
            return true;

    }

    public void diemDanh(Location location) throws ParseException {
        double x = location.getLatitude();
        double y = location.getLongitude();
        Calendar calendar = Calendar.getInstance();
        Date now1 = calendar.getTime();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String now2 = format.format(now1);
        Date now = format.parse(now2);

        if ((x > 10.8703783 && x < 10.879399) && (y > 106.8060374 && y<106.8113543)){
            String startstr = start.getText().toString();
            String endstr = end.getText().toString();
            Date starttime = format.parse(startstr);
            Date endtime = format.parse(endstr);

            if(now.after(starttime) && now.before(endtime)){
                diemDanhToDb();

            }else {
                Toast.makeText(getApplicationContext(), "Điểm danh sai giờ!", Toast.LENGTH_LONG).show();

            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Điểm danh sai vị trí!", Toast.LENGTH_LONG).show();
        }

    }

    public void diemDanhToDb(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("mamon", tenmon.getText().toString());
        map.put("userid", mAuth.getUid());
        FirebaseDatabase.getInstance().getReference().child("user_class").push().setValue(map)
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
                Toast.makeText(getApplicationContext(), "Điểm danh thành công!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setDataToView(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("courses");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String starttime = "";
                String endtime ="";
                for(DataSnapshot ds: snapshot.getChildren()){
                    Courses = ds.getValue(courses.class);
                    if (Courses.getmamonhoc().equals(tenmon.getText())){
                        switch (Courses.getstartime()){
                            case 0:
                                starttime = "22:30:00";
                                endtime = "23:59:00";
                                break;
                            case 1:
                                starttime = "13:00:00";
                                endtime = "17:30:00";
                                break;
                        }
                        end.setText(endtime);
                        start.setText(starttime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
