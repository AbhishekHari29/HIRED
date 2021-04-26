package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.droidevils.hired.R;

public class ProfileSetupActivity extends AppCompatActivity {

    private final int numberOfForm = 3;
    private int currentForm = 0;
    LinearLayout[] profilesForm;
    Button nextButton, previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        profilesForm = new LinearLayout[numberOfForm];
        profilesForm[0] = findViewById(R.id.profile1_layout);
        profilesForm[1] = findViewById(R.id.profile2_layout);
        profilesForm[2] = findViewById(R.id.profile3_layout);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

    }

    public void onNextPreviousClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                if (currentForm + 1 == numberOfForm)
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                else if (currentForm + 1 < numberOfForm) {
                    currentForm++;
                    changeFormTab(currentForm);
                } else return;
                break;
            case R.id.previous_button:
                if (currentForm - 1 >= 0) {
                    currentForm--;
                    changeFormTab(currentForm);
                } else return;
                break;
        }
        if (currentForm == numberOfForm - 1)
            nextButton.setText("Finish");
        else
            nextButton.setText("Next");
    }

    public void changeFormTab(int value) {
        for (LinearLayout layout : profilesForm)
            layout.setVisibility(View.GONE);
        profilesForm[value].setVisibility(View.VISIBLE);
    }
}