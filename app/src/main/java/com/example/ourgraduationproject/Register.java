package com.example.ourgraduationproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourgraduationproject.Models.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Register extends Activity implements View.OnClickListener {
    private static final String TAG = "act";
    private EditText usernameED;
    private EditText addressED;
    private EditText emailED;
    private EditText passwordED;
    private EditText nameED;
    private EditText passwordConfirmED;
    private EditText licenseED;
    private EditText securityQuestionAnswerED;
    private RadioButton radiomale;
    private RadioButton radiofemale;
    private TextView dateviwier;
    private TextView gotosignin;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private String date;
    private DatabaseReference myRef;
    DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        nameED = findViewById(R.id.regEBname);
        usernameED = findViewById(R.id.regEBusername);
        licenseED = findViewById(R.id.regEBlicense);
        addressED = findViewById(R.id.regEBaddress);
        emailED = findViewById(R.id.regEBemail);
        passwordED = findViewById(R.id.regEBpassword);
        passwordConfirmED = findViewById(R.id.regEBConfpassword);
        securityQuestionAnswerED = findViewById(R.id.regEBanswer);
        radiomale = findViewById(R.id.radioButtonMale);
        radiofemale = findViewById(R.id.radioButtonFemal);
        dateviwier = findViewById(R.id.textviewDate);
        registerButton = findViewById(R.id.regBTNregister);
        gotosignin = findViewById(R.id.signinhere);

        registerButton.setOnClickListener(this);
        gotosignin.setOnClickListener(this);


        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                date = month + "/" + dayOfMonth + "/" + year;
            }
        };
        dateviwier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myRef.child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String consultaName = areaSnapshot.getValue(String.class);
                    nomeConsulta.add(consultaName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, nomeConsulta);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

   @Override
    public void onClick(View v) {
        if (v == registerButton) {
            Registerdata();
        }
        if (v == gotosignin) {
            finish();
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
        }
    }

    private void Registerdata() {
        final String email = emailED.getText().toString().trim();
        final String name = nameED.getText().toString();
        final String username = usernameED.getText().toString().trim();
        final String password = passwordED.getText().toString().trim();
        final String cofirmpass = passwordConfirmED.getText().toString();
        final String license = licenseED.getText().toString();
        final String securityquestionanswer = securityQuestionAnswerED.getText().toString();
        final String address = addressED.getText().toString();
        final String gender;
        final String date2;
        date2 = date;

        if (radiomale.isChecked()) {
            gender = "male";
        } else {
            gender = "female";
        }


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(license) || TextUtils.isEmpty(securityquestionanswer) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enusre all fields are filled", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(cofirmpass)) {
            Toast.makeText(this, "Password fields doesn't match", Toast.LENGTH_LONG).show();
            return;
        } else {
            final userData user = new userData(name, username, address, gender, date2, license, securityquestionanswer);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                } else {
                                    Toast.makeText(Register.this, "Registration unsuccessful missing data", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        String s = task.getException().toString();
                        Toast.makeText(Register.this, s, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

}
