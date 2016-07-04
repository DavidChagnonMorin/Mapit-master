package com.example.chagnoda.mapit;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
/******IMPORTANT******
* Le projet n'est par terminé donc utiliser cet acompte pour vous connecter
 * user login: david@gmail.com
 * password: 1
 *
* */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    View mapit_button;
    MyLocationListener locationListener = new MyLocationListener();
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapit_button = findViewById(R.id.mapitbutton);
        mapit_button.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toast.makeText(getApplicationContext(), "Press the Mapit button to create a new group or join one near you! ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            startActivity(new Intent("com.example.chagnoda.mapit.MapsActivity"));
        } else if (id == R.id.nav_friends) {
            startActivity(new Intent("com.example.chagnoda.mapit.FriendListActivity"));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent("com.example.chagnoda.mapit.ProfileActivity"));
        } else if (id == R.id.nav_parametre) {
            startActivity(new Intent("com.example.chagnoda.mapit.SettingActivity"));
        } else if (id == R.id.nav_logout) {
            final Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit");
            ref.unauth();
            Toast.makeText(getApplicationContext(), "You have logged out ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent("com.example.chagnoda.mapit.NotConnectedActivity"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        findAllGroupeAroundMe();


    }


    // impossible de faire la permission pour les version precedent api 23
    public Location GPSupdates() {//changer type de retour a location pour l'utilise dans la methode create groupe
        int REQUEST_CODE_ASK_PERMISSIONS = 123;
        int hasLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);

        }
        else {
            // LocationManager service de geolocalisation
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // Recupere les coordonnees dans location, location.getLatitude() et location.getLongitude()
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
            // locationManager.removeUpdates(locationListener);
        }
        return null;
    }

    /*
    distance = R*acos(cos(a)*cos(b)*cos(c-d)+sin(a)*sin(b))
    avec R le rayon de la terre (en metre pour obtenir un résultat en metre)
    a = latitude du point A (en radians)
    b = latitude du point B (en radians)
    c = longitude du point A (en radians)
    d = longitude du point B (en radians)
    */
    public double distWithCoord(double a, double b, double c, double d){
        //R: rayon de la terre
        double R = 6378137;
        return R*Math.acos(Math.cos(a)*Math.cos(b)*Math.cos(c-d)+Math.sin(a)*Math.sin(b));
    }
    //6378137*acos(cos(45.50930521)*cos(45.50932363)*cos(-73.40194585+73.40178653)+sin(45.50930521)*sin(45.50932363))
    public void findAllGroupeAroundMe(){
        try {
            Location mylocation = GPSupdates();

            //GeoLocation location = new GeoLocation(GPSupdates().getLatitude(),GPSupdates().getLongitude());
            final double myLatitude = mylocation.getLatitude();
            final double myLongitude = mylocation.getLongitude();
            Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Groups");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean groupcreated = false;
                    if(dataSnapshot.getValue() == null){
                        // aucun groupe dans le data base
                        Log.d("My app: ","aucun groupe dans la base de donnee , cree un nouveau");
                        startActivity(new Intent("com.example.chagnoda.mapit.CreateGroupActivity"));
                    }
                    for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                        double groupLatitude = (double)groupSnapshot.child("groupeLatitude").getValue();
                        double groupLongitude = (double) groupSnapshot.child("groupeLongitude").getValue();
                        Log.d("My app: ","dist "+ Double.toString(distWithCoord(myLatitude, groupLatitude, myLongitude, groupLongitude)));
                        if (distWithCoord(myLatitude, groupLatitude, myLongitude, groupLongitude) <= 600) {
                            //group exist in zone of scan, join this one
                            Log.d("My app: ", "groupe a promximite");
                            groupcreated = true;
                            SharedPreferences groupActuel = getSharedPreferences("groupeActuel", 0);
                            SharedPreferences.Editor editor = groupActuel.edit();
                            editor.putString("nomgroupeactuel",groupSnapshot.child("groupeName").getValue().toString());
                            editor.commit();
                            startActivity(new Intent("com.example.chagnoda.mapit.ChatActivity"));
                            break;
                        }

                    }
                    if(groupcreated == false) {
                        Log.d("My app: ", "groupe cree");
                        startActivity(new Intent("com.example.chagnoda.mapit.CreateGroupActivity"));
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        catch (Exception exc){
            Toast.makeText(getApplicationContext(), "You have to activate the Location functionality of your system", Toast.LENGTH_SHORT).show();
        }
    }


    public void findAllGroupeAroundLocation(GeoLocation location){

        Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit");
        GeoFire gf = new GeoFire(ref);
        //Query autour de 'mylocation' avec un rayon de 0.5 kilometers
        GeoQuery geoQuery = gf.queryAtLocation(location,0.5);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(FirebaseError error) {

            }
        });
    }

    // Fonction qui gere la creation de groupe

    // On doit metter a jour la location avec GeoFire chaque fois un groupe est cree
    //Geofire est une base de donnee independant (comme la base qui gere le login)




//refs : https://github.com/firebase/geofire-java,
//       https://www.firebase.com/blog/2013-09-25-location-queries-geofire.html
//       https://www.firebase.com/blog/2014-06-23-geofire-two-point-oh.html


}