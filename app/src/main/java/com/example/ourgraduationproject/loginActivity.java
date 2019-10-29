package com.example.ourgraduationproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends Activity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private TextView forgotpass;
    private TextView Help;
    private Button loginBtn;
    private Button goRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox rememberme;
    private Boolean isadmin = false;
    private SharedPreferences loginPreferences;
    private static final String prefs_name = "prefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.loginemail);
        passwordText = findViewById(R.id.loginpassword);
        loginBtn = findViewById(R.id.loginBTN);
        Help = findViewById(R.id.helpText);
        goRegister = findViewById(R.id.goRegister);
        rememberme = findViewById(R.id.RembmerMeCheckBox);
        forgotpass = findViewById(R.id.forgotpassword);
        progressDialog = new ProgressDialog(this);
        loginPreferences = getSharedPreferences(prefs_name, MODE_PRIVATE);
        loginBtn.setOnClickListener(this);
        goRegister.setOnClickListener(this);
        forgotpass.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        getprefrenceDate();
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), helpActivity.class));
            }
        });
    }

    private void getprefrenceDate() {
        SharedPreferences sp = getSharedPreferences(prefs_name, MODE_PRIVATE);
        if (sp.contains("pref_email")) {
            String em = sp.getString("pref_email", "not found.");
            emailText.setText(em.toString());
        }
        if (sp.contains("pref_pass")) {
            String pass = sp.getString("pref_pass", "not found.");
            passwordText.setText(pass.toString());
        }
        if (sp.contains("pref_check")) {
            boolean ch = sp.getBoolean("pref_check", false);
            rememberme.setChecked(ch);
        }

    }

    private void userlogin() {
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enusre all fields are filled", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Logging in....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            if (rememberme.isChecked()) {


                                Boolean ischecked = rememberme.isChecked();
                                SharedPreferences.Editor editor = loginPreferences.edit();
                                editor.putString("pref_email", email);
                                editor.putString("pref_pass", password);
                                editor.putBoolean("pref_check", ischecked);
                                editor.apply();

                            } else {
                                loginPreferences.edit().clear().apply();
                            }
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("admin")) {
                                        Toast.makeText(getApplicationContext(), "admin", Toast.LENGTH_SHORT).show();

                                    /*   mp  = MediaPlayer.create(getApplicationContext(),R.raw.loginsound);
                                       mp.start();*/

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), adminActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "not admin", Toast.LENGTH_SHORT).show();
                                      /* mp  = MediaPlayer.create(getApplicationContext(),R.raw.loginsound);
                                       mp.start();*/
                                        finish();
                                        Intent intent = new Intent(loginActivity.this, profileActivity.class);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Toast.makeText(loginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getInstance().getCurrentUser() != null) {

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("admin")) {
                        Toast.makeText(getApplicationContext(), "admin", Toast.LENGTH_SHORT).show();

                                     /* mp  = MediaPlayer.create(getApplicationContext(),R.raw.loginsound);
                                       mp.start();*/

                        startActivity(new Intent(getApplicationContext(), adminActivity.class));
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "not admin", Toast.LENGTH_SHORT).show();
                                      /* mp  = MediaPlayer.create(getApplicationContext(),R.raw.loginsound);
                                       mp.start();*/
                        startActivity(new Intent(getApplicationContext(), profileActivity.class));
                        finish();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {

        }


    }

    @Override
    public void onClick(View v) {

        if (v == loginBtn) {
            userlogin();
        }
        if (v == goRegister) {
            startActivity(new Intent(this, Register.class));

        }
        if (v == forgotpass) {

            startActivity(new Intent(this, forgotPassworActivity.class));
        }
    }
}
