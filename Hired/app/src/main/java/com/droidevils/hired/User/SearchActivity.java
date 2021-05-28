package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidevils.hired.Helper.AvailableServiceHelper;
import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.Bean.UserInterface;
import com.droidevils.hired.Helper.ProcessManager;
import com.droidevils.hired.Helper.ServiceAdapter;
import com.droidevils.hired.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final String SEARCH_ID = "USER_SEARCH";
    public static final String SERVICE_SEARCH = "SERVICE_SEARCH";
    public static final String CATEGORY_SEARCH = "CATEGORY_SEARCH";
    public static final String USER_SEARCH = "USER_SEARCH";

    private ProcessManager processManager;

    private String searchType;
    private String searchId;

    // TODO SearchView size
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

        if (currentUser == null){
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
            finish();
        }

        processManager = new ProcessManager(SearchActivity.this);

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
            public void getAllService(ArrayList<AvailableService> services) {
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

                    //Filter Result
                    if (searchType != null && !searchType.equals("")) {
                        serviceAdapter.getFilter().filter(searchId);
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
            public void getAllService(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    myServices.addAll(services);
                    // DO something with data
                }
                processManager.decrementProcessCount();
            }
        });
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
                Toast.makeText(getApplicationContext(), "Book Appointment" + serviceHelper.getUserName(), Toast.LENGTH_LONG).show();

                Intent AppointmentIntent = new Intent(getApplicationContext(), AppointmentActivity.class);
//                extras = new Bundle();
//                // Service ID, Service Name, Service Provider ID, Service Provider Name,
//                //Service Reciever ID and Service Reciever name
//                extras.putString(AppointmentActivity.APPOINTMENT_OPERATION, AppointmentActivity.APPOINTMENT_ADD);
//                extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_PROVIDER_ID, serviceHelper.getUserId());
//                extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_PROVIDER_NAME, serviceHelper.getUserName());
//                extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_RECEIVER_ID, currentUser.getUid());
//                UserBean.getUserById(currentUser.getUid(), new UserInterface() {
//                    @Override
//                    public void getUserById(UserBean userBean) {
//                        extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_RECEIVER_NAME, userBean.getFullName());
//                    }
//                });
//                extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_ID, serviceHelper.getServiceId());
//                extras.putString(AppointmentActivity.APPOINTMENT_SERVICE_NAME, serviceHelper.getServiceName());
//
//                AppointmentIntent.putExtras(extras);
                startActivity(AppointmentIntent);
                //Book Appointment

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}