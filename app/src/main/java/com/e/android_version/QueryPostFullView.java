package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QueryPostFullView extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName;
    private ImageView mainquesionImage;
    private TextView mainquestiontext;
    private Button submitAnswer;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebasedb;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_post_full_view);

        id=getIntent().getStringExtra("doc_id");
        userImage=findViewById(R.id.querypostfullview_useriv);
        userName=findViewById(R.id.querypostfullview_usernametv);
        mainquesionImage=findViewById(R.id.querypostfullview_mainimage);
        mainquestiontext=findViewById(R.id.querypostfullview_mainquestion);
        submitAnswer=findViewById(R.id.querypostfullview_writebtn);
        firebaseAuth=FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        FirebaseUser isuserexists=firebaseAuth.getCurrentUser();

        submitAnswer.setOnClickListener(view -> {
            Intent intent=new Intent(this,QueryPostAnswers.class);
            intent.putExtra("question_id",id);
            startActivity(intent);
        });


        if(isuserexists!=null)
        {
            new MyTask().doInBackground(id);
            new MyTask2().doInBackground(id);
            Map<String,Object> mp=new HashMap<>();
            mp.put("timestamp", FieldValue.serverTimestamp());
            firebasedb.collection("question")
                    .document(id)
                    .collection("views")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .addSnapshotListener((value, error) -> {
                        if(!value.exists())
                        {
                            firebasedb.collection("question")
                                    .document(id)
                                    .collection("views")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .set(mp)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            firebasedb.collection("question")
                                                    .document(id)
                                                    .update("views",FieldValue.increment(1));
                                            firebasedb.collection("question")
                                                    .document(id)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                String x=task.getResult().getString("userId");
                                                                firebasedb.collection("users")
                                                                        .document(x)
                                                                        .update("total_views",FieldValue.increment(1));
                                                            }
                                                        }
                                                    });
                                        }
                                    });


                        }

                    });

        }
    }


    private class MyTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url=strings[0];
            return doFetch(url);
        }

        private String doFetch(String url) {
            firebasedb.collection("question")
                    .document(url)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            firebasedb.collection("users")
                                    .document(task.getResult().get("userId").toString())
                                    .get()
                                    .addOnCompleteListener(task1 -> {

                                        RequestOptions requestOptions=new RequestOptions()
                                                .centerCrop()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .priority(Priority.HIGH)
                                                .dontAnimate()
                                                .dontTransform();
                                        Glide.with(getApplicationContext())
                                                .load(task1.getResult().getString("profileimg"))
                                                .apply(requestOptions)
                                                .into(userImage);

                                       // userEmail.setText(task1.getResult().getString("email"));
                                        userName.setText(task1.getResult().getString("name"));
                                    });

                        }
                    });
            return "";

        }
    }
    private class MyTask2 extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url=strings[0];
            return doFetch(url);
        }

        private String doFetch(String url) {
            firebasedb.collection("question").document(url)
                    .get()
                    .addOnCompleteListener(task -> {

                        if(! task.getResult().getString("mainQuestion").isEmpty())
                        {
                            String msgtext=task.getResult().getString("mainQuestion")+"\n";
                            mainquestiontext.setText(msgtext);
                            //getting question image
                            {
                                RequestOptions requestOptions=new RequestOptions()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(Priority.HIGH)
                                        .dontAnimate()
                                        .dontTransform();
                                Glide.with(getApplicationContext())
                                        .load(task.getResult().getString("profileimg"))
                                        .apply(requestOptions)
                                        .into(mainquesionImage);

                            }
                        }

                    });

            return "";

        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser==null)
        {
            startActivity(new Intent(QueryPostFullView.this, LoginActivity.class));
            finish();
        }else{

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}