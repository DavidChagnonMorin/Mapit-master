package com.example.chagnoda.mapit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_maps);
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
        final Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Groups");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot groupSnapshot : dataSnapshot.getChildren()){
                    double latitude = (double)groupSnapshot.child("groupeLatitude").getValue();
                    double longitude = (double)groupSnapshot.child("groupeLongitude").getValue();
                    String color = groupSnapshot.child("groupeColor").getValue().toString();
                    int c = Integer.parseInt(color);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Group: " + groupSnapshot.child("groupeName").getValue().toString())
                            .snippet("Paticipants: " + groupSnapshot.child("groupeSize").getValue().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));



                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(latitude, longitude))
                            .radius(500).fillColor(c).strokeColor(c);
                    Circle circle = mMap.addCircle(circleOptions);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                final Marker m = marker;
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                            if(m.getTitle().equals("Group: " + groupSnapshot.child("groupeName").getValue().toString())){
                                SharedPreferences groupActuel = getSharedPreferences("groupeActuel", 0);
                                SharedPreferences.Editor editor = groupActuel.edit();
                                editor.putString("nomgroupeactuel",groupSnapshot.child("groupeName").getValue().toString());
                                editor.commit();
                                startActivity(new Intent("com.example.chagnoda.mapit.ChatActivity"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });


    }

}
