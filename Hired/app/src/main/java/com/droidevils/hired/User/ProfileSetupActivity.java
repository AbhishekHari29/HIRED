package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.droidevils.hired.Helper.Validation;
import com.droidevils.hired.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ProfileSetupActivity extends AppCompatActivity {

    //Form Navigation
    private final int numberOfTab = 3;
    private int currentTab = 0;
    private LinearLayout[] profileTabs;
    private Button nextButton, previousButton;

    //Form Input Elements
    private TextInputLayout fullName, email, phone, gender, birthDate, pincode, address, summary;
    private TextInputLayout field, degree, school, city, state, graduationDate;
    private TextInputLayout jobTitle, jobCompany, jobLocation, jobExperience, jobDesc;
    private AutoCompleteTextView genderAtv, degreeAtv;
    private CheckBox stillStudying, stillWorking;
    MaterialDatePicker birthDatePicker, graduationDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //Form Input Elements
        //Personal
        fullName = findViewById(R.id.fullname_layout);
        email = findViewById(R.id.email_layout);
        gender = findViewById(R.id.gender_layout);
        genderAtv = findViewById(R.id.gender_atv);
        phone = findViewById(R.id.phone_layout);
        birthDate = findViewById(R.id.dob_layout);
        pincode = findViewById(R.id.pincode_layout);
        address = findViewById(R.id.address_layout);
        summary = findViewById(R.id.personal_summary_layout);

        //Education
        field = findViewById(R.id.education_field_layout);
        degree = findViewById(R.id.degree_layout);
        degreeAtv = findViewById(R.id.degree_atv);
        school = findViewById(R.id.school_name_layout);
        city = findViewById(R.id.education_city_layout);
        state = findViewById(R.id.education_state_layout);
        graduationDate = findViewById(R.id.grad_date_layout);
        stillStudying = findViewById(R.id.still_studying_checkbox);

        //Work Experience
        jobTitle = findViewById(R.id.job_title_layout);
        jobCompany = findViewById(R.id.company_name_layout);
        jobLocation = findViewById(R.id.job_location_layout);
        jobExperience = findViewById(R.id.job_expyear_layout);
        jobDesc = findViewById(R.id.job_desc_layout);
        stillWorking = findViewById(R.id.still_working_checkbox);

        // Form navigation
        profileTabs = new LinearLayout[numberOfTab];
        profileTabs[0] = findViewById(R.id.profile1_layout);
        profileTabs[1] = findViewById(R.id.profile2_layout);
        profileTabs[2] = findViewById(R.id.profile3_layout);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

        initBirthDatePicker();
        initGradDateDatePicker();

        genderAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender)));
        degreeAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.degree)));
    }

    private void initGradDateDatePicker() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Graduation Date");
        graduationDatePicker = builder.build();
        graduationDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Long value = Long.valueOf(selection.toString());
                Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utc.setTimeInMillis(value);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formatted = format.format(utc.getTime());
                graduationDate.getEditText().setText(formatted);
            }
        });
        graduationDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graduationDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
    }

    /* TODO Detect Error */
    private void initBirthDatePicker() {

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date of Birth");
        birthDatePicker = builder.build();
        birthDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Long value = Long.valueOf(selection.toString());
                Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utc.setTimeInMillis(value);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formatted = format.format(utc.getTime());
                birthDate.getEditText().setText(formatted);
            }
        });
        birthDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
    }

    private void onProfileFinish() {

        /* TODO Validate */

        /* TODO Upload to FireBase */

        /* TODO Navigate */

    }

    private boolean validateCurrentTab() {
        switch (currentTab) {
            case 0:
                return validatePersonal();
            case 1:
                return validateEducation();
            case 2:
                return validateWorkExperience();
            default:
                return false;
        }
    }

    public void changeFormTab(int value) {
        for (LinearLayout layout : profileTabs)
            layout.setVisibility(View.GONE);
        profileTabs[value].setVisibility(View.VISIBLE);
    }

//    public void onNextPreviousClick(View view) {
//        switch (view.getId()) {
//            case R.id.next_button:
//                if (currentForm + 1 == numberOfForm)
//                    onProfileFinish();
//                else if (currentForm + 1 < numberOfForm) {
//                    if (validateCurrentTab()) {
//                        currentForm++;
//                        changeFormTab(currentForm);
//                    } else return;
//                } else return;
//                break;
//            case R.id.previous_button:
//                if (currentForm - 1 >= 0) {
//                    currentForm--;
//                    changeFormTab(currentForm);
//                } else return;
//                break;
//        }
//        if (currentForm == numberOfForm - 1)
//            nextButton.setText("Finish");
//        else
//            nextButton.setText("Next");
//    }

    public void onNextClick(View view) {
        if (currentTab + 1 == numberOfTab) {
            onProfileFinish();
        } else if (currentTab + 1 < numberOfTab && validateCurrentTab()) {
            changeFormTab(++currentTab);
            updateButtonText();
        }
    }

    public void onPreviousClick(View view) {
        if (currentTab - 1 >= 0) {
            changeFormTab(--currentTab);
            updateButtonText();
        }
    }

    private void updateButtonText() {
        if (currentTab == numberOfTab - 1)
            nextButton.setText(R.string.finish);
        else
            nextButton.setText(R.string.next);
    }

    private boolean validatePersonal() {
        return (Validation.validateEmpty(fullName) & Validation.validateEmail(email) & Validation.validatePhone(phone) &
                Validation.validateDropDown(gender, genderAtv) & Validation.validateEmpty(birthDate) & Validation.validateEmpty(pincode));
    }

    private boolean validateEducation() {
        return (Validation.validateEmpty(field) & Validation.validateDropDown(degree, degreeAtv) & Validation.validateEmpty(school) &
                Validation.validateEmpty(city) & Validation.validateEmpty(state) & Validation.validateEmpty(graduationDate));
    }

    private boolean validateWorkExperience() {
        return true;
    }
}