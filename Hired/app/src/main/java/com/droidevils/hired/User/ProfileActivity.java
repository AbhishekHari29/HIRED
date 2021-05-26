package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidevils.hired.Common.LoadingDialog;
import com.droidevils.hired.Helper.Bean.ProfileBean;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE_TYPE = "PROFILE_TYPE";
    public static final String USER_PROFILE = "USER";
    public static final String OTHER_PROFILE = "OTHER";

    public String profileType;

    private LoadingDialog loadingDialog;
    private int loadingProcess;

    CircularImageView profileImage;
    LinearLayout personalinfo, experience, review;
    TextView personalinfobtn, experiencebtn, reviewbtn;

    //Profile Information
    ProfileBean currentProfileBean;
    TextView userName, userCityState;
    TextView contactChat, contactEmail, contactPhone;
    TextView personalSummary;
    TextView phone, email, cityState;
    TextView degreeField, institution, eduCityState;
    TextView jobTitle, jobExperience, jobCompanyLocation;

    String currentUserId;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference profileReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        currentUserId = currentUser.getUid();

        //Profile Type
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profileType = extras.getString(PROFILE_TYPE);
        } else {
            profileType = "";
        }


        loadingDialog = new LoadingDialog(ProfileActivity.this);
        loadingProcess = 0;

        loadingDialog.startLoadingDialog();

        loadingProcess++;
        //Navigation
        personalinfo = findViewById(R.id.personalinfo);
        experience = findViewById(R.id.experience);
        review = findViewById(R.id.review);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        experiencebtn = findViewById(R.id.experiencebtn);
        reviewbtn = findViewById(R.id.reviewbtn);

        //Profile Information
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.profile_user_name);
        userCityState = findViewById(R.id.profile_user_city_state);
        contactChat = findViewById(R.id.contact_chat_button);
        contactEmail = findViewById(R.id.contact_email_button);
        contactPhone = findViewById(R.id.contact_phone_button);
        personalSummary = findViewById(R.id.profile_personal_summary);
        phone = findViewById(R.id.profile_phone_number);
        email = findViewById(R.id.profile_email);
        cityState = findViewById(R.id.profile_city_state);
        degreeField = findViewById(R.id.education_degree_field);
        institution = findViewById(R.id.education_institution);
        eduCityState = findViewById(R.id.education_city_state);
        jobTitle = findViewById(R.id.job_title);
        jobExperience = findViewById(R.id.job_experience);
        jobCompanyLocation = findViewById(R.id.job_company_location);

        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        experience.setVisibility(View.GONE);
        review.setVisibility(View.GONE);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        profileReference = firebaseDatabase.getReference("Profile");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Profile");

        if (currentUserId != null) {
            updateProfileInformation();
        }

        contactPhone.setOnClickListener(v -> openCallerIntent());

        contactEmail.setOnClickListener(v -> sendEmailIntent());

        personalinfobtn.setOnClickListener(v -> {

            personalinfo.setVisibility(View.VISIBLE);
            experience.setVisibility(View.GONE);
            review.setVisibility(View.GONE);
            personalinfobtn.setTextColor(getColor(R.color.blue));
            experiencebtn.setTextColor(getColor(R.color.grey));
            reviewbtn.setTextColor(getColor(R.color.grey));

        });

        experiencebtn.setOnClickListener(v -> {

            personalinfo.setVisibility(View.GONE);
            experience.setVisibility(View.VISIBLE);
            review.setVisibility(View.GONE);
            personalinfobtn.setTextColor(getColor(R.color.grey));
            experiencebtn.setTextColor(getColor(R.color.blue));
            reviewbtn.setTextColor(getColor(R.color.grey));

        });

        reviewbtn.setOnClickListener(v -> {

            personalinfo.setVisibility(View.GONE);
            experience.setVisibility(View.GONE);
            review.setVisibility(View.VISIBLE);
            personalinfobtn.setTextColor(getColor(R.color.grey));
            experiencebtn.setTextColor(getColor(R.color.grey));
            reviewbtn.setTextColor(getColor(R.color.blue));

        });

        loadingProcess--;
        if (loadingProcess < 1)
            loadingDialog.stopLoadingDialog();
    }

    private void sendEmailIntent() {
        String recipient = currentProfileBean.getEmail();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    private void openCallerIntent() {
        String mobileNumber = currentProfileBean.getPhone();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
        intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
        startActivity(intent);
    }

    private void updateProfileInformation() {
        loadingProcess++;

        StorageReference profileImageReference = storageReference.child(currentUserId).child("profile_image.jpg");
        profileImageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        profileReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentProfileBean = snapshot.getValue(ProfileBean.class);
                if (currentProfileBean != null) {
                    userName.setText(currentProfileBean.getFullName());
                    userCityState.setText(String.format("%s, %s", currentProfileBean.getCity(), currentProfileBean.getState()));
                    personalSummary.setText(currentProfileBean.getSummary());
                    phone.setText(currentProfileBean.getPhone());
                    email.setText(currentProfileBean.getEmail());
                    cityState.setText(String.format("%s, %s", currentProfileBean.getCity(), currentProfileBean.getState()));
                    degreeField.setText(String.format("%s, %s", currentProfileBean.getDegree(), currentProfileBean.getField()));
                    institution.setText(currentProfileBean.getInstitution());
                    eduCityState.setText(String.format("%s, %s", currentProfileBean.getEduCity(), currentProfileBean.getEduState()));
                    jobTitle.setText(currentProfileBean.getJobTitle());
                    jobExperience.setText(String.format("%d Yrs Experience", currentProfileBean.getJobExperience()));
                    jobCompanyLocation.setText(String.format("%s, %s", currentProfileBean.getCompany(), currentProfileBean.getLocation()));

                    loadingProcess--;
                    if (loadingProcess < 1)
                        loadingDialog.stopLoadingDialog();

                } else {

                    loadingProcess--;
                    if (loadingProcess < 1)
                        loadingDialog.stopLoadingDialog();

                    Intent profileSetupIntent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(ProfileSetupActivity.PROFILE_OPERATION, ProfileSetupActivity.PROFILE_ADD);
                    profileSetupIntent.putExtras(extras);
                    startActivity(profileSetupIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });
    }

    public void onClickEditProfile(View view) {
        Intent profileSetupIntent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ProfileSetupActivity.PROFILE_OPERATION, ProfileSetupActivity.PROFILE_MODIFY);
        profileSetupIntent.putExtras(extras);
        startActivity(profileSetupIntent);
        finish();
    }

    public void onClickBookmarkProfile(View view) {

    }

    public void goBackButton(View view) {
        onBackPressed();
    }
}
