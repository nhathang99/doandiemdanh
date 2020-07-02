package com.example.doandiemdanh;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiemdanhActivity extends AppCompatActivity {
    EditText Edittextkinhdo, Edittextvido;
    TextView Textviewdiachi;
    Button getLocation;

    public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat,lon;

    private  boolean gps_enable = false;
    private  boolean network_enable = false;

    // to generate address
    Geocoder geocoder;
    List<Address> myaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diemdanh);
        Edittextvido = findViewById(R.id.Edittext_vido);
        Edittextkinhdo = findViewById(R.id.Edittext_kinhdo);
        Textviewdiachi = findViewById(R.id.diachi_textview);
        getLocation = findViewById(R.id.button);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMylocation();
            }
        });
        checklocationPermission();

    }

    class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(DiemdanhActivity.this, "run"+lat + lon,
                    Toast.LENGTH_SHORT).show();
            Log.d("my_location", "onLocationChanged: "+lat + lon);
            if(location!=null)

            lat=""+location.getLatitude();
            lon = "" +location.getLongitude();
            Toast.makeText(DiemdanhActivity.this, "run"+lat + lon,
                    Toast.LENGTH_SHORT).show();
            Edittextvido.setText(lat);
            Edittextkinhdo.setText(lon);

            geocoder = new Geocoder(DiemdanhActivity.this, Locale.getDefault());
            locationManager.removeUpdates(locationListener);


            try {
                myaddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String Textviewdiachi1 = myaddress.get(0).getAddressLine(0);
            Textviewdiachi.setText(Textviewdiachi1);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
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

}
