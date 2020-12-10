package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private CircleImageView nav_image_view;
    private FirebaseFirestore firebasedb;
    private TextView name_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        firebasedb=FirebaseFirestore.getInstance();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        NavController navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        name_tv= header.findViewById(R.id.nav_view_name);
        nav_image_view=header.findViewById(R.id.circle_image_view);
        if(mAuth.getCurrentUser()!=null)
        {
            firebasedb.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                    name_tv.setText(task.getResult().get("name").toString());
                }

            });
        }

    }
    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser==null)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.loginbutton:

                if(mAuth.getCurrentUser()!=null)
                {
                    Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    this.finish();
                }

                return true;
            case R.id.logoutbutton:

                if(mAuth.getCurrentUser()!=null)
                {
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    this.finish();
                }else {
                    Toast.makeText(MainActivity.this,"you haven't logged in",Toast.LENGTH_SHORT).show();
                }
                return true;

            default:return true;
        }

    }
}