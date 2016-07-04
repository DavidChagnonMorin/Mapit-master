package com.example.chagnoda.mapit;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Fait par Bouchra
// référence : Site de Firebase

// les photos sont prix de google (internet)

public class ListProfile extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    MyAdapter adapter;
    List<NewProfil> profiles = new ArrayList<NewProfil>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profile);

        listView=(ListView)findViewById(R.id.list_profiles);

        Firebase.setAndroidContext(this);

        Firebase myFirebaseRef= new Firebase("https://sizzling-inferno-6141.firebaseio.com/");

        myFirebaseRef.child("Mapit/Profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                Log.d("firebase","datasetchanged");
                for (DataSnapshot child : snapshot.getChildren()) {

                    final NewProfil newprofil = new NewProfil();

                    newprofil.email = String.valueOf(child.child("email").getValue());
                    newprofil.username = String.valueOf(child.child("userName").getValue());
                    newprofil.password = String.valueOf(child.child("password").getValue());
                    newprofil.photoUrl=String.valueOf(child.child("urlPhoto").getValue());

                    profiles.add(newprofil);


                }
                adapter=new MyAdapter(profiles);
                listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(FirebaseError error) {}
        });


        listView.setOnItemClickListener(ListProfile.this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String username1=profiles.get(position).username;
        String image=profiles.get(position).photoUrl;
        Intent intent=new Intent(ListProfile.this, ProfileActivity.class);
        intent.putExtra("PHOTO_URL", image);
        intent.putExtra("USER_NAME",username1);
        startActivity(intent);


    }


    public class MyAdapter extends BaseAdapter{

        LayoutInflater inflater;
        List<NewProfil> profiles=new ArrayList<NewProfil>();

        public MyAdapter(List<NewProfil> profiles) {
            this.profiles=profiles;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return profiles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v=convertView;
            if(v==null){

                v=inflater.inflate(R.layout.rangeeprofile, parent, false);// on a crée notre propre layout
            }
            if(position%2==0) v.setBackgroundColor(0xFFE3B5B5);
            else v.setBackgroundColor(Color.WHITE);

            TextView tv=(TextView)v.findViewById(R.id.textView_user_id);
            ImageView image=(ImageView)v.findViewById(R.id.imageView_rangeePhotoProfile_id);


            String user=profiles.get(position).username;
            String UrlPhoto=profiles.get(position).photoUrl;

            tv.setText(user);


            Picasso.with(getApplicationContext()).load(UrlPhoto).into(image);

            return v;
        }
    }
}