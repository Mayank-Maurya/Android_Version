package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;

public class Add_Post extends AppCompatActivity {

    private Button addpost;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebasedb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__post);
        addpost=findViewById(R.id.btn_addpost);
        firebaseAuth=FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        addpost.setOnClickListener(view -> Add_Post.this.finish());
    }
}