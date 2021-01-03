package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnswerSubmit extends AppCompatActivity {
    private String id;
    private EditText AnswerContent;
    private Button AnswerSubmit;
    private FirebaseFirestore firebasedb;
    private FirebaseAuth firebaseAuth;
    private String username;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_submit);
        id=getIntent().getStringExtra("question_id");
        AnswerContent=findViewById(R.id.addanswer_content);
        AnswerSubmit=findViewById(R.id.btn_addanswer);
        firebaseAuth=FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        userid=firebaseAuth.getCurrentUser().getUid();

        firebasedb.collection("users")
                .document(userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        username=task.getResult().getString("name").toString();
                    }
                });

        if(firebaseAuth.getCurrentUser()!=null)
        {
            AnswerSubmit.setOnClickListener(view -> {
                String Answer=AnswerContent.getText().toString();
                Map<String,Object> map=new HashMap<>();
                map.put("content",Answer);
                map.put("upvote",0);
                map.put("downvote",0);
                map.put("name",username);
                map.put("userId",userid);
                map.put("questionID",id);
                map.put("timestamp", FieldValue.serverTimestamp());
                firebasedb.collection("answer")
                        .add(map)
                        .addOnSuccessListener(documentReference -> {
                            Map<String,Object> m=new HashMap<>();
                            m.put("answerID",documentReference.getId());
                            m.put("timestamp",map.get("timestamp"));
                            firebasedb.collection("question")
                                    .document(id)
                                    .collection("answers")
                                    .add(m);
                            firebasedb.collection("users")
                                    .document(userid)
                                    .collection("answers")
                                    .add(m);
                            firebasedb.collection("users")
                                    .document(userid)
                                    .update("total_answer",FieldValue.increment(1));
                            firebasedb.collection("question")
                                    .document(id)
                                    .update("answercount",FieldValue.increment(1));
                        }).addOnCompleteListener(task -> {
                            finish();
                });


//                firebasedb.collection("question")
//                        .document(id)
//                        .collection("answers")
//                        .add(map)
//                        .addOnSuccessListener(documentReference -> {
//                            Map<String,Object> m=new HashMap<>();
//                            m.put("answerID",documentReference.getId());
//                            m.put("timestamp",map.get("timestamp"));
//                            firebasedb.collection("question")
//                                    .document(id)
//                                    .collection("answers")
//                                    .add(m)
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                                            Toast.makeText(AnswerSubmit.this,"up in question",Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                            firebasedb.collection("users")
//                                    .document(userid)
//                                    .collection("answers")
//                                    .add(m)
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                                            Toast.makeText(AnswerSubmit.this,"up in users",Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });

//                        })
//                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentReference> task) {
//                                finish();
//                            }
//                        });


            });

        }




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}