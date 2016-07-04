package com.example.chagnoda.mapit;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengli on 2016-03-21.
 */
public class SuscribeActivity extends AppCompatActivity implements View.OnClickListener{
    Button suscribe;
    TextView username_view,email_view, password_view, confirmed_password_view;
    ImageButton profilepicture_view;
    ImageButton imagePick;
    private final static int SELECT_PHOTO = 12345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_suscribe);
        suscribe = (Button)findViewById(R.id.suscribebutton);
        suscribe.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        username_view = (TextView)findViewById(R.id.usernamebox);
        final String username = username_view.getText().toString();
        email_view = (TextView)findViewById(R.id.emailbox);
        final String email = email_view.getText().toString();
        password_view = (TextView)findViewById(R.id.passwordbox);
        final String password = password_view.getText().toString();
        confirmed_password_view = (TextView)findViewById(R.id.confirmedpasswordbox);
        String confirmed_password = confirmed_password_view.getText().toString();


        final Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit");
        ref.createUser(email, password, new Firebase.ResultHandler() {

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), " Your account has been created!", Toast.LENGTH_SHORT).show();
                Profile new_user = new Profile(username,email, password);
                ref.child("Profiles").child(username).setValue(new_user);
                startActivity(new Intent("com.example.chagnoda.mapit.LoginActivity"));

            }
            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "User not created!", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
