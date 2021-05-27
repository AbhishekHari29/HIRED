package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidevils.hired.Helper.AvailableServiceHelper;
import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.ProfileBean;
import com.droidevils.hired.Helper.ProcessManager;
import com.droidevils.hired.Helper.ServiceAdapter;
import com.droidevils.hired.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE_TYPE = "PROFILE_TYPE";
    public static final String PROFILE_ID = "PROFILE_ID";
    public static final String USER_PROFILE = "USER";
    public static final String OTHER_PROFILE = "OTHER";

    private ProcessManager processManager;

    private String profileType;
    private String profileId;


    ImageView bookmarkBtn, editButton;
    Button addServiceButton;
    CircularImageView profileImage;
    LinearLayout personalInfoLayout, serviceLayout, reviewLayout;
    TextView personalInfoBtn, serviceBtn, reviewBtn;

    //Profile Information
    ProfileBean currentProfileBean;
    TextView userName, userCityState;
    TextView contactChat, contactEmail, contactPhone;
    TextView personalSummary;
    TextView phone, email, cityState;
    TextView degreeField, institution, eduCityState;
    TextView jobTitle, jobExperience, jobCompanyLocation;

    ArrayList<AvailableService> availableServices;

    ArrayList<AvailableServiceHelper> serviceHelpers;
    ListView serviceListView;
    ServiceAdapter serviceAdapter;


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

        processManager = new ProcessManager(ProfileActivity.this);

        processManager.incrementProcessCount();//1

        //Navigation
        personalInfoLayout = findViewById(R.id.personalinfo);
        serviceLayout = findViewById(R.id.service);
        reviewLayout = findViewById(R.id.review);
        personalInfoBtn = findViewById(R.id.personalinfobtn);
        serviceBtn = findViewById(R.id.service_btn);
        reviewBtn = findViewById(R.id.reviewbtn);

        editButton = findViewById(R.id.profile_edit_button);
        bookmarkBtn = findViewById(R.id.profile_bookmark_button);
        addServiceButton = findViewById(R.id.add_service_btn);

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

        serviceListView = findViewById(R.id.service_list);
        availableServices = new ArrayList<>();
        serviceHelpers = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(ProfileActivity.this, serviceHelpers);
        serviceListView.setAdapter(serviceAdapter);
        registerForContextMenu(serviceListView);

        retrieveAvailableServiceInformation();

        /*making personal info visible*/
        personalInfoLayout.setVisibility(View.VISIBLE);
        serviceLayout.setVisibility(View.GONE);
        reviewLayout.setVisibility(View.GONE);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        profileReference = firebaseDatabase.getReference("Profile");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Profile");

        //Profile Type
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profileType = extras.getString(PROFILE_TYPE);
            profileId = extras.getString(PROFILE_ID);
            if (profileId != null && !profileId.isEmpty()) {
                updateProfileInformation();
            }
            if (profileType != null && !profileType.isEmpty()) {
                if (profileType.equals(USER_PROFILE)) {
                    addServiceButton.setEnabled(true);
                    addServiceButton.setVisibility(View.VISIBLE);
                    editButton.setEnabled(true);
                    editButton.setVisibility(View.VISIBLE);
                    bookmarkBtn.setEnabled(false);
                    bookmarkBtn.setVisibility(View.INVISIBLE);
                } else if (profileType.equals(OTHER_PROFILE)) {
                    addServiceButton.setEnabled(false);
                    addServiceButton.setVisibility(View.INVISIBLE);
                    editButton.setEnabled(false);
                    editButton.setVisibility(View.INVISIBLE);
                    bookmarkBtn.setEnabled(true);
                    bookmarkBtn.setVisibility(View.VISIBLE);
                } else {

                }
            }
        } else {
            profileType = "";
            profileId = "";
        }

        contactPhone.setOnClickListener(v -> openCallerIntent());

        contactEmail.setOnClickListener(v -> sendEmailIntent());

        personalInfoBtn.setOnClickListener(v -> {

            personalInfoLayout.setVisibility(View.VISIBLE);
            serviceLayout.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.GONE);
            personalInfoBtn.setTextColor(getColor(R.color.blue));
            serviceBtn.setTextColor(getColor(R.color.grey));
            reviewBtn.setTextColor(getColor(R.color.grey));

        });

        serviceBtn.setOnClickListener(v -> {

            personalInfoLayout.setVisibility(View.GONE);
            serviceLayout.setVisibility(View.VISIBLE);
            reviewLayout.setVisibility(View.GONE);
            personalInfoBtn.setTextColor(getColor(R.color.grey));
            serviceBtn.setTextColor(getColor(R.color.blue));
            reviewBtn.setTextColor(getColor(R.color.grey));

        });

        reviewBtn.setOnClickListener(v -> {

            personalInfoLayout.setVisibility(View.GONE);
            serviceLayout.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.VISIBLE);
            personalInfoBtn.setTextColor(getColor(R.color.grey));
            serviceBtn.setTextColor(getColor(R.color.grey));
            reviewBtn.setTextColor(getColor(R.color.blue));

        });

        processManager.decrementProcessCount();//1
    }

    private void retrieveAvailableServiceInformation() {
        processManager.incrementProcessCount();

        //TODO Query only User Service
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
                    //Filter
                    serviceAdapter.getFilter().filter(profileId);
                }
                processManager.decrementProcessCount();
            }
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
        processManager.incrementProcessCount();//2
//        processManager.incrementProcessCount();//3
        StorageReference profileImageReference = storageReference.child(profileId).child("profile_image.jpg");
        profileImageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImage);
//            processManager.decrementProcessCount();//3
        });

        profileReference.child(profileId).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    processManager.decrementProcessCount();//2

                } else {

                    processManager.decrementProcessCount();//2

                    if (profileType.equals(USER_PROFILE)) {
                        Intent profileSetupIntent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(ProfileSetupActivity.PROFILE_OPERATION, ProfileSetupActivity.PROFILE_ADD);
                        profileSetupIntent.putExtras(extras);
                        startActivity(profileSetupIntent);
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), "Profile Not found", Toast.LENGTH_LONG).show();
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

    public void onClickAddServiceBtn(View view) {
        Intent serviceAddIntent = new Intent(getApplicationContext(), ServiceUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ServiceUpdateActivity.SERVICE_OPERATION, ServiceUpdateActivity.SERVICE_ADD);
        serviceAddIntent.putExtras(bundle);
        startActivity(serviceAddIntent);
    }

    public void goBackButton(View view) {
        onBackPressed();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (profileType.equals(USER_PROFILE)) {
            inflater.inflate(R.menu.context_profile_service_menu, menu);
        } else {
            inflater.inflate(R.menu.context_search_service_menu, menu);
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AvailableServiceHelper serviceHelper = (AvailableServiceHelper) serviceHelpers.get((int) menuInfo.id);
        switch (item.getItemId()) {
            case R.id.context_book_appointment:

                //Book Appointment

                return true;

            case R.id.context_delete_service:

                new AlertDialog.Builder(this)
                        .setTitle("Delete Service")
                        .setMessage("Are you sure you want to delete this Service ?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AvailableService service = new AvailableService();
                                service.setServiceId(serviceHelper.getServiceId());
                                service.setUserId(serviceHelper.getUserId());
                                service.deleteService(new AvailableServiceInterface() {
                                    @Override
                                    public void getBooleanResult(Boolean result) {
                                        Toast.makeText(getApplicationContext(), result?"Service Deleted":"Service couldn't be Deleted", Toast.LENGTH_SHORT).show();
                                        if (result) {
                                            serviceHelpers.remove((int) menuInfo.id);
                                            serviceAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            case R.id.context_edit_service:
                Intent serviceUpdateIntent = new Intent(getApplicationContext(), ServiceUpdateActivity.class);
                Bundle extras = new Bundle();
                extras.putString(ServiceUpdateActivity.SERVICE_OPERATION, ServiceUpdateActivity.SERVICE_MODIFY);
                extras.putString(ServiceUpdateActivity.SERVICE_ID, serviceHelper.getServiceId());
                serviceUpdateIntent.putExtras(extras);
                startActivity(serviceUpdateIntent);
                return true;
            case R.id.context_set_availability:
                AvailableService availableService = new AvailableService();
                availableService.setUserId(serviceHelper.getUserId());
                availableService.setServiceId(serviceHelper.getServiceId());
                if (serviceHelper.isAvailability())
                    availableService.disableAvailability(new AvailableServiceInterface() {
                        @Override
                        public void getBooleanResult(Boolean result) {
                            if (result) {
                                serviceHelper.setAvailability(false);
                                serviceHelpers.set((int) menuInfo.id, serviceHelper);
                                serviceAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Service Disabled", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Service couldn't be Disabled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                else
                    availableService.enableAvailability(new AvailableServiceInterface() {
                        @Override
                        public void getBooleanResult(Boolean result) {
                            if (result) {
                                serviceHelper.setAvailability(true);
                                serviceHelpers.set((int) menuInfo.id, serviceHelper);
                                serviceAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Service Enabled", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Service couldn't be Enabled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
