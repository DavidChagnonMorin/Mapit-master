package com.example.david.mapit;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.firebase.client.Firebase;



public class MainActivity extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        Firebase ref = new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit");
        ref.child("Groups").setValue("liste des groupes!");
        ref.child("Profiles").setValue("liste des profiles");
    }





}