package com.example.david.mapit;
import android.media.Image;
/**
 * Created by David on 22/03/2016.
 */
public class Profiles {
    public String userName;
    public Image profilePicture;
    public Profiles[] friends;
    public Photos[] photos;
   // public Groups[] actualGroups;
   // public Groups[] groupsHistory;


    public Profiles(String userName, Image profilePicture) {
        this.userName = userName;
        this.profilePicture = profilePicture;
    }
    public String getUserName(){
        return this.userName;
    }
    public Image getProfilePicture(){return this.profilePicture;}
    public void setUserName(String newUserName) {this.userName = newUserName;}
    public void setProfilePicture(Image newProfilePicture){this.profilePicture = newProfilePicture;}

    }




