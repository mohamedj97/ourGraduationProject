package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addCom extends AppCompatActivity {

    EditText cnam, onam, addres, phon, mail;
    Button sa;
    admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_com);
        admin = new admin(this);
        cnam = (EditText) findViewById(R.id.cname);
        onam = (EditText) findViewById(R.id.oname);
        addres = (EditText) findViewById(R.id.addres);
        phon = (EditText) findViewById(R.id.phonnum);
        mail = (EditText) findViewById(R.id.mail);
        sa = (Button) findViewById(R.id.save);

        admin = new admin(this);
        sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.addsponser(cnam.getText().toString(), onam.getText().toString(), addres.getText().toString(), phon.getText().toString(), mail.getText().toString());
                Toast.makeText(getApplicationContext(), "Company Added", Toast.LENGTH_LONG).show();
            }
        });


    }
}
