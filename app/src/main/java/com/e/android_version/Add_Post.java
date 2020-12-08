package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Add_Post extends AppCompatActivity {
    Button addpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__post);
        addpost=findViewById(R.id.btn_addpost);
        addpost.setOnClickListener(view -> Add_Post.this.finish());
    }
}