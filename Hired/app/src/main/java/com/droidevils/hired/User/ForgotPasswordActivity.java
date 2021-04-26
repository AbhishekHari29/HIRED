package com.droidevils.hired.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidevils.hired.Common.LoadingDialog;
import com.droidevils.hired.Helper.Validation;
import com.droidevils.hired.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    private TextView headerText;
    private TextInputLayout emailLayout;
    private Button sendResetBtn, goToLoginBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        loadingDialog = new LoadingDialog(ForgotPasswordActivity.this);
        headerText = (TextView) findViewById(R.id.header);
        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        sendResetBtn = (Button) findViewById(R.id.reset_button);
        goToLoginBtn = (Button) findViewById(R.id.goto_login);

        mAuth = FirebaseAuth.getInstance();

        sendResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                String emailId = emailLayout.getEditText().getText().toString().trim();

                if (!Validation.validateEmail(emailLayout)){
                    Toast.makeText(ForgotPasswordActivity.this, "Validation Failed", Toast.LENGTH_LONG).show();
                    loadingDialog.stopLoadingDialog();
                    return;
                }

                mAuth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Reset Link Sent", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class);
                            loadingDialog.stopLoadingDialog();
                            startActivity(intent);
                            finish();
                        }
                        else {
                            loadingDialog.stopLoadingDialog();
                            Toast.makeText(ForgotPasswordActivity.this, "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(headerText, "header_text");
                pairs[1] = new Pair<View, String>(emailLayout, "email_trans");
                pairs[2] = new Pair<View, String>(sendResetBtn, "button_trans");
                pairs[3] = new Pair<View, String>(goToLoginBtn, "goto_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgotPasswordActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });

    }
}