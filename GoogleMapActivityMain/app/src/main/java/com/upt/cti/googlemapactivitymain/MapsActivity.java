package com.upt.cti.googlemapactivitymain;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.upt.cti.googlemapactivitymain.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final int REQ_PERMISSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng upt = new LatLng(45.7476360266562, 21.226266202568336);
        LatLng camin1mv = new LatLng(45.7453850697558, 21.226252923361407);

        double distance = SphericalUtil.computeDistanceBetween(upt,camin1mv);
        System.out.println("Distance: " + distance);
        mMap.addMarker(new MarkerOptions().position(upt).title("Marker in UPT, TM"));
        mMap.addMarker(new MarkerOptions().position(camin1mv).title("Marker in Camin1MV, TM"));

        List<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(upt);
        listPoints.add(camin1mv);
        Polyline polyline = mMap.addPolyline(drawPolyLineOnMap(listPoints,mMap)
                .clickable(true)
        );

       mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
           @Override
           public void onPolylineClick(Polyline polyline) {
               Toast.makeText(MapsActivity.this, "Distance between upt and camin1mv is: " + distance,Toast.LENGTH_SHORT).show();
           }
       });

//        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//
//            @Override
//            public void onPolylineClick(Polyline polyline) {
//
//
//                polyline.getPoints();
//                Toast.makeText(MapsActivity.this, "Distance between upt and camin1mv is: " , Toast.LENGTH_LONG).show();
//
//            }
//        });



        mMap.moveCamera(CameraUpdateFactory.newLatLng(upt));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camin1mv));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        iconGenerator.setTextAppearance(R.color.black);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getPosition().equals(upt)) {
                    Toast.makeText(MapsActivity.this, "I'm studying", Toast.LENGTH_LONG).show();
                }
                else {
                    //
                }
                return false;
            }
        });



        mMap.addMarker(new MarkerOptions()
                .position(upt)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("Facultate"))));
        if (checkPermission()) {
            mMap.setMyLocationEnabled(true);
        }
            else {
                askPermission();
            }

        }


    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[]grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission())
                        mMap.setMyLocationEnabled(true);
                } else {
                    // Permission denied
                }
                break;
            }
        }
    }

    public PolylineOptions drawPolyLineOnMap(List<LatLng> list, GoogleMap googleMap){
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GREEN);
        polyOptions.width(30);
        polyOptions.addAll(list);
        googleMap.addPolyline(polyOptions);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list){
            builder.include(latLng);
        }
        builder.build();
        return polyOptions;
    }



}