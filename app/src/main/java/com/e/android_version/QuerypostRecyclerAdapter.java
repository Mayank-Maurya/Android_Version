package com.e.android_version;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuerypostRecyclerAdapter extends RecyclerView.Adapter<QuerypostRecyclerAdapter.ViewHolder> {


   private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Context context;
    public List<QueryPost> queryPostList;
    public QuerypostRecyclerAdapter(List<QueryPost> queryPostList)
    {
        this.queryPostList=queryPostList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.queryasked_list_item,parent,false);

        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String userId=firebaseAuth.getCurrentUser().getUid();
        String title=queryPostList.get(position).getQuestiontitle();
        int views=queryPostList.get(position).getViews();
        int comments=queryPostList.get(position).getComments();
        String name=queryPostList.get(position).getName();
        String userid=queryPostList.get(position).getUserId();
       final String questionId=queryPostList.get(position).getQuestionId();
        holder.setQuestionTitle(title);
        holder.setviews(views);
        holder.setComments(comments);
        holder.setName(name);
        holder.setProfileimg(userid);
        firebaseFirestore.collection("question")
                .document(questionId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        holder.setLikes( value.getLong("likes").intValue());
                    }
                });

        holder.questionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,QueryPostFullView.class);
                intent.putExtra("doc_id",questionId);
                context.startActivity(intent);
            }
        });

        firebaseFirestore.collection("question")
                .document(questionId)
                .collection("likes")
                .document(userId)
                .addSnapshotListener((value, error) -> {

                    if(value.exists())
                    {
                        holder.likesbtn.setImageDrawable(context.getDrawable(R.drawable.ic_like2));
                    }else{
                        holder.likesbtn.setImageDrawable(context.getDrawable(R.drawable.ic_like));

                    }

                });




        holder.likesbtn.setOnClickListener(view -> {
            firebaseFirestore.collection("question")
                    .document(questionId)
                    .collection("likes")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(!task.getResult().exists())
                        {
                            Map<String,Object> mp=new HashMap<>();
                            mp.put("timestamp",FieldValue.serverTimestamp());
                            firebaseFirestore.collection("question")
                                    .document(questionId)
                                    .collection("likes")
                                    .document(userId)
                                    .set(mp);
                            firebaseFirestore.collection("question")
                                    .document(questionId)
                                    .update("likes",FieldValue.increment(1));
                        }else{
                            firebaseFirestore.collection("question")
                                    .document(questionId)
                                    .collection("likes")
                                    .document(userId)
                                    .delete();
                            firebaseFirestore.collection("question")
                                    .document(questionId)
                                    .update("likes",FieldValue.increment(-1));

                        }
                    });


        });

        //holder.setTime(datestring);

    }

    @Override
    public int getItemCount() {
        return queryPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private View view;
        private TextView questionTitle;
        private TextView likes;
        private TextView views;
        private TextView comments;
        private TextView name;
        private CircleImageView profileimg;
        private ImageView likesbtn;
       // private TextView timestamp;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view=itemView;
            questionTitle=view.findViewById(R.id.querypost_asked_question);
            likes=view.findViewById(R.id.querypost_likes);
            views=view.findViewById(R.id.querypost_views);
            comments=view.findViewById(R.id.querypost_answers);
            name=view.findViewById(R.id.querypost_username);
            profileimg=view.findViewById(R.id.querypost_circle_view);
            likesbtn=view.findViewById(R.id.querypost_likesimage);






        }


        public void setQuestionTitle(String text)
        {


            questionTitle.setText(text);
        }
        public void setLikes(int likes_no)
        {

            likes.setText(String.valueOf(likes_no));

        }
        public void setviews(int views_no)
        {


            views.setText(String.valueOf(views_no));

        }
        public void setComments(int comments_no)
        {
            comments.setText(String.valueOf(comments_no));
        }
        public void setName(String name1)
        {
            name.setText(name1);
        }
        public void setProfileimg(String user_id)
        {
            firebaseFirestore.collection("users").document(user_id)
                    .get()
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful())
                        {
                            if(task.getResult().getString("profileimg") != null)
                            {
                                RequestOptions options= new RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.drawable.profile_placeholder)
                                        .error(R.drawable.profile_placeholder)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(Priority.HIGH)
                                        .dontAnimate()
                                        .dontTransform();

                                Glide.with(context)
                                        .load(task.getResult().getString("profileimg"))
                                        .apply(options)
                                        .placeholder(R.drawable.profile_placeholder)
                                        .into(profileimg);
                            }
                        }
                    });
        }
    }
}
