package com.e.android_version;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Fragment_home extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView querypost_recyclerView;
    private List<QueryPost> queryPostList;

    private FirebaseFirestore firebaseFirestore;
    private QuerypostRecyclerAdapter querypostRecyclerAdapter;

    public Fragment_home() {

    }

    public static Fragment_home newInstance(String param1, String param2) {
        Fragment_home fragment = new Fragment_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);


        queryPostList=new ArrayList<>();
        querypost_recyclerView=view.findViewById(R.id.Query_post_recyclerview);

        querypostRecyclerAdapter=new QuerypostRecyclerAdapter(queryPostList);

        querypost_recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        querypost_recyclerView.setAdapter(querypostRecyclerAdapter);


        firebaseFirestore=FirebaseFirestore.getInstance();



        firebaseFirestore.collection("question")
                .addSnapshotListener((value, error) -> {
                    if(isAdded()){

                        if(error!=null)
                        {
                            Toast.makeText(getContext(),"Nothing found",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(DocumentChange doc: value.getDocumentChanges())
                        {

                            if(doc.getType() == DocumentChange.Type.ADDED)
                            {
                                QueryPost queryPost=doc.getDocument().toObject(QueryPost.class);
                                queryPostList.add(queryPost);
                                querypostRecyclerAdapter.notifyDataSetChanged();

                            }

                        }

                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);









//        String[] Query_Post_List={"mayank is in trouble","he is awesome","help needed"};
//        recyclerView=view.findViewById(R.id.Query_post_recyclerview);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new QueryPostAdapter(Query_Post_List);
//        recyclerView.setAdapter(adapter);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new ListingAdapter(mListing);
//        mRecyclerView.setAdapter(mAdapter);
    }
}