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

import com.droidevils.hired.User.LoginActivity;
import com.droidevils.hired.R;

public class SplashActivity extends AppCompatActivity {

    private static int ANIM_DURATION = 2000;
    private static int SPLASH_SCREEN = 5000;
    private Animation topAnim, bottomAnim;
    private ImageView appLogo;
    private TextView appTitle, appSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        appLogo = (ImageView) findViewById(R.id.start_main_logo);
        appTitle = (TextView) findViewById(R.id.start_app_title);
        appSlogan = (TextView) findViewById(R.id.start_app_slogan);

        // Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        topAnim.setDuration(ANIM_DURATION);
        bottomAnim.setDuration(ANIM_DURATION);
        ;
        appLogo.setAnimation(topAnim);
        appTitle.setAnimation((bottomAnim));
        appSlogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                Pair[] pair = new Pair[2];
                pair[0] = new Pair<View, String>(appLogo, "main_logo");
                pair[1] = new Pair<View, String>(appTitle, "app_title");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pair);

                startActivity(intent, options.toBundle());
                finish();
            }
        }, SPLASH_SCREEN);

    }
}