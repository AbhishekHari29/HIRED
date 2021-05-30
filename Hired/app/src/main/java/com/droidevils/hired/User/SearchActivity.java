package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.droidevils.hired.Helper.Adapter.AvailableServiceHelper;
import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.Bean.UserInterface;
import com.droidevils.hired.Helper.ProcessManager;
import com.droidevils.hired.Helper.Adapter.ServiceAdapter;
import com.droidevils.hired.Helper.Bean.UserLocation;
import com.droidevils.hired.Helper.Bean.UserLocationInterface;
import com.droidevils.hired.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 1;

    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final String SEARCH_ID = "SEARCH_ID";
    public static final String QUERY_SEARCH = "QUERY_SEARCH";
    public static final String SERVICE_SEARCH = "SERVICE_SEARCH";
    public static final String CATEGORY_SEARCH = "CATEGORY_SEARCH";
    public static final String USER_SEARCH = "USER_SEARCH";

    private ProcessManager processManager;
    FusedLocationProviderClient fusedLocationProviderClient;

    private String searchType;
    private String searchId;

    private UserLocation currentUserLocation;

    private SearchView searchView;

    ArrayList<AvailableService> availableServices;
    ArrayList<Service> myServices;

    ArrayList<AvailableServiceHelper> serviceHelpers;
    ListView searchResultListView;
    ServiceAdapter serviceAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
            finish();
        }

        processManager = new ProcessManager(SearchActivity.this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getCurrentLocation();

        processManager.incrementProcessCount();//1
        searchResultListView = findViewById(R.id.search_result);
        searchView = findViewById(R.id.search_view);

        availableServices = new ArrayList<>();
        myServices = new ArrayList<>();
        serviceHelpers = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(SearchActivity.this, serviceHelpers);
        searchResultListView.setAdapter(serviceAdapter);
        registerForContextMenu(searchResultListView);

//        retrieveServiceInformation();
        retrieveAvailableServiceInformation();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchType = extras.getString(SEARCH_TYPE);
            searchId = extras.getString(SEARCH_ID);
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

        processManager.decrementProcessCount();//1

    }

    private void retrieveAvailableServiceInformation() {
        processManager.incrementProcessCount();
        AvailableService.getAllService(new AvailableServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<AvailableService> services) {
                if (services != null && services.size() > 0) {
                    availableServices.addAll(services);
                    for (AvailableService availableService : availableServices)
                        serviceHelpers.add(new AvailableServiceHelper(R.drawable.service1,
                                availableService.getUserId(), availableService.getUserName(),
                                availableService.getServiceId(), availableService.getServiceName(),
                                availableService.getRating(), availableService.isAvailability(),
                                availableService.getTimeFrom(), availableService.getTimeTo(),
                                availableService.getWorkingDays()));
                    serviceAdapter.setOriginalList(serviceHelpers);
                    serviceAdapter.notifyDataSetChanged();
                    getCurrentLocation();
                    //Filter Result
                    if (searchType != null && !searchType.equals("")) {
                        serviceAdapter.getFilter().filter(searchId);
                        if (searchType.equals(QUERY_SEARCH))
                            searchView.setQuery(searchId, false);
                    }
                }
                processManager.decrementProcessCount();
            }
        });
    }

    private void retrieveServiceInformation() {
        processManager.incrementProcessCount();
        Service.getAllService(new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    myServices.addAll(services);
                    // DO something with data
                }
                processManager.decrementProcessCount();
            }
        });
    }

    //Location
    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                fusedLocationProviderClient.removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int lastLocationIndex = locationResult.getLocations().size() - 1;
                    Location location = locationResult.getLocations().get(lastLocationIndex);
                    Toast.makeText(SearchActivity.this, "Lat: " + location.getLatitude() + " , Long: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    currentUserLocation = new UserLocation(location.getLatitude(), location.getLongitude());
                    optimizeServiceByLocation(currentUserLocation);
                }
            }
        }, Looper.getMainLooper());
    }

    private void getUserCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        //check if GPS is ON
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Toast.makeText(SearchActivity.this, "Lat: " + location.getLatitude() + " , Long: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SearchActivity.this, "Location Not found", Toast.LENGTH_LONG).show();
                    turnOnLocationService();
                }
            }
        });
    }

    private void turnOnLocationService() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(SearchActivity.this, "GPS On", Toast.LENGTH_LONG).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(SearchActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                    e.printStackTrace();
                }
            }
        });
    }

    private void optimizeServiceByLocation(UserLocation location){
        HashMap<String,Double> distanceMap = new HashMap<>();
        for (AvailableServiceHelper availableServiceHelper:serviceHelpers){
            if (distanceMap.get(availableServiceHelper.getUserId()) != null){
                availableServiceHelper.setDistance(distanceMap.get(availableServiceHelper.getUserId()));
            } else {
                UserLocation.getLocationByUserId(availableServiceHelper.getUserId(), new UserLocationInterface() {
                    @Override
                    public void getLocation(UserLocation userLocation) {
                        if (userLocation!=null){
                            Log.i("MESSAGE", "Iterating Location");
                            double distance = location.computeDistance(userLocation);
                            distanceMap.put(availableServiceHelper.getUserId(), distance);
                            availableServiceHelper.setDistance(distance);
                        }
//                    else {
//                        ProfileBean.getLocationByPinCode(availableServiceHelper.getUserId(), new UserLocationInterface() {
//                            @Override
//                            public void getLocation(UserLocation userLocation) {
//                                if (userLocation!=null){
//                                    availableServiceHelper.setDistance(location.computeDistance(userLocation));
//                                }
//                            }
//                        });
//                    }
                    }
                });
            }

        }

//        int min = 2;
//        int max = 400;
//        for (AvailableServiceHelper availableServiceHelper:serviceHelpers)
//            availableServiceHelper.setDistance(Math.random()*(max-min+1)+min);
        sortByDistance();
    }

    private void sortByDistance(){
        Collections.sort(serviceHelpers, new Comparator<AvailableServiceHelper>() {
            @Override
            public int compare(AvailableServiceHelper o1, AvailableServiceHelper o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });
        serviceAdapter.notifyDataSetChanged();
    }

    public void goBackButton(View view) {
        onBackPressed();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_search_service_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AvailableServiceHelper serviceHelper = (AvailableServiceHelper) serviceHelpers.get((int) contextMenuInfo.id);
        switch (item.getItemId()) {
            case R.id.context_view_profile:
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                Bundle extras = new Bundle();
                extras.putString(ProfileActivity.PROFILE_TYPE, ProfileActivity.OTHER_PROFILE);
                extras.putString(ProfileActivity.PROFILE_ID, serviceHelper.getUserId());
                profileIntent.putExtras(extras);
                startActivity(profileIntent);
                return true;
            case R.id.context_book_appointment:
                UserBean.getUserById(currentUser.getUid(), new UserInterface() {
                    @Override
                    public void getUserById(UserBean userBean) {
                        Intent appointmentIntent = new Intent(getApplicationContext(), AppointmentBookActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(AppointmentBookActivity.APPOINTMENT_OPERATION, AppointmentBookActivity.APPOINTMENT_ADD);
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_PROVIDER_ID, serviceHelper.getUserId());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_PROVIDER_NAME, serviceHelper.getUserName());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_RECEIVER_ID, currentUser.getUid());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_RECEIVER_NAME, (userBean != null) ? userBean.getFullName() : "");
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_ID, serviceHelper.getServiceId());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_NAME, serviceHelper.getServiceName());
                        appointmentIntent.putExtras(extras);
                        startActivity(appointmentIntent);
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS){
            switch (resultCode){
                case Activity.RESULT_OK:
                    Toast.makeText(SearchActivity.this, "GPS Turned On", Toast.LENGTH_LONG).show();
                    getUserCurrentLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(SearchActivity.this, "GPS needed to be On", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(SearchActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

    }
}