package com.e.android_version;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Fragment_home extends Fragment {
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView querypost_recyclerView;
    private List<QueryPost> queryPostList;
    private FirebaseFirestore firebaseFirestore;
    private QuerypostRecyclerAdapter querypostRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private DocumentSnapshot lastvisible;
    private Boolean firstpageload=true;
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
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        //Sorting the queries in descending order w.r.t timestamp
        if(firebaseAuth.getCurrentUser()!=null)
        {
            querypost_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean reachedBottom=!recyclerView.canScrollVertically(1);
                    if(reachedBottom)
                    {
                        Toast.makeText(getContext(),"Reached",Toast.LENGTH_SHORT).show();
                        loadpost();
                    }
                }
            });
            Query firstQuery=firebaseFirestore.collection("question")
                    .orderBy("timestamp",Query.Direction.DESCENDING)
                    .limit(6);
            firstQuery.addSnapshotListener((value, error) -> {
                if (Fragment_home.this.isAdded()) {
                    if (error != null) {
                        Toast.makeText(Fragment_home.this.getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (firstpageload) {
                        lastvisible = value.getDocuments().get(value.size() - 1);
                    }
                    for (DocumentChange doc : value.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            QueryPost queryPost = doc.getDocument().toObject(QueryPost.class);
                            if (firstpageload) {
                                queryPostList.add(queryPost);
                            } else {
                                queryPostList.add(0, queryPost);
                            }
                            querypostRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                    firstpageload = false;
                }
            });
        }
        return view;
    }
    public void loadpost()
    {
        Query nextQuery=firebaseFirestore.collection("question")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .startAfter(lastvisible)
                .limit(6);




        nextQuery.addSnapshotListener((value, error) -> {
            if(isAdded()){

                if(error!=null)
                {
                    Toast.makeText(getContext(),"Nothing found",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!value.isEmpty())
                {
                    lastvisible = value.getDocuments().get(value.size() -1);


                    for(DocumentChange doc: value.getDocumentChanges())
                    {

                        if(doc.getType() == DocumentChange.Type.ADDED)
                        {
                           // String querypostid=doc.getDocument().getId();

                            QueryPost queryPost=doc.getDocument().toObject(QueryPost.class);
                            queryPostList.add(queryPost);
                            querypostRecyclerAdapter.notifyDataSetChanged();
                        }

                    }

                }


            }
        });

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState=new Bundle();
        Parcelable liststate=querypost_recyclerView
                .getLayoutManager()
                .onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, liststate);
    }
    @Override
    public void onResume() {
        super.onResume();

        if(mBundleRecyclerViewState!=null)
        {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            querypost_recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        }
}