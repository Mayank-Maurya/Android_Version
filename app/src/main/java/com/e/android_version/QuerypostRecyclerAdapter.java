package com.e.android_version;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.sql.Time;
import java.util.List;

public class QuerypostRecyclerAdapter extends RecyclerView.Adapter<QuerypostRecyclerAdapter.ViewHolder> {


    public List<QueryPost> queryPostList;
    public QuerypostRecyclerAdapter(List<QueryPost> queryPostList)
    {
        this.queryPostList=queryPostList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.queryasked_list_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title=queryPostList.get(position).getQuestiontitle();
        int likes=queryPostList.get(position).getLikes();
        int views=queryPostList.get(position).getViews();
        int comments=queryPostList.get(position).getComments();
        String name=queryPostList.get(position).getName();
       // FieldValue timestamp =queryPostList.get(position).getTimestamp();

        holder.setQuestionTitle(title);
        holder.setLikes(likes);
        holder.setviews(views);
        holder.setComments(comments);
        holder.setName(name);
        //holder.setTimestamp(timestamp);

    }

    @Override
    public int getItemCount() {
        return queryPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private View view;
        private TextView questionTitle;
        private TextView likes;
        private TextView views;
        private TextView comments;
        private TextView name;
        //private TextView timestamp;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view=itemView;

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
//        public void setTimestamp(FieldValue timestamp1)
//        {
//            timestamp=view.findViewById(R.id.querypost_timstamp);
//
//            timestamp.setText(String.valueOf(timestamp1));
//
//        }


    }
}
