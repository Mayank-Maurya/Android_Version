package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        TextView tv = findViewById(R.id.dialog);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashtrans);
        tv.startAnimation(animation);
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (currentVersionCode == savedVersionCode) {
                        final Intent i = new Intent(LaunchScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        return;

                    } else if (savedVersionCode == DOESNT_EXIST) {
                        final Intent i = new Intent(LaunchScreen.this, Onboarding.class);
                        startActivity(i);
                        finish();
                    } else if (currentVersionCode > savedVersionCode) {
                        // TODO This is an upgrade
                    }

                }
            }
        };
        timer.start();
    }
}