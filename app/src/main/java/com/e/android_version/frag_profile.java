package com.e.android_version;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//perfect version
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class frag_profile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Uri Profileimageuri=null;
    private CircleImageView profileimage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebasedb;
    private TextView nametv;
    private TextView emailtv;
    private TextView totalviews;
    private TextView totallikes;
    private TextView queryasked;
    private TextView contribution,achievement,phone,dob;
    private String user=null;
    private String mParam1;
    private String mParam2;
    private RecyclerView profilerecyclerview;
    private List<QueryPostprofile> queryPostprofileList;
    private Querypostprofileadapter querypostprofileadapter;
    private DocumentSnapshot lastvisible;
    private Boolean firstpageload=true;
    private TextView Activity,about;
    private Boolean isactivityclicked=true;
    private Boolean isaboutclicked=false;
    private ConstraintLayout constraintLayout;


    public frag_profile() {
    }
    public static frag_profile newInstance(String param1, String param2) {
        frag_profile fragment = new frag_profile();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_frag_profile, container, false);
        profileimage= rootview.findViewById(R.id.Profile_image);
        queryPostprofileList=new ArrayList<>();
        profilerecyclerview=rootview.findViewById(R.id.Profile_recylerview);
        querypostprofileadapter=new Querypostprofileadapter(queryPostprofileList);
        profilerecyclerview.setLayoutManager(new LinearLayoutManager(container.getContext()));
        profilerecyclerview.setAdapter(querypostprofileadapter);
        mAuth=FirebaseAuth.getInstance();
        nametv=rootview.findViewById(R.id.Profile_name);
        constraintLayout=rootview.findViewById(R.id.cl_about);
        emailtv=rootview.findViewById(R.id.Profile_email);
        totallikes=rootview.findViewById(R.id.Profile_query_likes);
        totalviews=rootview.findViewById(R.id.Profile_query_Views);
        Activity=rootview.findViewById(R.id.tvactivity);
        about=rootview.findViewById(R.id.tvaAbout);
        contribution=rootview.findViewById(R.id.profileabout_contributions);
        achievement=rootview.findViewById(R.id.profileabout_achievement);
        dob=rootview.findViewById(R.id.profileabout_dob);
        phone=rootview.findViewById(R.id.profileabout_phoneno);
        queryasked=rootview.findViewById(R.id.Profile_query_asked);
        firebasedb=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        user=mAuth.getCurrentUser().getUid();
        StorageReference image_path=storageReference.child("Profile_Image").child(user+".jpg");

        if(isactivityclicked)
        {
            Activity.setBackgroundResource(R.color.navy_blue);
        }
        if(isaboutclicked)
        {
            about.setBackgroundResource(R.color.navy_blue);
        }
        about.setOnClickListener(view -> {
            if(isactivityclicked)
            {
                profilerecyclerview.setAlpha(0);
                isactivityclicked=false;
                isaboutclicked=true;
                constraintLayout.setVisibility(View.VISIBLE);
                about.setBackgroundResource(R.color.navy_blue);
                Activity.setBackgroundResource(R.color.white);

            }
        });
        Activity.setOnClickListener(view -> {
            if(isaboutclicked)
            {
                profilerecyclerview.setAlpha(1);
                isaboutclicked=false;
                isactivityclicked=true;
                constraintLayout.setVisibility(View.GONE);
                about.setBackgroundResource(R.color.white);
                Activity.setBackgroundResource(R.color.navy_blue);
            }

        });

        if(mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            //database
            firebasedb.collection("users").document(id).get().addOnCompleteListener(task -> {
                if(isAdded()){
                    if (task.isSuccessful()) {
                        emailtv.setText(task.getResult().get("email").toString());
                        nametv.setText(task.getResult().get("name").toString());
                        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, ((Long) task.getResult().get("total_answer")).intValue());
                        valueAnimator.setDuration(1500);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                totallikes.setText(valueAnimator.getAnimatedValue().toString());
                            }
                        });
                        valueAnimator.start();
                        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(0, ((Long) task.getResult().get("total_views")).intValue());
                        valueAnimator1.setDuration(1500);
                        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator1) {
                                totalviews.setText(valueAnimator1.getAnimatedValue().toString());
                            }
                        });
                        valueAnimator1.start();
                        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, ((Long) task.getResult().get("query_asked")).intValue());
                        valueAnimator2.setDuration(1500);
                        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                queryasked.setText(valueAnimator2.getAnimatedValue().toString());
                            }
                        });
                        valueAnimator2.start();
//                        totallikes.setText(String.valueOf(((Long) task.getResult().get("total_answer")).intValue()));
//                        totalviews.setText(String.valueOf(((Long) task.getResult().get("total_views")).intValue()));
//                        queryasked.setText(String.valueOf(((Long) task.getResult().get("query_asked")).intValue()));
                        if(task.getResult().get("profileimg").toString() != null)
                        {

                            RequestOptions options= new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .error(R.drawable.profile_placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(Priority.HIGH)
                                    .dontAnimate()
                                    .dontTransform();
                            Glide.with(getContext())
                                    .load(task.getResult().get("profileimg").toString())
                                    .apply(options)
                                    .into(profileimage);

                        }

                        Map<String,Object> aboutmap= (Map<String, Object>) task.getResult().get("about");
                        contribution.setText(aboutmap.get("contribution").toString());
                        achievement.setText(aboutmap.get("achievements").toString());
                        phone.setText(task.getResult().getString("phone"));
                        dob.setText(task.getResult().getString("dob"));
                    } else {
                        Toast.makeText(getActivity(), "found nothing", Toast.LENGTH_SHORT).show();
                    }

                }


            });



           profilerecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            Query firstQuery=firebasedb.collection("users")
                   .document(user)
                    .collection("questions")
                    .limit(3);

            firstQuery.addSnapshotListener(((value, error) -> {
                if(isAdded())
                {
                    if (error != null) {
                        Toast.makeText(frag_profile.this.getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (firstpageload) {
                        lastvisible = value.getDocuments().get(value.size() - 1);
                    }
                    if(!value.isEmpty())
                    {
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                firebasedb.collection("question")
                                        .document(doc.getDocument().getString("questionId"))
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            QueryPostprofile queryPostprofile = documentSnapshot.toObject(QueryPostprofile.class);
                                            if (firstpageload) {
                                                queryPostprofileList.add(queryPostprofile);
                                            } else {
                                                queryPostprofileList.add(0, queryPostprofile);
                                            }
                                            querypostprofileadapter.notifyDataSetChanged();
                                        });
                            }
                        }
                        firstpageload = false;

                    }

                }
            }));
        }

        profileimage.setOnClickListener(view -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(), frag_profile.this);

            new AlertDialog.Builder(getContext())
                    .setTitle("Want to upload")
                    .setMessage("Remember the previous image will be replaced.\n")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Continue with delete operation

                        if(Profileimageuri!=null)
                        {

                            image_path.putFile(Profileimageuri).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                  image_path.getDownloadUrl().addOnSuccessListener(uri -> {

                                      String download=uri.toString();
                                      Toast.makeText(getActivity(),download+"",Toast.LENGTH_SHORT).show();

                                      Map<String , Object> mp=new HashMap<>();
                                      mp.put("profileimg",download);
                                      firebasedb.collection("users").document(mAuth.getCurrentUser().getUid())
                                              .update(mp)
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task1) {
                                                      if(task1.isSuccessful())
                                                      {
                                                          Toast.makeText(getActivity(),"Adderess uploaded",Toast.LENGTH_SHORT).show();

                                                      }


                                                  }
                                              });

                                  });


                                    Toast.makeText(getActivity(),"picture uploaded",Toast.LENGTH_SHORT).show();
                                }

                            });

                        }




                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        });
        return rootview;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Profileimageuri= result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    void loadpost()
    {
        if(lastvisible!=null && user!=null) {

            Query firstQuery=firebasedb.collection("users")
                    .document(user)
                    .collection("questions")
                    .startAfter(lastvisible)
                    .limit(3);

            firstQuery.addSnapshotListener(((value, error) -> {
                if(isAdded())
                {
                    if (error != null) {
                        Toast.makeText(frag_profile.this.getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!value.isEmpty())
                    {
                            lastvisible = value.getDocuments().get(value.size() - 1);
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                firebasedb.collection("question")
                                        .document(doc.getDocument().getString("questionId"))
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            QueryPostprofile queryPostprofile = documentSnapshot.toObject(QueryPostprofile.class);
                                            queryPostprofileList.add(queryPostprofile);
                                            querypostprofileadapter.notifyDataSetChanged();
                                        });
                            }
                        }

                    }

                }
            }));
        }

    }

}