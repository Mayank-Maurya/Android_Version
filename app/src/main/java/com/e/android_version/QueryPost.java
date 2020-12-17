package com.e.android_version;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;

public class QueryPost {

    public String userid,title,name;
    public int likes,views,comments;
   // public FieldValue timestamp;
   // public Arrays tags[];


    public QueryPost(){}

    public QueryPost(String userid,String title,String name, int likes, int views, int comments) {
        this.userid = userid;
        this.title = title;
        this.name=name;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
        //this.timestamp = timestamp;
       // this.tags = tags;
    }

    public String getUserid() {
        return userid;
    }

    public String getQuestiontitle() {
        return title;
    }
    public String getName() {
        return name;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public int getComments() {
        return comments;
    }

  //  public FieldValue getTimestamp() {
        //return timestamp;
    //}

//    public Arrays[] getTags() {
//        return tags;
//    }



}
