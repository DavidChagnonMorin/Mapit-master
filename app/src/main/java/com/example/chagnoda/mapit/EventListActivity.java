package com.example.chagnoda.mapit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengli on 2016-03-21.
 */
public class EventListActivity extends AppCompatActivity{
    ListView list;
    List<Event> eventList=new ArrayList<Event>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_event_list);

        list=(ListView)findViewById(R.id.listEvent);
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        String user = userInfo.getString("username", "missing");

        Firebase firebase= new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Profiles");
        Firebase ref=firebase.child(user);
        ref.child("Evenements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event event= new Event();
                    event.eventName=child.child("name").getValue().toString();
                    event.eventImageUrl=child.child("photoUrl").getValue().toString();
                    event.eventDescription=child.child("Description").getValue().toString();
                    eventList.add(event);

                }
                adapter = new MyAdapter(eventList);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

    }

    public class MyAdapter extends BaseAdapter {

        List<Event> eventList=new ArrayList<Event>();

        LayoutInflater inflater;
        public MyAdapter(List<Event> eventList) {
            this.eventList=eventList;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return eventList.size();
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
                v=inflater.inflate(R.layout.rangee_event, parent, false);
            }
            if(position%2==0) v.setBackgroundColor(0xFFE3B5B5);
            else v.setBackgroundColor(Color.WHITE);

            TextView tv=(TextView)v.findViewById(R.id.rangee_textEvent);
            ImageView image=(ImageView)v.findViewById(R.id.rangee_imageEvent);

            String event=eventList.get(position).eventName;
            String photoUrl=eventList.get(position).eventImageUrl;

            tv.setText(event);
            Picasso.with(getApplicationContext()).load(photoUrl).into(image);
            return v;
        }
    }

}