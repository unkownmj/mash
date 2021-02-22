package com.example.imash.Model;


import android.widget.Button;

public class BattleNotification {

    private String sender_id;
    private String post_id;
    private  String reciver_id;



    public BattleNotification() {
    }

    public BattleNotification(String sender_id, String post_id, Button accept_btn, Button decline_btn) {
        this.sender_id = sender_id;
        this.post_id = post_id;
        this.reciver_id = reciver_id;

    }

    public String getReciver_id() {
        return reciver_id;
    }

    public void setReciver_id(String reciver_id) {
        this.reciver_id = reciver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}

