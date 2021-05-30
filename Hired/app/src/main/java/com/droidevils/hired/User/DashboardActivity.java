package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidevils.hired.Helper.Bean.Category;
import com.droidevils.hired.Helper.Bean.CategoryInterface;
import com.droidevils.hired.Helper.Adapter.CategoryAdapter;
import com.droidevils.hired.Helper.Adapter.CategoryHelper;
import com.droidevils.hired.Helper.Adapter.FeaturedServiceAdapter;
import com.droidevils.hired.Helper.Adapter.MostViewedServiceAdapter;
import com.droidevils.hired.Helper.Adapter.ServiceHelper;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Bean.UserLocation;
import com.droidevils.hired.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    static final float END_SCALE = 0.7f;

    //TODO Notification

    RecyclerView featuredServiceRecycler, mostViewedRecycler, categoryRecycler;
    RecyclerView.Adapter featuredServiceAdapter, mostViewedServiceAdapter, categoryAdapter;

    SearchView searchView;

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

        //Update Location
        updateLocationToFirebase();

        featuredServiceRecycler = findViewById(R.id.featured_service_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoryRecycler = findViewById(R.id.category_recycler);
        menuButton = findViewById(R.id.menu_button);
        contentView = findViewById(R.id.content);
        searchView = findViewById(R.id.search_view);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SearchActivity.SEARCH_TYPE, SearchActivity.QUERY_SEARCH);
                bundle.putString(SearchActivity.SEARCH_ID, query);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void updateLocationToFirebase() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DashboardActivity.this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    UserLocation userLocation = new UserLocation(location.getLatitude(), location.getLongitude());
                    FirebaseDatabase.getInstance().getReference("Location").child(currentUser.getUid()).setValue(userLocation);
                }
            }
        });
    }

    //Recycler View
    private void featuredServiceRecycler() {
        featuredServiceRecycler.setHasFixedSize(true);
        featuredServiceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ServiceHelper> featureServices = new ArrayList<>();
        featuredServiceAdapter = new FeaturedServiceAdapter(featureServices);
        featuredServiceRecycler.setAdapter(featuredServiceAdapter);

        String placeHolder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        Service.getTopNService(10, new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    Collections.reverse(services);
                    for (Service service:services)
                        featureServices.add(new ServiceHelper(R.drawable.service1,service.getServiceId(), service.getServiceName(), service.getDescription().equals("")?placeHolder:service.getDescription(), 4 ));
                    featuredServiceAdapter.notifyDataSetChanged();
                }
            }
        });


//        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 3.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
//        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 4.0, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
//        featureServices.add(new ServiceHelper(R.drawable.service1, (float) 4.5, "AC Services", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));


    }

    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ServiceHelper> mostViewedServices = new ArrayList<>();
        mostViewedServiceAdapter = new MostViewedServiceAdapter(mostViewedServices);
        mostViewedRecycler.setAdapter(mostViewedServiceAdapter);

        String placeHolder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        Service.getTopNService(10, new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    Collections.reverse(services);
                    for (Service service:services)
                        mostViewedServices.add(new ServiceHelper(R.drawable.service1,service.getServiceId(), service.getServiceName(), service.getDescription().equals("")?placeHolder:service.getDescription(), 4 ));
                    mostViewedServiceAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void categoryRecycler() {
        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GradientDrawable[] gradientDrawables = new GradientDrawable[4];

        gradientDrawables[0] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradientDrawables[1] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradientDrawables[2] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradientDrawables[3] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

        Category.getAllCategory(new CategoryInterface() {
            @Override
            public void getAllCategory(ArrayList<Category> categories) {
                int gradientIndex = 0;
                int gradientLength = gradientDrawables.length;
                if (categories != null && categories.size() > 0) {
                    ArrayList<CategoryHelper> categoryHelpers = new ArrayList<>();
                    for (Category category : categories)
                        categoryHelpers.add(new CategoryHelper(R.drawable.chat, category.getCategoryId(),category.getCategoryName(), gradientDrawables[(gradientIndex++) % gradientLength]));
                    categoryAdapter = new CategoryAdapter(categoryHelpers);
                    categoryRecycler.setAdapter(categoryAdapter);
                }
            }
        });
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
                gotoProfileActivity(new View(this));
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.nav_category:
                gotoCategoryActivity(new View(this));
                break;
            case R.id.nav_search:
                gotoSearchActivity(new View(this));
                break;
            case R.id.nav_appointment:
                gotoAppointmentActivity(new View(this));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Click Functions
    public void gotoAppointmentActivity(View view) {
        Intent appointmentIntent = new Intent(getApplicationContext(), AppointmentListActivity.class);
        startActivity(appointmentIntent);
    }

    public void gotoSearchActivity(View view) {
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(searchIntent);
    }

    public void gotoCategoryActivity(View view) {
        Intent categoryIntent = new Intent(getApplicationContext(), CategoryActivity.class);
        startActivity(categoryIntent);
    }

    public void gotoProfileActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProfileActivity.PROFILE_TYPE, ProfileActivity.USER_PROFILE);
        bundle.putString(ProfileActivity.PROFILE_ID, currentUser.getUid());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onClickCategoryCard(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SearchActivity.SEARCH_TYPE, SearchActivity.CATEGORY_SEARCH);
        bundle.putString(SearchActivity.SEARCH_ID, view.getContentDescription().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onClickMostViewedCard(View view){
        ViewGroup viewGroup = (ViewGroup) view;
        String serviceId = (String) viewGroup.findViewById(R.id.most_viewed_title).getContentDescription();
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SearchActivity.SEARCH_TYPE, SearchActivity.SERVICE_SEARCH);
        bundle.putString(SearchActivity.SEARCH_ID, serviceId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onClickFeaturedCard(View view){
        ViewGroup viewGroup = (ViewGroup) view;
        String serviceId = (String) viewGroup.findViewById(R.id.featured_service_title).getContentDescription();
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SearchActivity.SEARCH_TYPE, SearchActivity.SERVICE_SEARCH);
        bundle.putString(SearchActivity.SEARCH_ID, serviceId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationToFirebase();
            } else {
                Toast.makeText(DashboardActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

    }

}