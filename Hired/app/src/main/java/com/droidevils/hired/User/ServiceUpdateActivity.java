package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.Bean.UserInterface;
import com.droidevils.hired.Helper.Validation;
import com.droidevils.hired.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class ServiceUpdateActivity extends AppCompatActivity {

    public static final String SERVICE_OPERATION = "SERVICE_OPERATION";
    public static final String SERVICE_ADD = "SERVICE_ADD";
    public static final String SERVICE_MODIFY = "SERVICE_MODIFY";
    public static final String SERVICE_ID = "SERVICE_ID";

    private String currentUserId, serviceType, serviceId;

    private TextInputLayout serviceLayout;
    private AutoCompleteTextView serviceAtv;
    private Button timeFromBtn;
    private Button timeToBtn;
    private MaterialDayPicker workingDayPicker;
    private SwitchMaterial availabilitySwitch;
    private int hr1, hr2, min1, min2;
    private MaterialDayPicker.Weekday[] days;

    private AvailableService availableService;

    private ArrayList<String> serviceNames;
    private ArrayList<Service> serviceList;
    private ArrayAdapter<String> serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_update);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = currentUser.getUid();
        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(ServiceUpdateActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ServiceUpdateActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        serviceLayout = findViewById(R.id.service_layout);
        serviceAtv = findViewById(R.id.service_atv);
        timeFromBtn = findViewById(R.id.service_time_from);
        timeToBtn = findViewById(R.id.service_time_to);
        workingDayPicker = findViewById(R.id.service_working_day_picker);
        availabilitySwitch = findViewById(R.id.service_availability_switch);

        days = new MaterialDayPicker.Weekday[]{
                MaterialDayPicker.Weekday.SUNDAY,
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.TUESDAY,
                MaterialDayPicker.Weekday.WEDNESDAY,
                MaterialDayPicker.Weekday.THURSDAY,
                MaterialDayPicker.Weekday.FRIDAY,
                MaterialDayPicker.Weekday.SATURDAY,
        };

        //Service ATV
        serviceNames = new ArrayList<>();
        serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, serviceNames);
        serviceAtv.setAdapter(serviceAdapter);
        retrieveServiceInformation();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serviceType = extras.getString(SERVICE_OPERATION);
            if (serviceType != null && serviceType.equals(SERVICE_MODIFY)) {
                serviceId = extras.getString(SERVICE_ID);
                getExistingService();
            }
        }

        //Time Picker
        timeFromBtn.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ServiceUpdateActivity.this, (view, hourOfDay, minute) -> {
                hr1 = hourOfDay;
                min1 = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, hourOfDay, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                timeFromBtn.setText(dateFormat.format(calendar.getTime()));
            }, 12, 0, false);
            timePickerDialog.updateTime(hr1, min1);
            timePickerDialog.show();
        });
        timeToBtn.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ServiceUpdateActivity.this, (view, hourOfDay, minute) -> {
                hr2 = hourOfDay;
                min2 = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, hourOfDay, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                timeToBtn.setText(dateFormat.format(calendar.getTime()));
            }, 12, 0, false);
            timePickerDialog.updateTime(hr2, min2);
            timePickerDialog.show();
        });
    }

    private void getExistingService() {

        AvailableService.getServiceById(currentUserId, serviceId, new AvailableServiceInterface() {
            @Override
            public void getService(AvailableService service) {
                if (service != null) {
                    ServiceUpdateActivity.this.availableService = service;
                    serviceAtv.setText(availableService.getServiceName(), false);
                    timeFromBtn.setText(availableService.getTimeFrom());
                    timeToBtn.setText(availableService.getTimeTo());
                    availabilitySwitch.setChecked(availableService.isAvailability());
                    String workingDays = availableService.getWorkingDays();
                    List<MaterialDayPicker.Weekday> workingDaysList = new ArrayList<>();
                    for (int i = 0; i < 7; i++)
                        if (workingDays.charAt(i) == '1')
                            workingDaysList.add(days[i]);
                    workingDayPicker.setSelectedDays(workingDaysList);
                } else {
                    Toast.makeText(getApplicationContext(), "No Service found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrieveServiceInformation() {
        Service.getAllService(new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    serviceList = services;
                    for (Service service : services)
                        serviceNames.add(service.getServiceName());
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "ServiceList Missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickUpdate(View view) {

        //Validate
        if (!validateService())
            return;


        UserBean.getUserById(currentUserId, new UserInterface() {
            @Override
            public void getUserById(UserBean userBean) {
                if (userBean != null) {
                    String serviceName = serviceAtv.getText().toString();
                    String serviceId = serviceList.get(serviceNames.indexOf(serviceName)).getServiceId();
                    String userName = userBean.getFullName();
                    String timeFrom = timeFromBtn.getText().toString().trim();
                    String timeTo = timeToBtn.getText().toString().trim();
                    String workingDays = "";
                    List<MaterialDayPicker.Weekday> workingDaysList = workingDayPicker.getSelectedDays();
                    for (int i = 0; i < 7; i++) {
                        workingDays += (workingDaysList.contains(days[i])) ? "1" : "0";
                    }
                    float rating = new Random().nextInt() % 6;
                    Boolean availability = availabilitySwitch.isChecked();
                    AvailableService availableService = new AvailableService(currentUserId, userName, serviceId, serviceName, availability, timeFrom, timeTo, workingDays, rating);

                    switch (serviceType) {
                        case SERVICE_ADD:
                            availableService.addService(new AvailableServiceInterface() {
                                @Override
                                public void getBooleanResult(Boolean result) {
                                    if (result){
                                        Toast.makeText(getApplicationContext(), "Service Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Service not Added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        case SERVICE_MODIFY:
                        default:
                            availableService.updateService(new AvailableServiceInterface() {
                                @Override
                                public void getBooleanResult(Boolean result) {
                                    if (result){
                                        Toast.makeText(getApplicationContext(), "Service Updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Service not Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "UserName not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateService() {
        boolean result = true;
        if (!Validation.validateDropDown(serviceLayout, serviceAtv)) {
            result = false;
        }
        if (!serviceNames.contains(serviceAtv.getText().toString())) {
            Toast.makeText(ServiceUpdateActivity.this, "Select from Service List", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (!Validation.validateTime(timeFromBtn.getText().toString().trim())) {
            Toast.makeText(ServiceUpdateActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (!Validation.validateTime(timeToBtn.getText().toString().trim())) {
            Toast.makeText(ServiceUpdateActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (workingDayPicker.getSelectedDays().size() == 0) {
            Toast.makeText(ServiceUpdateActivity.this, "Select atleast one Working Day", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    public void goBackButton(View view) {
        onBackPressed();
    }

}