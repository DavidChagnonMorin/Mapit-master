package com.example.chagnoda.mapit;

import java.util.Date;

/**
 * Created by chengli on 2016-03-23.
 */
public class Discussion {
    private boolean left;
    private Object content;

    private Date timesend;

    private String sender;


    public Discussion(boolean left, String sender , Object content , Date time){

        this.left = left;

        this.sender = sender;

        this.content = content;

        this.timesend = time;
    }

    public Date getTimeSend(){
        return this.timesend;
    }

    public Object getDiscussionContent(){
        return this.content;
    }

    public String getSender(){
        return this.sender;
    }

    public boolean getLeft(){return this.left;}
}
