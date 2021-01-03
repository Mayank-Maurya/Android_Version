package com.e.android_version;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore firebasedb;
    private FirebaseAuth firebaseAuth;

    public frag_settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_settings.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_settings newInstance(String param1, String param2) {
        frag_settings fragment = new frag_settings();
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
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_frag_settings, container, false);
        Switch darkmode=view.findViewById(R.id.darkmodeswitch);
        EditText namechange=view.findViewById(R.id.changenameet);
        Button namechangebtn=view.findViewById(R.id.changenamebtn);
        firebaseAuth=FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        namechangebtn.setOnClickListener(view1 -> {
            String newname=namechange.getText().toString();
            if(newname.length()>4)
            {
                Map<String,Object> m=new HashMap<>();
                m.put("name",newname);
                firebasedb.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .update(m)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(container.getContext(),"Name Updated!",Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                Toast.makeText(container.getContext(),"name must be length of 5",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}