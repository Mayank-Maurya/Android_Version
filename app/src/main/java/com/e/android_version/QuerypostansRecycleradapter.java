package com.e.android_version;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class QuerypostansRecycleradapter extends RecyclerView.Adapter<QuerypostansRecycleradapter.ViewHolder>{


    FirebaseFirestore firebasedb;
    public List<QueryPostAns> queryPostAnsList;

    public QuerypostansRecycleradapter(List<QueryPostAns> queryPostAnsList)
    {
        this.queryPostAnsList=queryPostAnsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.querypostanswers_listitem,parent,false);

        firebasedb=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QueryPostAns item=queryPostAnsList.get(position);
        String content=queryPostAnsList.get(position).getContent();
        String name=queryPostAnsList.get(position).getName();
        String userid=queryPostAnsList.get(position).getUserId();
        int dvote=queryPostAnsList.get(position).getDownvote();
        int uvote=queryPostAnsList.get(position).getUpvote();

        holder.setName(name);
        holder.setQuestionContent(content);
        holder.setUpvote(uvote);
        holder.setDownvote(dvote);
        holder.setProfileimg(userid);
        holder.questionContent.setText(item.getContent());

        holder.questionContent.setOnStateChangeListener(isShrink -> {

            QueryPostAns item1=queryPostAnsList.get(position);
            item1.setShrink(isShrink);
            queryPostAnsList.set(position,item1);
        });
        holder.questionContent.setText(item.getContent());
        holder.questionContent.resetState(item.isShrink());
    }


    @Override
    public int getItemCount() {
        return queryPostAnsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private View view;
        private ExpandableTextView questionContent;
        private TextView downvote;
        private TextView upvote;
        private TextView name;
        private CircleImageView profileimg;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            questionContent=view.findViewById(R.id.querypostans_content);
            name=view.findViewById(R.id.querypostans_username);
            upvote=view.findViewById(R.id.querypostans_upvote);
            downvote=view.findViewById(R.id.querypostans_downvote);
            profileimg=view.findViewById(R.id.querypostanswers_imageview);
        }



        public void setQuestionContent(String questionContent1)
        {

            questionContent.setText(questionContent1);
        }
        public void setName(String name1)
        {
            name.setText(name1);
        }
        public void setUpvote(int upvote1)
        {
            upvote.setText(String.valueOf(upvote1));
        }
        public void setDownvote(int downvote1)
        {
            upvote.setText(String.valueOf(downvote1));
        }

        public void setProfileimg(String profileimg1) {


            firebasedb.collection("users")
                    .document(profileimg1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                if (task.getResult().getString("profileimg") != null)
                                {


                                    RequestOptions options= new RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.drawable.profile_placeholder)
                                            .error(R.drawable.profile_placeholder)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .priority(Priority.HIGH)
                                            .dontAnimate()
                                            .dontTransform();

                                    Glide.with(view)
                                            .load(task.getResult().getString("profileimg"))
                                            .apply(options)
                                            .placeholder(R.drawable.profile_placeholder)
                                            .into(profileimg);


                                }
                            }

                        }
                    });





        }

        @Override
        public void onClick(View view) {
        }



    }
}
