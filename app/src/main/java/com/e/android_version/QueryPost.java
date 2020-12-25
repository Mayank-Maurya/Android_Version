package com.e.android_version;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.Date;

public class QueryPost {

    public String userId,title,name,questionId;
    public int likes,views,comments;
    //public Date timestamp;
   // public Arrays tags[];

    public QueryPost(){}


    public QueryPost(String userId,String title,String name, int likes, int views, int comments,String questionId) {
        this.userId = userId;
        this.title = title;
        this.name=name;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
        this.questionId=questionId;
       // this.timestamp = timestamp;
       // this.tags = tags;
    }

    public String getUserId() {
        return userId;
    }
    public String getQuestionId(){
        return questionId;
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

//    public Date getTimestamp() {
//        return timestamp;
//    }
//    public void setTimestamp(Date timestamp)
//    {
//        this.timestamp=timestamp;
//    }



}
