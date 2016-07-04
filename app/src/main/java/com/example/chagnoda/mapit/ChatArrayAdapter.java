package com.example.chagnoda.mapit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chagnoda.mapit.Discussion;
import com.example.chagnoda.mapit.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class ChatArrayAdapter extends ArrayAdapter<Discussion> {

    private TextView chatText;
    private List<Discussion> chatMessageList = new ArrayList<Discussion>();
    private Context context;

    @Override
    public void add(Discussion object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId ) {
        super(context, textViewResourceId);
        this.context = context;
       // this.chatMessageList = chatMessageList;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public Discussion getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Discussion chatMessageObj = getItem(position);
        SharedPreferences userInfo = getContext().getSharedPreferences("userInfo", 0);
        String user = userInfo.getString("username", "missing");
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(!chatMessageObj.getSender().equals(user)){
            row = inflater.inflate(R.layout.right, parent, false);
        }
        else{
            row = inflater.inflate(R.layout.left, parent, false);
        }
        /*
        if (chatMessageObj.getLeft()) {
            row = inflater.inflate(R.layout.right, parent, false);
        }else{
            row = inflater.inflate(R.layout.left, parent, false);
        }
        */
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.getSender()+": "+chatMessageObj.getDiscussionContent().toString());
        return row;
    }
}