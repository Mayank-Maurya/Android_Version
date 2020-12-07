package com.e.android_version;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
    private FirebaseUser current_user=null;
    private FirebaseStorage firebaseStorage;


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
        current_user=mAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        if(current_user!=null)
        {
//            String user=current_user.getUid();
//            String image=firebaseStorage.getDownloadUrl("gs://android-version-91d30.appspot.com/Profile_Image/").
//            Glide.with(frag_profile.this)
//                    .load(image)
//                    .into(profileimage);
        }
        profileimage.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(getContext(),frag_profile.this));

        return rootview;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Profileimageuri= result.getUri();
                String user=mAuth.getCurrentUser().getUid();
                StorageReference image_path=storageReference.child("Profile_Image").child(user+".jpg");
                image_path.putFile(Profileimageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"picture uploaded",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //profileimage.setImageURI(Profileimageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}