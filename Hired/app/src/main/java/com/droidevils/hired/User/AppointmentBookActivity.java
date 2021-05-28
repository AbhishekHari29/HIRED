package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;

import com.droidevils.hired.Helper.Bean.Appointment;
import com.droidevils.hired.Helper.Bean.AppointmentInterface;
import com.droidevils.hired.Helper.Validation;
import com.droidevils.hired.R;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AppointmentBookActivity extends AppCompatActivity {

    public static final String APPOINTMENT_OPERATION = "APPOINTMENT_OPERATION";
    public static final String APPOINTMENT_ADD = "APPOINTMENT_ADD";
    public static final String APPOINTMENT_MODIFY = "APPOINTMENT_MODIFY";

    public static final String APPOINTMENT_SERVICE_PROVIDER_ID = "APPOINTMENT_SERVICE_PROVIDER_ID";
    public static final String APPOINTMENT_SERVICE_PROVIDER_NAME = "APPOINTMENT_SERVICE_PROVIDER_NAME";
    public static final String APPOINTMENT_SERVICE_RECEIVER_ID = "APPOINTMENT_SERVICE_RECEIVER_ID";
    public static final String APPOINTMENT_SERVICE_RECEIVER_NAME = "APPOINTMENT_SERVICE_RECEIVER_NAME";
    public static final String APPOINTMENT_SERVICE_ID = "APPOINTMENT_SERVICE_ID";
    public static final String APPOINTMENT_SERVICE_NAME = "APPOINTMENT_SERVICE_NAME";

    private TextInputLayout serviceProviderLayout,serviceLayout, commentLayout;
    private Button dateButton, timeButton;
    private int hr,min;

    private MaterialDatePicker appointmentDatePicker;
    private MaterialTimePicker appointmentTimePicker;

    private String appointmentOperation;
    private String serviceId;
    private String serviceName;
    private String serviceProviderId;
    private String serviceProviderName;
    private String serviceReceiverId;
    private String serviceReceiverName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        serviceLayout = findViewById(R.id.appointment_service_layout);
        serviceProviderLayout = findViewById(R.id.appointment_service_provider_layout);
        commentLayout = findViewById(R.id.appointment_comment_layout);
        timeButton = findViewById(R.id.appointment_time_button);
        dateButton = findViewById(R.id.appointment_date_button);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            appointmentOperation = extras.getString(APPOINTMENT_OPERATION);
            serviceId = extras.getString(APPOINTMENT_SERVICE_ID);
            serviceName = extras.getString(APPOINTMENT_SERVICE_NAME);
            serviceProviderId = extras.getString(APPOINTMENT_SERVICE_PROVIDER_ID);
            serviceProviderName = extras.getString(APPOINTMENT_SERVICE_PROVIDER_NAME);
            serviceReceiverId = extras.getString(APPOINTMENT_SERVICE_RECEIVER_ID);
            serviceReceiverName = extras.getString(APPOINTMENT_SERVICE_RECEIVER_NAME);

            serviceLayout.getEditText().setText(serviceName);
            serviceLayout.setContentDescription(serviceId);
            serviceProviderLayout.getEditText().setText(serviceProviderName);
            serviceProviderLayout.setContentDescription(serviceProviderId);

            if (appointmentOperation!=null && appointmentOperation.equals(APPOINTMENT_MODIFY)){
                Appointment.getAppointmentById(serviceProviderId, serviceReceiverId, serviceId, new AppointmentInterface() {
                    @Override
                    public void getAppointmentById(Appointment appointment) {
                        if (appointment != null){
                            timeButton.setText(appointment.getTime());
                            dateButton.setText(appointment.getDate());
                            commentLayout.getEditText().setText(appointment.getComment());
                        }
                    }
                });
            }
        }
        initTimePicker();
        initDatePicker();
    }



    public void onClickBookAppointment(View view){

        //validation
        if (!validateAppointment())
            return;

        //read
        String appointmentTime = timeButton.getText().toString().trim();
        String appointmentDate = dateButton.getText().toString().trim();
        String appointmentComment = commentLayout.getEditText().getText().toString().trim();
        String appointmentStatus = Appointment.PENDING;

        Appointment appointment = new Appointment(serviceProviderId, serviceProviderName, serviceReceiverId,serviceReceiverName,serviceId,serviceName, appointmentTime, appointmentDate, appointmentStatus, appointmentComment);
        appointment.addAppointment(new AppointmentInterface() {
            @Override
            public void getBooleanResult(Boolean result) {
                if (result){
                    Toast.makeText(getApplicationContext(), "Appointment Sent", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Appointment couldn't be Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateAppointment() {
        boolean result = true;
        if (!Validation.validateTime(timeButton.getText().toString().trim())){
            result = false;
            Toast.makeText(getApplicationContext(), "Select Time", Toast.LENGTH_SHORT).show();
        }
        if (!Validation.validateDate(dateButton.getText().toString().trim())){
            result = false;
            Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void goBackButton(View view) {
        onBackPressed();
    }

    private void initTimePicker(){
//        appointmentTimePicker = new MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .setHour(12)
//                .setMinute(0)
//                .build();
//
//        timeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                appointmentTimePicker.show(getSupportFragmentManager(), "fragment_tag");
//            }
//        });

        timeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentBookActivity.this, (view, hourOfDay, minute) -> {
                hr = hourOfDay;
                min = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, hourOfDay, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                timeButton.setText(dateFormat.format(calendar.getTime()));
            }, 12, 0, false);
            timePickerDialog.updateTime(hr, min);
            timePickerDialog.show();
        });
    }

    private void initDatePicker() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date");
        appointmentDatePicker = builder.build();
        appointmentDatePicker.addOnPositiveButtonClickListener(selection -> {
            Long value = Long.valueOf(selection.toString());
            Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc.setTimeInMillis(value);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formatted = format.format(utc.getTime());
            dateButton.setText(formatted);
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
    }

}