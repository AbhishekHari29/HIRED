package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.droidevils.hired.Helper.Bean.ProfileBean;
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
import com.google.firebase.database.FirebaseDatabase;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

public class SearchActivity extends AppCompatActivity {

    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final String SEARCH_ID = "SEARCH_ID";
    public static final String QUERY_SEARCH = "QUERY_SEARCH";
    public static final String SERVICE_SEARCH = "SERVICE_SEARCH";
    public static final String CATEGORY_SEARCH = "CATEGORY_SEARCH";
    public static final String USER_SEARCH = "USER_SEARCH";

    private String searchType;
    private String searchId;

    private ProcessManager processManager;

    //Location
    private final int PERMISSION_ID = 44;
    private final int REQUEST_CHECK_SETTINGS = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private UserLocation currentUserLocation;

    private SearchView searchView;
    private ListView searchResultListView;

    private ArrayList<AvailableService> availableServices;
    private ArrayList<AvailableServiceHelper> serviceHelpers;
    private ServiceAdapter serviceAdapter;

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
            startActivity(intent);
            finish();
        }

        processManager = new ProcessManager(SearchActivity.this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getCurrentLocation();

        processManager.incrementProcessCount();//1
        searchResultListView = findViewById(R.id.search_result);
        searchView = findViewById(R.id.search_view);

        availableServices = new ArrayList<>();
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
//                    getCurrentLocation();
                    getLastLocation();
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

    //Location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            currentUserLocation = new UserLocation(location.getLatitude(), location.getLongitude());
                            FirebaseDatabase.getInstance().getReference("Location").child(currentUser.getUid()).setValue(currentUserLocation);
                            optimizeServiceByLocation(currentUserLocation);
                        }
                    }
                });
            } else {
//                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
                enableLocationService();
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        LocationCallback mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                currentUserLocation = new UserLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                FirebaseDatabase.getInstance().getReference("Location").child(currentUser.getUid()).setValue(currentUserLocation);
                optimizeServiceByLocation(currentUserLocation);
            }
        };

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void enableLocationService() {
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
                    getLastLocation();
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

    private void optimizeServiceByLocation(UserLocation location) {

        UserLocation.getAllLocation(new UserLocationInterface() {
            @Override
            public void getLocationHashMap(HashMap<String, UserLocation> locationMap) {
                if (locationMap != null && locationMap.size() > 0) {
                    for (AvailableServiceHelper helper : serviceHelpers)
                        if (locationMap.get(helper.getUserId()) != null
                                // Filtering out current user
                                && !currentUser.getUid().equals(helper.getUserId())
                        )
                            helper.setDistance(location.computeDistance(locationMap.get(helper.getUserId())));
                    serviceAdapter.notifyDataSetChanged();
                    sortByDistance();
                }
            }
        });

    }


    private void sortByDistance() {
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
                if (serviceHelper.isAvailability()) {
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
                } else {
                    Toast.makeText(getApplicationContext(),"Service is Unavailable", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(SearchActivity.this, "GPS Turned On", Toast.LENGTH_LONG).show();
                getLastLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

}