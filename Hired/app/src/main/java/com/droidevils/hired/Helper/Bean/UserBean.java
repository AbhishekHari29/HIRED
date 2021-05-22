package com.droidevils.hired.Helper.Bean;

public class UserBean {

    public  static final String KEY_USER_ID = "KEY_USER_ID";
    public  static final String KEY_USER_TYPE = "KEY_USER_TYPE";

    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String password;
    private String userType;
    private String createdAt;


    public UserBean() {
    }

    public UserBean(String fullName, String emailId, String phoneNumber, String password, String userType, String createdAt) {
        this.fullName = fullName;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userType = userType;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
