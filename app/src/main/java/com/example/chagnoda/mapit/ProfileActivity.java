package com.example.chagnoda.mapit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 *
 * Fait par Bouchra
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button amis;
    Button events;
    TextView textView;
    ImageView imageView;
    String user;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = (ImageView) findViewById(R.id.imageView_photoProfile_id);
        textView = (TextView) findViewById(R.id.profilename_id_text);

        amis=(Button)findViewById(R.id.button_Amis);
        events=(Button)findViewById(R.id.button_Events);

        Intent intent = getIntent();
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        String user = userInfo.getString("username", "missing");
        imageUrl=intent.getStringExtra("PHOTO_URL");

        textView.setText(user);
        Picasso.with(this).load(imageUrl).into(imageView);



        amis.setOnClickListener(this);
        events.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.button_Amis:
                Log.d("My App:", "work");
                startActivity(new Intent("com.example.chagnoda.FriendListActivity"));
                break;
            case R.id.button_Events:
                startActivity(new Intent("com.example.chagnoda.mapit.EventListActivity"));
                break;
        }

    }


}