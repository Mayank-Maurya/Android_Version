package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    private FirebaseFirestore firebasedb;
    private FirebaseAuth firebaseAuth;
    private TextView settings_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch darkmode=findViewById(R.id.darkmodeswitch);
        //EditText namechange=findViewById(R.id.changenameet);
       // Button namechangebtn=findViewById(R.id.changenamebtn);
        settings_about=findViewById(R.id.settings_about_tv);
        firebaseAuth= FirebaseAuth.getInstance();
        firebasedb= FirebaseFirestore.getInstance();
//        namechangebtn.setOnClickListener(view1 -> {
//            String newname=namechange.getText().toString();
//            if(newname.length()>4)
//            {
//                Map<String,Object> m=new HashMap<>();
//                m.put("name",newname);
//                firebasedb.collection("users")
//                        .document(firebaseAuth.getCurrentUser().getUid())
//                        .update(m)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(Settings.this,"Name Updated!",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }else{
//                Toast.makeText(Settings.this,"name must be length of 5",Toast.LENGTH_SHORT).show();
//            }
//        });

        settings_about.setOnClickListener(view -> {
            startActivity(new Intent(Settings.this,App_About.class));
        });



    }
}