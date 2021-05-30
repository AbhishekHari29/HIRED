package com.droidevils.hired.Helper.Bean;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileBean {

    private String fullName, email, phone, summary;
    private String gender, birthDate, address, city, state, pincode;
    private String field, degree, institution, eduCity, eduState, graduationDate;
    private String jobTitle, company, location, jobDescription;
    private int jobExperience;
    private boolean stillStudying, stillWorking;

    public ProfileBean() {
    }

    public static void getLocationByPinCode(String userId, UserLocationInterface userLocationInterface) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Profile");
        reference.child(userId).child("pincode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pincode = snapshot.getValue(String.class);
                if (pincode != null) {
                    userLocationInterface.getLocation(UserLocation.getLocation(pincode));
                } else {
                    userLocationInterface.getLocation(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userLocationInterface.getLocation(null);
            }
        });

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getEduCity() {
        return eduCity;
    }

    public void setEduCity(String eduCity) {
        this.eduCity = eduCity;
    }

    public String getEduState() {
        return eduState;
    }

    public void setEduState(String eduState) {
        this.eduState = eduState;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public int getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(int jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public boolean isStillStudying() {
        return stillStudying;
    }

    public void setStillStudying(boolean stillStudying) {
        this.stillStudying = stillStudying;
    }

    public boolean isStillWorking() {
        return stillWorking;
    }

    public void setStillWorking(boolean stillWorking) {
        this.stillWorking = stillWorking;
    }
}
