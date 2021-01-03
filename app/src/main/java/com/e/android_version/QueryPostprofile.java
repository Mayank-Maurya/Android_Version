package com.e.android_version;

public class QueryPostprofile {
    public String mainQuestion;
    public int likes,answercount,views;
    public boolean isShrink;
    public QueryPostprofile(){}
    public QueryPostprofile(String mainQuestion,int views, int likes,boolean isShrink,int answercount) {
        this.mainQuestion=mainQuestion;
        this.views=views;
        this.likes=likes;
        this.answercount=answercount;
        this.isShrink=isShrink;
    }


    public String getMainQuestion(){return mainQuestion;}
    public int getLikes() {
        return likes;
    }
    public int getViews() {
        return views;
    }
    public int getAnswercount(){return answercount;}
    public boolean isShrink()
    {
        return isShrink;
    }
    public void setShrink(boolean s) {
        this.isShrink=s;
    }




}
