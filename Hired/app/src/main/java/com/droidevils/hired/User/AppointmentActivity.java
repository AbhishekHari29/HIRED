package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.droidevils.hired.R;

public class AppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);



    }



    public void onClickBookAppoitnment(View view){


    }

    public void goBackButton(View view) {
        onBackPressed();
    }

}