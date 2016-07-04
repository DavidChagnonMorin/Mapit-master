package com.example.chagnoda.mapit;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chengli on 2016-03-24.
 */
public class Supergroupe extends Groupe {


    private ArrayList<Groupe> channels;


    public Supergroupe(String name , int size , int color , double latitude ,double longitude, String creator, ArrayList<String> members, ArrayList<Discussion> discussions,Date timeoflastmessage){

        super(name ,size ,color ,  latitude , longitude ,creator,discussions,timeoflastmessage);
        Groupe general = new Groupe("general",50, Color.GREEN, latitude , longitude,creator,discussions,timeoflastmessage);
        this.channels.add(this);
    }


    public void addNewGroupeToChannels(Groupe newGroupe){

        this.channels.add(newGroupe);

    }

    public void deleteAGroupeFromChannels(Groupe groupe){
        this.channels.remove(groupe);
    }

}