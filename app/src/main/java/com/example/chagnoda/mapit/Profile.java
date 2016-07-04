package com.example.chagnoda.mapit;
import android.media.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 22/03/2016.
 */
public class Profile {
    private String email;
    private String username;
    private String password;
    private Image profilepicture;
    private String userID;

    public Profile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public String getUserName(){return this.username;}
    public String getPassword(){return this.password;}
    public String getEmail(){return this.email;}
    public Image getProfilePicture(){return this.profilepicture;}
    public String getUserID(){ return this.userID;}
    public void setUserID(String userID){this.userID = userID;}
    public void setUserName(String newUserName) {this.username = newUserName;}
    public void setProfilePicture(Image newProfilePicture){this.profilepicture = newProfilePicture;}



    }




