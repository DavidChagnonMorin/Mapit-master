package com.example.chagnoda.mapit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by chengli on 2016-03-21.
 * Sources
 * Chat inspiree du site web https://trinitytuts.com/simple-chat-application-using-listview-in-android/
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);
        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);

          //chatArrayAdapter
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        SharedPreferences groupeActuel = getSharedPreferences("groupeActuel", 0);
        String NomGroupe = groupeActuel.getString("nomgroupeactuel", "missing");
        Firebase firebase= new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Groups");
        final Firebase ref=firebase.child(NomGroupe);
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        final String user = userInfo.getString("username", "missing");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String size  = dataSnapshot.child("groupeSize").getValue().toString();
                int groupSize = Integer.parseInt(size);
                groupSize+=1;
                SharedPreferences groupActuel = getSharedPreferences("groupeActuel", 0);
                SharedPreferences.Editor editor = groupActuel.edit();
                editor.putString("position",Integer.toString(groupSize));
                editor.commit();
                ref.child("memberList").child(Integer.toString(groupSize)).setValue(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref.child("memberList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String size = dataSnapshot.child("groupeSize").getValue().toString();
                        int groupSize = Integer.parseInt(size);
                        groupSize += 1;
                        ref.child("groupeSize").setValue(groupSize);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String size = dataSnapshot.child("groupeSize").getValue().toString();
                        int groupSize = Integer.parseInt(size);
                        groupSize -= 1;
                        ref.child("groupeSize").setValue(groupSize);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref.child("discussions").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("sender").equals(user)) {
                    chatArrayAdapter.add(new Discussion(!side, dataSnapshot.child("sender").getValue().toString(), dataSnapshot.child("discussionContent").getValue().toString(), Calendar.getInstance().getTime()));

                }
                else{
                    chatArrayAdapter.add(new Discussion(side, dataSnapshot.child("sender").getValue().toString(), dataSnapshot.child("discussionContent").getValue().toString(), Calendar.getInstance().getTime()));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // case de saisi de texte
        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //methode qui ajuste la listview a l'ajout d'un message
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


    }
    public void onPause(){
        super.onPause();;
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        final String user = userInfo.getString("username", "missing");
        SharedPreferences groupeActuel = getSharedPreferences("groupeActuel", 0);
        String NomGroupe = groupeActuel.getString("nomgroupeactuel", "missing");
        String position = groupeActuel.getString("position","missing");
        Firebase ref= new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Groups");
        ref.child(NomGroupe).child("memberList").child(position).removeValue();
    }


    private boolean sendChatMessage() {
        Firebase firebase= new Firebase("https://sizzling-inferno-6141.firebaseio.com/Mapit/Groups");
        SharedPreferences groupeActuel = getSharedPreferences("groupeActuel", 0);
        String NomGroupe = groupeActuel.getString("nomgroupeactuel", "missing");
        final Firebase ref = firebase.child(NomGroupe);
        ref.child("discussions").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cpt = 0;
                for (DataSnapshot discussion : dataSnapshot.getChildren()) {
                    cpt += 1;
                }
                SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
                String user = userInfo.getString("username", "missing");
                Discussion discussion = new Discussion(!side, user, chatText.getText().toString(), Calendar.getInstance().getTime());
                ref.child("discussions").child(Integer.toString(cpt)).setValue(discussion);
                chatText.setText("");

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return true;
    }


}
