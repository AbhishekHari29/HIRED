package com.droidevils.hired.Helper;

import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean validateEmpty(View view){
        TextInputLayout layout = (TextInputLayout) view;
        String value = layout.getEditText().getText().toString().trim();
        if (value.isEmpty()){
            layout.setError(layout.getContentDescription() + " is required");
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateEmail(View view){
        TextInputLayout layout = (TextInputLayout) view;
        String value = layout.getEditText().getText().toString().trim();
        if (!validateEmpty(view)){
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            layout.setError("Invalid Email Address");
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validatePassword(View view){
        TextInputLayout layout = (TextInputLayout) view;
        String value = layout.getEditText().getText().toString().trim();
        if (!validateEmpty(view)){
            return false;
        } else if (value.length() < 8){
            layout.setError("Password is too short");
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateDropDown(TextInputLayout layout, AutoCompleteTextView atv){
        String value = atv.getText().toString().trim();
        if (value.isEmpty() || value.equals("")){
            layout.setError("Select "+layout.getContentDescription());
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean comparePassword(View view1, View view2){
        TextInputLayout layout1 = (TextInputLayout) view1;
        TextInputLayout layout2 = (TextInputLayout) view2;
        String value1 = layout1.getEditText().getText().toString().trim();
        String value2 = layout2.getEditText().getText().toString().trim();
        if (!validateEmpty(layout2)){
            return false;
        } else if (!value1.equals(value2)){
            layout2.setError("Password doesn't match");
            return false;
        }
        else {
            layout2.setError(null);
            layout2.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validatePhone(View view) {
        TextInputLayout layout = (TextInputLayout) view;
        String value = layout.getEditText().getText().toString().trim();
        if (!validateEmpty(view)){
            return false;
        } else if (!Patterns.PHONE.matcher(value).matches()){
            layout.setError("Invalid Phone Number");
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateTime(String time) {
        String regexPattern = "(1[012]|0?[1-9]):" + "[0-5][0-9](\\s)" + "?(?i)(am|pm)";
        Pattern compiledPattern = Pattern.compile(regexPattern);
        if (time == null) {
            return false;
        }
        Matcher m = compiledPattern.matcher(time);
        return m.matches();
    }


    public static boolean validateDate(String value) {
        String regexPattern = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        Pattern compiledPattern = Pattern.compile(regexPattern);
        if (value == null) {
            return false;
        }
        Matcher m = compiledPattern.matcher(value) ;
        return m.matches();
    }
}