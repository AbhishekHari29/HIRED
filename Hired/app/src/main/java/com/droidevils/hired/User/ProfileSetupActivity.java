package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.droidevils.hired.Common.LoadingDialog;
import com.droidevils.hired.Helper.Bean.ProfileBean;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.Validation;
import com.droidevils.hired.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ProfileSetupActivity extends AppCompatActivity {

    public static final String PROFILE_OPERATION = "PROFILE_OPERATION";
    public static final String PROFILE_ADD = "PROFILE_ADD";
    public static final String PROFILE_MODIFY = "PROFILE_MODIFY";

    private String profileOperation;

    private final int PICK_IMAGE = 1;

    //Form Navigation
    private final int numberOfTab = 4;
    private int currentTab = 0;
    private LinearLayout[] profileTabs;
    private Button nextButton, previousButton;

    //Form Input Elements
    private Uri profileImageUri;
    private CircularImageView profileImage;
    private TextInputLayout fullName, email, phone, summary;
    private TextInputLayout gender, birthDate, address, city, state, pincode;
    private TextInputLayout field, degree, institution, eduCity, eduState, graduationDate;
    private TextInputLayout jobTitle, jobCompany, jobLocation, jobExperience, jobDesc;
    private AutoCompleteTextView genderAtv, degreeAtv;
    private CheckBox stillStudying, stillWorking;
    MaterialDatePicker birthDatePicker, graduationDatePicker;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference userReference, profileReference;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    UserBean currentUserBean;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        loadingDialog = new LoadingDialog(ProfileSetupActivity.this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


        //Form Input Elements
        //Account
        profileImage = findViewById(R.id.profile_image);
        fullName = findViewById(R.id.fullname_layout);
        email = findViewById(R.id.email_layout);
        phone = findViewById(R.id.phone_layout);
        summary = findViewById(R.id.personal_summary_layout);

        //Personal
        gender = findViewById(R.id.gender_layout);
        genderAtv = findViewById(R.id.gender_atv);
        birthDate = findViewById(R.id.dob_layout);
        address = findViewById(R.id.address_layout);
        city = findViewById(R.id.city_layout);
        state = findViewById(R.id.state_layout);
        pincode = findViewById(R.id.pincode_layout);

        //Education
        field = findViewById(R.id.education_field_layout);
        degree = findViewById(R.id.degree_layout);
        degreeAtv = findViewById(R.id.degree_atv);
        institution = findViewById(R.id.institution_layout);
        eduCity = findViewById(R.id.education_city_layout);
        eduState = findViewById(R.id.education_state_layout);
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
        profileTabs[0] = findViewById(R.id.profile0_layout);
        profileTabs[1] = findViewById(R.id.profile1_layout);
        profileTabs[2] = findViewById(R.id.profile2_layout);
        profileTabs[3] = findViewById(R.id.profile3_layout);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

        initBirthDatePicker();
        initGradDateDatePicker();

        genderAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender)));
        degreeAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.degree)));

        //Firebase
        rootNode = FirebaseDatabase.getInstance();
        profileReference = rootNode.getReference("Profile");
        userReference = rootNode.getReference("User");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profileOperation = extras.getString(PROFILE_OPERATION);
            if (profileOperation.equals(PROFILE_MODIFY)) {
                retrieveProfileInformation();
            }
        } else {
            profileOperation = "";
        }

        //Existing User Information
        userReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserBean = snapshot.getValue(UserBean.class);
                if (currentUserBean != null) {
                    fullName.getEditText().setText(currentUserBean.getFullName());
                    email.getEditText().setText(currentUserBean.getEmailId());
                    phone.getEditText().setText(currentUserBean.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void retrieveProfileInformation() {

        // TODO Profile Image not displaying
        //Retrive Profile Image
        StorageReference profileImageReference = storageReference.child(currentUser.getUid()).child("profile_image.jpg");
        profileImageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        //Existing Profile Information
        profileReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileBean profileBean = snapshot.getValue(ProfileBean.class);
                if (profileBean != null) {
                    //Account
                    fullName.getEditText().setText(profileBean.getFullName());
                    email.getEditText().setText(profileBean.getEmail());
                    phone.getEditText().setText(profileBean.getPhone());
                    summary.getEditText().setText(profileBean.getSummary());

                    //Personal
                    genderAtv.setText(profileBean.getGender(), false);
                    birthDate.getEditText().setText(profileBean.getBirthDate());
                    address.getEditText().setText(profileBean.getAddress());
                    city.getEditText().setText(profileBean.getCity());
                    state.getEditText().setText(profileBean.getState());
                    pincode.getEditText().setText(profileBean.getPincode());

                    //Education
                    field.getEditText().setText(profileBean.getField());
                    degreeAtv.setText(profileBean.getDegree(), false);
                    institution.getEditText().setText(profileBean.getInstitution());
                    eduCity.getEditText().setText(profileBean.getEduCity());
                    eduState.getEditText().setText(profileBean.getEduState());
                    stillStudying.setChecked(profileBean.isStillStudying());
                    graduationDate.getEditText().setText(profileBean.getGraduationDate());

                    //Work Experience
                    jobTitle.getEditText().setText(profileBean.getJobTitle());
                    jobCompany.getEditText().setText(profileBean.getCompany());
                    jobLocation.getEditText().setText(profileBean.getLocation());
                    jobExperience.getEditText().setText(String.valueOf(profileBean.getJobExperience()));
                    jobDesc.getEditText().setText(profileBean.getJobDescription());
                    stillWorking.setChecked(profileBean.isStillWorking());

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void uploadProfileImageToFirebase() {
        if (profileImageUri != null) {
            StorageReference profileImageRef = storageReference.child("Profile").child(currentUser.getUid()).child("profile_image.jpg");
            profileImageRef.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        Log.d("profileImage", "Image Upload Failed");
                    }
                }
            });
        }
    }

    public void updateProfilePicture(View view) {
        Intent pickImageIntent = new Intent();
        pickImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData())
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                profileImage.setImageURI(profileImageUri);
                uploadProfileImageToFirebase();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void onProfileFinish() {

        loadingDialog.startLoadingDialog();

        // Validate
        if (!(validatePersonal() && validateEducation() && validateWorkExperience())) {
            Toast.makeText(getApplicationContext(), "Validation Failed", Toast.LENGTH_LONG).show();
            loadingDialog.stopLoadingDialog();
            return;
        }

        //Initialize Profile Bean
        ProfileBean profileBean = new ProfileBean();
        //Account
        profileBean.setFullName(fullName.getEditText().getText().toString().trim());
        profileBean.setEmail(email.getEditText().getText().toString().trim());
        profileBean.setPhone(phone.getEditText().getText().toString().trim());
        profileBean.setSummary(summary.getEditText().getText().toString().trim());

        //Personal
        profileBean.setGender(genderAtv.getText().toString().trim());
        profileBean.setBirthDate(birthDate.getEditText().getText().toString().trim());
        profileBean.setAddress(address.getEditText().getText().toString().trim());
        profileBean.setCity(city.getEditText().getText().toString().trim());
        profileBean.setState(state.getEditText().getText().toString().trim());
        profileBean.setPincode(pincode.getEditText().getText().toString().trim());

        //Education
        profileBean.setField(field.getEditText().getText().toString().trim());
        profileBean.setDegree(degreeAtv.getText().toString().trim());
        profileBean.setInstitution(institution.getEditText().getText().toString().trim());
        profileBean.setEduCity(eduCity.getEditText().getText().toString().trim());
        profileBean.setEduState(eduState.getEditText().getText().toString().trim());
        profileBean.setStillStudying(stillStudying.isChecked());
        profileBean.setGraduationDate(graduationDate.getEditText().getText().toString().trim());

        //Work Experience
        profileBean.setJobTitle(jobTitle.getEditText().getText().toString().trim());
        profileBean.setCompany(jobCompany.getEditText().getText().toString().trim());
        profileBean.setLocation(jobLocation.getEditText().getText().toString().trim());
        profileBean.setJobExperience(Integer.parseInt(jobExperience.getEditText().getText().toString().trim()));
        profileBean.setJobDescription(jobDesc.getEditText().getText().toString().trim());
        profileBean.setStillWorking(stillWorking.isChecked());

        //Update User
        String fullNameString = fullName.getEditText().getText().toString().trim();
        String phoneNumberString = phone.getEditText().getText().toString().trim();
        if (!currentUserBean.getFullName().equals(fullNameString) || !currentUserBean.getPhoneNumber().equals(phoneNumberString)) {
            currentUserBean.setFullName(fullNameString);
            currentUserBean.setPhoneNumber(phoneNumberString);
            userReference.child(currentUser.getUid()).setValue(currentUserBean).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //Upload to FireBase
        // Profile Image
        uploadProfileImageToFirebase();
        // Profile Information
        profileReference.child(currentUser.getUid()).setValue(profileBean).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    loadingDialog.stopLoadingDialog();
                    startActivity(intent);
                    finish();
                } else {
                    loadingDialog.stopLoadingDialog();
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Navigation
    public void changeFormTab(int value) {
        for (LinearLayout layout : profileTabs)
            layout.setVisibility(View.GONE);
        profileTabs[value].setVisibility(View.VISIBLE);
    }

    public void onNextClick(View view) {
        if (currentTab + 1 == numberOfTab && validateCurrentTab()) {
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

    //Validation
    private boolean validateCurrentTab() {
        switch (currentTab) {
            case 0:
                return validateAccount();
            case 1:
                return validatePersonal();
            case 2:
                return validateEducation();
            case 3:
                return validateWorkExperience();
            default:
                return false;
        }
    }

    private boolean validateAccount() {
        return (Validation.validateEmpty(fullName) & Validation.validateEmail(email) & Validation.validatePhone(phone));
    }

    private boolean validatePersonal() {
        return (Validation.validateDropDown(gender, genderAtv) & Validation.validateEmpty(birthDate) &
                Validation.validateEmpty(address) & Validation.validateEmpty(city) &
                Validation.validateEmpty(state) & Validation.validateEmpty(pincode));
    }

    private boolean validateEducation() {
        return (Validation.validateEmpty(field) & Validation.validateDropDown(degree, degreeAtv) & Validation.validateEmpty(institution) &
                Validation.validateEmpty(eduCity) & Validation.validateEmpty(eduState) & Validation.validateEmpty(graduationDate));
    }

    private boolean validateWorkExperience() {
        return true;
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

    public void goBackButton(View view) {
        onBackPressed();
    }
}