package com.e.android_version;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.OrderBy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QueryPostAnswers extends AppCompatActivity {
    private String id;
    private RecyclerView querypostans_recyclerView;
    private List<QueryPostAns> queryPostansList;
    private FirebaseFirestore firebaseFirestore;
    private QuerypostansRecycleradapter querypostansRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private DocumentSnapshot lastvisible;
    private Boolean firstpageload=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_post_answers);
        id=getIntent().getStringExtra("question_id");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queryPostansList=new ArrayList<>();
        querypostans_recyclerView=findViewById(R.id.querypostanswers_recyclerview);
        querypostansRecyclerAdapter=new QuerypostansRecycleradapter(queryPostansList);
        querypostans_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        querypostans_recyclerView.setAdapter(querypostansRecyclerAdapter);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            querypostans_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom=!recyclerView.canScrollVertically(1);

                    if(reachedBottom)
                    {
                        Toast.makeText(QueryPostAnswers.this,"Reached",Toast.LENGTH_SHORT).show();
                        loadmorepost();

                    }
                }
            });
            Query firstquery=firebaseFirestore.collection("question")
                    .document(id)
                    .collection("answers")
                    .limit(6);

            firstquery.addSnapshotListener(((value, error) -> {
                if(error!=null)
                {
                    Toast.makeText(this, "Congratulations! you're first one to answer\nNo Answers yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (firstpageload) {
                    try {
                        lastvisible = value.getDocuments().get(value.size() - 1);
                    }catch (ArrayIndexOutOfBoundsException e)
                    {
                        Toast.makeText(this, "Congratulations! you're first one to answer\nNo Answers yet", Toast.LENGTH_SHORT).show();
                    }

                }
                if(value.size()>0)
                {for (DocumentChange doc : value.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        firebaseFirestore.collection("answer")
                                .document(doc.getDocument().getString("answerID"))
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if(documentSnapshot.exists())
                                    {
                                        QueryPostAns queryPostAns=documentSnapshot.toObject(QueryPostAns.class);
                                        if (firstpageload) {
                                            queryPostansList.add(queryPostAns);
                                        } else {
                                            queryPostansList.add(0, queryPostAns);
                                        }
                                        querypostansRecyclerAdapter.notifyDataSetChanged();
                                    }


                                });
                    }
                }
                    firstpageload = false;

                }
            }));
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->{
            Intent intent=new Intent(this,AnswerSubmit.class);
            intent.putExtra("question_id",id);
            startActivity(intent);
            finish();
        });
    }

    void loadmorepost()
    {
        if(lastvisible!=null)
        {
            Query nextquery=firebaseFirestore.collection("question")
                    .document(id)
                    .collection("answers")
                    .startAfter(lastvisible)
                    .limit(6);
            nextquery.addSnapshotListener(((value, error) -> {
                if(error!=null)
                {
                    Toast.makeText(this, "Congratulations! you're first one to answer\nNo Answers yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value.size()>0)
                {
                    lastvisible = value.getDocuments().get(value.size() - 1);
                    if(lastvisible!=null)
                    {
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                firebaseFirestore.collection("answer")
                                        .document(doc.getDocument().getString("answerID"))
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if(documentSnapshot.exists()) {
                                                QueryPostAns queryPostAns = documentSnapshot.toObject(QueryPostAns.class);
                                                queryPostansList.add(queryPostAns);
                                                querypostansRecyclerAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }

                    }

                }
            }));

        }

    }
}