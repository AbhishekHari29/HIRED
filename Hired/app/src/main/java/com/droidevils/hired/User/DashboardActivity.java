package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.droidevils.hired.Helper.DashboardAdapter.CategoryAdaptor;
import com.droidevils.hired.Helper.DashboardAdapter.CategoryHelper;
import com.droidevils.hired.Helper.DashboardAdapter.FeaturedServiceAdapter;
import com.droidevils.hired.Helper.DashboardAdapter.MostViewedServiceAdapter;
import com.droidevils.hired.Helper.DashboardAdapter.ServiceHelper;
import com.droidevils.hired.R;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView featuredServiceRecycler, mostViewedRecycler, categoryRecycler;
    RecyclerView.Adapter featuredServiceAdapter, mostViewedServiceAdapter, categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        featuredServiceRecycler = findViewById(R.id.featured_service_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoryRecycler = findViewById(R.id.category_recycler);
        featuredServiceRecycler();
        mostViewedRecycler();
        categoryRecycler();

    }

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

        ArrayList<CategoryHelper> categoryHelpers = new ArrayList<>();
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Marketing"));
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Social"));
        categoryHelpers.add(new CategoryHelper(R.drawable.chat, "Education"));

        categoryAdapter = new CategoryAdaptor(categoryHelpers);
        categoryRecycler.setAdapter(categoryAdapter);
    }

}