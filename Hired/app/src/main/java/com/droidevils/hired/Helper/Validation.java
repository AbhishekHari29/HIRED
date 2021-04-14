package com.droidevils.hired.Helper;

import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

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
            layout.setError("Select User Type");
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
}
