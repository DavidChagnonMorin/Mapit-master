package com.example.chagnoda.mapit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by David on 27/04/2016.
 */
public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    Button groupbutton;
    TextView groupname_view;
    LocationManager locationManager;
    MyLocationListener locationListener = new MyLocationListener();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_creategroup);

        groupbutton = (Button)findViewById(R.id.creategroupbutton);
        groupbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        groupname_view = (TextView)findViewById(R.id.groupname);
        String groupname = groupname_view.getText().toString();
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        Profile copy_profile = new Profile(userInfo.getString("username", "missing username"),userInfo.getString("email","missing email"),userInfo.getString("password","missing password"));
        createGroup(copy_profile, groupname);


    }



    public void createGroup (Profile creator,String groupname){
        Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit");
        Location location = GPSupdates();//acquérir location quand on cree un groupe
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String s = "welcome to the groupe : "+groupname+" , the creator of groupe is : " + creator.getUserName();
        ArrayList<Discussion> discussions = new ArrayList<Discussion>();
        Discussion firstMessage = new Discussion(true, creator.getUserName() , s, Calendar.getInstance().getTime());
        discussions.add(firstMessage);
        Date timeoflastmessage = discussions.get(discussions.size()-1).getTimeSend();
        Groupe new_group = new Groupe(groupname,0,randomColor(),latitude,longitude,creator.getUserName(),discussions,timeoflastmessage);//utilise cette location pour creer le groupe
        ref.child("Groups").child(new_group.getGroupeName()).setValue(new_group);//enregister dans la base
        ref.child("Groups").child(new_group.getGroupeName()).child("discussions").setValue(discussions);
        SharedPreferences groupeActuel = getSharedPreferences("groupeActuel", 0);
        SharedPreferences.Editor editor = groupeActuel.edit();
        editor.putString("nomgroupeactuel",groupname);
        editor.commit();
        startActivity(new Intent("com.example.chagnoda.mapit.ChatActivity"));
    }
    public int randomColor(){
        int selector = (int)Math.floor(Math.random()*10);
        Log.d("My app:",Integer.toString(selector));
        switch (selector){
            case 0:
                return -65400;
            case 1:
                return -65536;
            case 3:
                return -65800;
            case 4:
                return -65900;
            case 5:
                return -65100;
            case 6:
                return -65120;
            case 7:
                return -65200;
            case 8:
                return -65950;
            case 9:
                return -65810;
        }
        return 0;
    }

    public Location GPSupdates() {//changer type de retour a location pour l'utilise dans la methode create groupe
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return null;
        }
        // LocationManager service de geolocalisation
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // Recupere les coordonnees dans location, location.getLatitude() et location.getLongitude()
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Log.d sert a afficher dans le logcat, si on cherche My App dans la saisi de recherche en bas, on voit le message.(pour voir les coordonnees appuyer sur le bouton mapit(onClick()))
        Log.d("My App:", "latitude: " + location.getLatitude() + "longitude: " + location.getLongitude());
        return location;
        // locationManager.removeUpdates(locationListener);

    }
}
