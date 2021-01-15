 package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
        checkFirstRun();
        Toolbar toolbar=findViewById(R.id.toolbar);
        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_YES);
        Context context=getApplicationContext();

        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ImageButton opendrawer=findViewById(R.id.imagebutton);
        opendrawer.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
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
                    RequestOptions options= new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .error(R.drawable.profile_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .dontAnimate()
                            .dontTransform();
                    Glide.with(context)
                            .load(task.getResult().get("profileimg").toString())
                            .apply(options)
                            .into(nav_image_view);

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

            case R.id.Settings:
                if(mAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(MainActivity.this,Settings.class));
                }

            default:return true;
        }

    }
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            startActivity(new Intent(MainActivity.this,Onboarding.class));
            finish();

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}