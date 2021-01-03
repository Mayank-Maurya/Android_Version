package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_view extends AppCompatActivity {
    private Uri Profileimageuri=null;
    private CircleImageView profileimage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebasedb;
    private TextView nametv;
    private TextView emailtv;
    private TextView totalviews;
    private TextView totallikes;
    private TextView queryasked;
    private String user=null;
    private String mParam1;
    private String mParam2;
    private RecyclerView profilerecyclerview;
    private List<QueryPostprofile> queryPostprofileList;
    private Querypostprofileadapter querypostprofileadapter;
    private DocumentSnapshot lastvisible;
    private Boolean firstpageload=true;
    private TextView Activity,about;
    private Boolean isactivityclicked=true;
    private Boolean isaboutclicked=false;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
    }
}