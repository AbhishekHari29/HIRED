package com.droidevils.hired.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor sessionEditor;
    Context context;

    public static final String SESSION_USERSESSION = "userSession";
    public static final String SESSION_REMEMBERME = "rememberMeSession";

    private static final String IS_LOGIN = "isLoggedIn";
    private static final String IS_REMEMBERME = "isRememberMe";

    //User Session
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERID = "userId";
    public static final String KEY_EMAILID = "EmailId";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String KEY_USERTYPE = "userType";
    public static final String KEY_PASSWORD = "password";

    //Remember Me
    public static final String KEY_RM_EMAILID = "EmailId";
    public static final String KEY_RM_PASSWORD = "password";


    public SessionManager(Context context, String sessionType) {
        this.context = context;
        userSession = context.getSharedPreferences(sessionType, Context.MODE_PRIVATE);
        sessionEditor = userSession.edit();
    }


    //User Session
    public void createLoginSession(String fullName, String userId, String emailId, String phone, String userType, String password) {
        sessionEditor.putBoolean(IS_LOGIN, true);
        sessionEditor.putString(KEY_FULLNAME, fullName);
        sessionEditor.putString(KEY_USERID, userId);
        sessionEditor.putString(KEY_EMAILID, emailId);
        sessionEditor.putString(KEY_PHONE, phone);
        sessionEditor.putString(KEY_USERTYPE, userType);
        sessionEditor.putString(KEY_PASSWORD, password);

        sessionEditor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_FULLNAME, userSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERID, userSession.getString(KEY_USERID, null));
        userData.put(KEY_EMAILID, userSession.getString(KEY_EMAILID, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_USERTYPE, userSession.getString(KEY_USERTYPE, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));

        return userData;
    }

    public boolean isLoginSession() {
        return userSession.getBoolean(IS_LOGIN, false);
    }

    public void logoutUserFromSession() {
        sessionEditor.clear();
        sessionEditor.commit();
    }

    //Remember Me Session
    //User Session
    public void createRememberMeSession(String emailId, String password) {
        sessionEditor.putBoolean(IS_REMEMBERME, true);
        sessionEditor.putString(KEY_RM_EMAILID, emailId);
        sessionEditor.putString(KEY_RM_PASSWORD, password);

        sessionEditor.commit();
    }

    public HashMap<String, String> getRememberMeFromSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_RM_EMAILID, userSession.getString(KEY_RM_EMAILID, null));
        userData.put(KEY_RM_PASSWORD, userSession.getString(KEY_RM_PASSWORD, null));

        return userData;
    }

    public boolean isRememberMe() {
        return userSession.getBoolean(IS_REMEMBERME, false);
    }

    public void removeRememberMeFromSession() {
        sessionEditor.clear();
        sessionEditor.commit();
    }
}
