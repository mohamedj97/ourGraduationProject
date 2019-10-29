package com.example.ourgraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class adminActivity extends AppCompatActivity {
    private Button signoutBtn, CouponManagement, addSponsor, delSponsor, updateSponsor, viewUsers;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        firebaseAuth = firebaseAuth.getInstance();
        CouponManagement = (Button) findViewById(R.id.copounBtn);
        signoutBtn = (Button) findViewById(R.id.signoutadmin);
        addSponsor = (Button) findViewById(R.id.addSponCompany);
        delSponsor = (Button) findViewById(R.id.delSponsor);
        updateSponsor = (Button) findViewById(R.id.updateSpon);

        CouponManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminActivity.this, couponActivity.class);
                startActivity(intent);
            }
        });
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });

        addSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminActivity.this, addCom.class);
                startActivity(intent);
            }
        });

        delSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminActivity.this, deletSponser.class);
                startActivity(intent);
            }
        });

        updateSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminActivity.this, ubdate_cData.class);
                startActivity(intent);
            }
        });
    }
}
