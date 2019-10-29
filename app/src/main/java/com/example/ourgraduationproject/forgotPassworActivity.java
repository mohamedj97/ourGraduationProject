package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassworActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private Button changeEmailBTN;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwor);
        emailText =  findViewById(R.id.editTextResetemail);
        changeEmailBTN =  findViewById(R.id.resetBtn);
        progressBar =  findViewById(R.id.progressBar);
        //----------------------------------------------------------
        changeEmailBTN.setOnClickListener((View.OnClickListener) this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == changeEmailBTN) {
            String email = emailText.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your email id", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(forgotPassworActivity.this, "Check your Email for further instructions!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(forgotPassworActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}