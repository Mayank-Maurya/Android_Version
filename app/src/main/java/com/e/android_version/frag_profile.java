package com.e.android_version;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
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
    private String mParam1;
    private String mParam2;
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
        mAuth=FirebaseAuth.getInstance();
        nametv=rootview.findViewById(R.id.Profile_name);
        emailtv=rootview.findViewById(R.id.Profile_email);
        totallikes=rootview.findViewById(R.id.Profile_query_likes);
        totalviews=rootview.findViewById(R.id.Profile_query_Views);
        queryasked=rootview.findViewById(R.id.Profile_query_asked);
        firebasedb=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        String user=mAuth.getCurrentUser().getUid();
        StorageReference image_path=storageReference.child("Profile_Image").child(user+".jpg");
        if(mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            //database
            firebasedb.collection("users").document(id).get().addOnCompleteListener(task -> {
                if(isAdded()){
                    if (task.isSuccessful()) {
                        emailtv.setText(task.getResult().get("email").toString());
                        nametv.setText(task.getResult().get("name").toString());
                        totallikes.setText(String.valueOf(((Long) task.getResult().get("total_answer")).intValue()));
                        totalviews.setText(String.valueOf(((Long) task.getResult().get("total_views")).intValue()));
                        queryasked.setText(String.valueOf(((Long) task.getResult().get("query_asked")).intValue()));
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

                    } else {
                        Toast.makeText(getActivity(), "found nothing", Toast.LENGTH_SHORT).show();
                    }

                }


            });
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

}