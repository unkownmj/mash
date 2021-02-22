package com.example.imash.Model;

public class BattlePost extends BattleReciverPost {


    private String imageurl1;
    private String postid1;
    private String publisher1;
    private  String imageurl2;
    private String publisher2;
    private String postid2;


    public BattlePost() {
    }

    public BattlePost( String imageurl1, String postid1, String publisher1,String publisher2,String imageurl2 , String postid2) {


        this.imageurl1 = imageurl1;
        this.postid1 = postid1;
        this.publisher1 = publisher1;
        this.publisher2 = publisher2;
        this.imageurl2 = imageurl2;
       // this.postid2 = postid2;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
    }

    public String getPublisher2() {
        return publisher2;
    }

    public void setPublisher2(String publisher2) {
        this.publisher2 = publisher2;
    }

    public String getPostid2() {
        return postid2;
    }

   public void setPostid2(String postid2) {
        this.postid2 = postid2;
    }



    public String getImageurl1() {
        return imageurl1;
    }

    public void setImageurl1(String imageurl1) {
        this.imageurl1 = imageurl1;
    }


    public String getPostid1() {
        return postid1;
    }

    public void setPostid1(String postid) {
        this.postid1 = postid;
    }

    public String getPublisher1() {
        return publisher1;
    }

    public void setPublisher1(String publisher1) {
        this.publisher1 = publisher1;
    }


}

