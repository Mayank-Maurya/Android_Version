package com.e.android_version;

public class QueryPostAns {

    public String content,name,userId;
    public int downvote,upvote;
    public boolean isShrink;


    public QueryPostAns(){}
    public QueryPostAns(String userId,String content,String name, int downvote, int upvote,boolean isShrink) {
        this.userId = userId;
        this.name=name;
        this.content=content;
        this.downvote=downvote;
        this.upvote=upvote;
        this.isShrink=isShrink;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent(){return content;}
    public String getName() {
        return name;
    }

    public int getDownvote() {
        return downvote;
    }

    public int getUpvote() {
        return upvote;
    }
   public boolean isShrink()
    {
        return isShrink;
    }

    public void setShrink(boolean s) {
        this.isShrink=s;
    }
}



