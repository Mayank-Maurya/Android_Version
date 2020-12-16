package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Array;
import java.net.URI;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Post extends AppCompatActivity {

    private Button addpost;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebasedb;
    private EditText Title;
    private EditText Body;
    private EditText Tag1;
    private EditText Tag2;
    private EditText Tag3;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ImageView imageView;
    private Uri post_img_uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__post);
        addpost=findViewById(R.id.btn_addpost);
        Title=findViewById(R.id.addpost_ettitle);
        Body=findViewById(R.id.addpost_etbody);
        Tag1=findViewById(R.id.addpost_tag1);
        Tag2=findViewById(R.id.addpost_tag2);
        Tag3=findViewById(R.id.addpost_tag3);
        imageView=findViewById(R.id.addpost_profile_image);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        String name=firebasedb.collection("users")
                .document(firebaseAuth.getCurrentUser()
                        .getUid())
                .get()
                .getResult().get("name").toString();

        imageView.setOnClickListener(view -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
                });



        addpost.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if(firebaseAuth.getCurrentUser()!=null)
            {

                String title=Title.getText().toString();
                String body=Body.getText().toString();
                String tag1=Tag1.getText().toString();
                String tag2=Tag2.getText().toString();
                String tag3=Tag3.getText().toString();
                Map<String,Object> dataset=new HashMap<>();
                dataset.put("title",title);
                dataset.put("mainQuestion",body);
                dataset.put("timestamp",new Timestamp(new Date()));
                dataset.put("tags",Arrays.asList(tag1,tag2,tag3));
                dataset.put("answercount",0);
                dataset.put("views",0);
                dataset.put("likes",0);
                dataset.put("postimg",0);
                dataset.put("name",name);
                dataset.put("userId",firebaseAuth.getCurrentUser().getUid());
//                String s=firebasedb.collection("users")
//                        .document(firebaseAuth.getCurrentUser()
//                                .getUid())
//                        .get()
//                        .getResult().get("name").toString();
//
//                dataset.put("username",s);
                firebasedb.collection("question")
                        .add(dataset)
                        .addOnCompleteListener(task -> {

                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Add_Post.this,"Successfully asked a question",Toast.LENGTH_SHORT).show();
                                this.finish();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Add_Post.this,"!Successfully asked a question",Toast.LENGTH_SHORT).show();
                                this.finish();
                            }

                        });



                Map<String,Object> dataset1=new HashMap<>();
                dataset1.put("name",name);

                StorageReference image_path = storageReference.child("post_images").child("mayank.jpg");

                image_path.putFile(post_img_uri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Add_Post.this, "picture uploaded", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Add_Post.this, "picture Failed to upload", Toast.LENGTH_SHORT).show();
                    }

                });






            }else{

                Toast.makeText(Add_Post.this,"User haven't logged in.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Post.this, LoginActivity.class));
                this.finish();
            }


        });







    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                post_img_uri = result.getUri();

                imageView.setImageURI(post_img_uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}