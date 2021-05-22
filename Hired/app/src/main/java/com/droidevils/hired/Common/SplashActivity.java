package com.droidevils.hired.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidevils.hired.Helper.Bean.TempMain;
import com.droidevils.hired.R;
import com.droidevils.hired.User.DashboardActivity;
import com.droidevils.hired.User.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static int ANIM_DURATION = 2000;
    private static int SPLASH_SCREEN = 5000;

    ImageView appLogo;
    TextView developerCredit;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new TempMain().main();

        appLogo = (ImageView) findViewById(R.id.app_icon);
        developerCredit = (TextView) findViewById(R.id.developer_credit);

        // Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        topAnim.setDuration(ANIM_DURATION);
        bottomAnim.setDuration(ANIM_DURATION);

        appLogo.setAnimation(topAnim);
        developerCredit.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(com.droidevils.hired.Common.SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}