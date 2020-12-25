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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuerypostRecyclerAdapter extends RecyclerView.Adapter<QuerypostRecyclerAdapter.ViewHolder> {


    FirebaseFirestore firebaseFirestore;
    private Context context;
    private String id="";

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


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String title=queryPostList.get(position).getQuestiontitle();
        int likes=queryPostList.get(position).getLikes();
        int views=queryPostList.get(position).getViews();
        int comments=queryPostList.get(position).getComments();
        String name=queryPostList.get(position).getName();
        String userid=queryPostList.get(position).getUserId();
       final String questionId=queryPostList.get(position).getQuestionId();

       // id=questionId;
       

       // String datestring= queryPostList.get(position).getTimestamp().toString();

       // FieldValue timestamp =queryPostList.get(position).getTimestamp();

        holder.setQuestionTitle(title);
        holder.setLikes(likes);
        holder.setviews(views);
        holder.setComments(comments);
        holder.setName(name);
        holder.setProfileimg(userid);
       // holder.setQuestionId(questionId);

        holder.questionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,QueryPostFullView.class);
                intent.putExtra("doc_id",questionId);
                context.startActivity(intent);
            }
        });

        //holder.setTime(datestring);

    }

    @Override
    public int getItemCount() {
        return queryPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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

        }

        public void setQuestionId(String doc_id)
        {
            id=doc_id;
        }

        public void setQuestionTitle(String text)
        {
            questionTitle=view.findViewById(R.id.querypost_asked_question);

            questionTitle.setText(text);
        }
        public void setLikes(int likes_no)
        {
            likes=view.findViewById(R.id.querypost_likes);

            likes.setText(String.valueOf(likes_no));

        }
        public void setviews(int views_no)
        {
            views=view.findViewById(R.id.querypost_views);


            views.setText(String.valueOf(views_no));

        }
        public void setComments(int comments_no)
        {
            comments=view.findViewById(R.id.querypost_answers);

            comments.setText(String.valueOf(comments_no));

        }
        public void setName(String name1)
        {
            name=view.findViewById(R.id.querypost_username);

            name.setText(name1);

        }

        public void setProfileimg(String user_id)
        {

            profileimg=view.findViewById(R.id.querypost_circle_view);

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


        @Override
        public void onClick(View view) {




        }
//        public void setTime(String date)
//        {
//            timestamp=view.findViewById(R.id.querypost_timstamp);
//
//            timestamp.setText(date);
//
//        }


    }
}
