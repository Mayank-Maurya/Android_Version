package com.e.android_version;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class Querypostprofileadapter extends RecyclerView.Adapter<Querypostprofileadapter.ViewHolder>{
    public List<QueryPostprofile> queryPostprofileList;
    FirebaseFirestore firebasedb;
    public Querypostprofileadapter(List<QueryPostprofile> queryPostprofileList)
    {
        this.queryPostprofileList=queryPostprofileList;
    }
    @NonNull
    @Override
    public Querypostprofileadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.querypostprofile_listitem,parent,false);
        firebasedb= FirebaseFirestore.getInstance();
        return new Querypostprofileadapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Querypostprofileadapter.ViewHolder holder, int position) {
        final QueryPostprofile item=queryPostprofileList.get(position);
        if(item!=null)
        {
            holder.questionContent.setText(item.getMainQuestion());
            holder.questionContent.setOnStateChangeListener(isShrink -> {
                QueryPostprofile item1=queryPostprofileList.get(position);
                item1.setShrink(isShrink);
                queryPostprofileList.set(position,item1);
            });
            holder.questionContent.setText(item.getMainQuestion());
            holder.questionContent.resetState(item.isShrink());
            holder.likes.setText(String.valueOf(item.getLikes()));
            holder.views.setText(String.valueOf(item.getViews()));
            holder.answers.setText(String.valueOf(item.getAnswercount()));

        }

    }
    @Override
    public int getItemCount() {
        return queryPostprofileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private ExpandableTextView questionContent;
        private TextView likes;
        private TextView views;
        private TextView answers;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            questionContent=view.findViewById(R.id.querypostprofile_content);
            likes=view.findViewById(R.id.querypostprofile_likes);
            views=view.findViewById(R.id.querypostprofile_views);
            answers=view.findViewById(R.id.querypostprofile_answers);
        }
    }


}
