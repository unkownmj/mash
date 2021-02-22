package com.example.imash.Model;

public class BattleReciverPost {

    private String imageUrlBr;
    private  String postid;
    private  String acceptUserID;

    public BattleReciverPost() {
    }

    public BattleReciverPost(String imageUrlBr) {
        this.imageUrlBr = imageUrlBr;
        this.postid = postid;
        this.acceptUserID = acceptUserID;
    }

    public String getImageUrlBr() {
        return imageUrlBr;
    }

    public void setImageUrlBr(String imageUrlBr) {
        this.imageUrlBr = imageUrlBr;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getAcceptUserID() {
        return acceptUserID;
    }

    public void setAcceptUserID(String acceptUserID) {
        this.acceptUserID = acceptUserID;
    }
}
