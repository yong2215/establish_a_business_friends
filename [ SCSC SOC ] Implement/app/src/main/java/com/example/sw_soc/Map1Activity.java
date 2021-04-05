package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Map1Activity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Ref;

    int count = 0;
    List<Brand> brandList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String brandName = bundle.getString("brandname");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Ref = database.getReference("Brand/"+brandName);
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Brand brand = dataSnapshot.getValue(Brand.class);
                    brandList.add(brand);
                    count += 1;
                }
                onMapReady(mMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center = new LatLng(35.842967, 128.577986);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
        double lat;
        double lng;
        String bn;
        String name;
        String addr;
        for (int i = 0; i < brandList.size(); i++) {
            lat = brandList.get(i).위도;
            lng = brandList.get(i).경도;
            name = brandList.get(i).지점명;
            bn = brandList.get(i).브랜드명;
            addr = brandList.get(i).주소;
            LatLng fPoint = new LatLng(lat, lng);
            Marker point = mMap.addMarker(new MarkerOptions()
                    .position(fPoint)
                    .title("지점명: " +bn+" "+ name)
                    .snippet("주소: " + addr));
            point.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
        }
    }

}