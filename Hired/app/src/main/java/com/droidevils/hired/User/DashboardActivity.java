package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.droidevils.hired.Helper.DashboardAdapter.CategoryAdapter;
import com.droidevils.hired.Helper.DashboardAdapter.CategoryHelper;
import com.droidevils.hired.Helper.DashboardAdapter.FeaturedServiceAdapter;
import com.droidevils.hired.Helper.DashboardAdapter.MostViewedServiceAdapter;
import com.droidevils.hired.Helper.DashboardAdapter.ServiceHelper;
import com.droidevils.hired.Helper.UserBean;
import com.droidevils.hired.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    RecyclerView featuredServiceRecycler, mostViewedRecycler, categoryRecycler;
    RecyclerView.Adapter featuredServiceAdapter, mostViewedServiceAdapter, categoryAdapter;

    //Drawer Menu
    ImageView menuButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Check If user is Logged in
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Welcome Back !", Toast.LENGTH_LONG).show();
        }

        featuredServiceRecycler = findViewById(R.id.featured_service_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoryRecycler = findViewById(R.id.category_recycler);
        menuButton = findViewById(R.id.menu_button);
        contentView = findViewById(R.id.content);

        //Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_home);

        //Control Menu Items
        Menu navigationMenu = navigationView.getMenu();
        navigationMenu.findItem(R.id.nav_login).setVisible(false);
        navigationMenu.findItem(R.id.nav_signup).setVisible(false);

        //Navigation Drawer
        navigationDrawer();

        featuredServiceRecycler();
        mostViewedRecycler();
        categoryRecycler();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    //Navigation Functions
    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setScrimColor(getColor(R.color.blue_200));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(UserBean.KEY_USER_TYPE, ProfileActivity.USER_PROFILE);
                bundle.putString(UserBean.KEY_USER_ID, currentUser.getUid());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Recycler View
    private void featuredServiceRecycler() {
        featuredServiceRecycler.setHasFixedSize(true);
        featuredServiceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ServiceHelper> featureServices = new ArrayList<>();
        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 3.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 4.0, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 4.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));

        featuredServiceAdapter = new FeaturedServiceAdapter(featureServices);
        featuredServiceRecycler.setAdapter(featuredServiceAdapter);
    }

    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ServiceHelper> mostViewedServices = new ArrayList<>();
        mostViewedServices.add(new ServiceHelper(R.drawable.service1, (float) 3.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        mostViewedServices.add(new ServiceHelper(R.drawable.service1, (float) 4.0, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        mostViewedServices.add(new ServiceHelper(R.drawable.service1, (float) 4.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));

        mostViewedServiceAdapter = new MostViewedServiceAdapter(mostViewedServices);
        mostViewedRecycler.setAdapter(mostViewedServiceAdapter);
    }

    private void categoryRecycler() {
        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

        ArrayList<CategoryHelper> categoryHelpers = new ArrayList<>();
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Marketing", gradient1));
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Social", gradient2));
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Education", gradient3));

        categoryAdapter = new CategoryAdapter(categoryHelpers);
        categoryRecycler.setAdapter(categoryAdapter);
    }

    public void gotoProfileSetupActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
        startActivity(intent);
    }

    public void gotoProfileActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
}