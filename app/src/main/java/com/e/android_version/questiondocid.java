package com.e.android_version;

import com.google.firebase.firestore.Exclude;

public class questiondocid {


    @Exclude
    public String questiondocid;
    public <T extends questiondocid>T withid(final String id)
    {
        this.questiondocid=id;
        return (T) this;
    }

}
