package com.example.chagnoda.mapit;

import android.location.Location;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.graphics.Color;

/**
 * Created by chengli on 2016-03-23.
 */
public class Groupe {

    private int groupesize;

    private int groupecolor;

    private double latitude;

    private double longitude;

    private String creator;

    private ArrayList<String> members;

    private Date timeoflastmessage;

    private ArrayList<Discussion> discussions;

    private String groupename;

    public Groupe(){}

    public Groupe(String name , int size , int color , double latitude ,double longitude, String creator, ArrayList<Discussion> discussions,Date timeoflastmessage){

        this.groupename = name;
        this.groupesize=size;
        this.groupecolor=color;
        this.latitude=latitude;
        this.longitude=longitude;
        this.creator=creator;
        this.discussions = discussions;
        this.timeoflastmessage = timeoflastmessage;

    }

    public String getGroupeName(){
        return this.groupename;
    }

    public int getGroupeSize(){
        return this.groupesize;
    }

    public int getGroupeColor(){
        return this.groupecolor;
    }

    public ArrayList<String> getMemberList(){
        return this.members;
    }

    public double getGroupeLatitude(){
        return this.latitude;
    }

    public double getGroupeLongitude(){
        return this.longitude;
    }

    public String getCreator(){
        return this.creator;
    }

    public Date getTimeOfLastMessage(){
        return this.timeoflastmessage;
    }

    public void addDiscussionToGroupe(Discussion d){
        this.discussions.add(d);
    }


    public void extendGroupeSize(int newsize){
        this.groupesize+=newsize;
    }








}