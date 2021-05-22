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

import com.droidevils.hired.Helper.Bean.ProfileBean;
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.R;
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

    public static final String USER_PROFILE = "USER";
    public static final String OTHER_PROFILE = "OTHER";

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

    String currentUserId;
    String userType;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference profileReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserId = extras.getString(UserBean.KEY_USER_ID);
            userType = extras.getString(UserBean.KEY_USER_TYPE);
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }

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
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
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

    public void goBackButton(View view){
        onBackPressed();
    }
}
