package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.droidevils.hired.Common.LoadingDialog;
import com.droidevils.hired.Helper.AvailableServiceHelper;
import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.ServiceAdapter;
import com.droidevils.hired.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final String SERVICE_SEARCH = "SERVICE_SEARCH";
    public static final String CATEGORY_SEARCH = "CATEGORY_SEARCH";
    public static final String USER_SEARCH = "USER_SEARCH";

    private String searchType;
    private String searchId;

    private SearchView searchView;
    private LoadingDialog loadingDialog;
    private int loadingProcess;

    ArrayList<AvailableService> availableServices;
    ArrayList<Service> myServices;

    ArrayList<AvailableServiceHelper> serviceHelpers;
    ListView searchResultListView;
    ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchResultListView = findViewById(R.id.search_result);
        searchView = findViewById(R.id.search_view);

        loadingDialog = new LoadingDialog(SearchActivity.this);
        loadingProcess = 0;

        availableServices = new ArrayList<>();
        myServices = new ArrayList<>();
        serviceHelpers = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(SearchActivity.this, serviceHelpers);
        searchResultListView.setAdapter(serviceAdapter);
//        initializeList();

        loadingDialog.startLoadingDialog();
        retrieveServiceInformation();
        retrieveAvailableServiceInformation();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchType = extras.getString(SEARCH_TYPE);
            if (searchType.equals(SERVICE_SEARCH)) {
                searchId = extras.getString(SERVICE_SEARCH);
            } else if (searchType.equals(CATEGORY_SEARCH)) {
                searchId = extras.getString(CATEGORY_SEARCH);
            } else if (searchType.equals(USER_SEARCH)) {
                searchId = extras.getString(USER_SEARCH);
            }
        } else {
            searchType = "";
            searchId = "";
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                serviceAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                serviceAdapter.getFilter().filter(newText);


                return false;
            }
        });


    }

    private void retrieveAvailableServiceInformation() {
        loadingProcess++;
        AvailableService.getAllService(new AvailableServiceInterface() {
            @Override
            public void getAllService(ArrayList<AvailableService> services) {
                if (services != null && services.size() > 0) {
                    availableServices.addAll(services);
                    for (AvailableService availableService : availableServices)
                        serviceHelpers.add(new AvailableServiceHelper(R.drawable.service1,
                                availableService.getServiceId(), availableService.getServiceName(),
                                availableService.getRating(), availableService.isAvailability(),
                                availableService.getTimeFrom(), availableService.getTimeTo(),
                                availableService.getWorkingDays()));
                    serviceAdapter.setOriginalList(serviceHelpers);
                    serviceAdapter.notifyDataSetChanged();

                    if (searchType != null && !searchType.equals("")) {
                        serviceAdapter.getFilter().filter(searchId);
                    }
                }
                    loadingProcess--;
                    if (loadingProcess < 1)
                        loadingDialog.stopLoadingDialog();
            }
        });
    }

    private void retrieveServiceInformation() {
        loadingProcess++;
        Service.getAllService(new ServiceInterface() {
            @Override
            public void getAllService(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    myServices.addAll(services);
                    // DO something with data
                }
                loadingProcess--;
                if (loadingProcess < 1)
                    loadingDialog.stopLoadingDialog();
            }
        });
    }

    private void initializeList() {

        AvailableService.getAllService(new AvailableServiceInterface() {
            @Override
            public void getAllService(ArrayList<AvailableService> services) {
                if (services != null && services.size() > 0) {
                    for (AvailableService availableService : services) {
                        Log.i("MESSAGE", availableService.getUserId());
                        serviceHelpers.add(new AvailableServiceHelper(R.drawable.service1,
                                availableService.getServiceId(), availableService.getServiceName(),
                                availableService.getRating(), availableService.isAvailability(),
                                availableService.getTimeFrom(), availableService.getTimeTo(),
                                availableService.getWorkingDays()));
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "No Service Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onClickService(View view) {
        Toast.makeText(SearchActivity.this, "Clicked: " + view.getContentDescription(), Toast.LENGTH_SHORT).show();
    }

    public void goBackButton(View view) {
        onBackPressed();
    }

}