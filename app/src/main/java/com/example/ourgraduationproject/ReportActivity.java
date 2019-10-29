package com.example.ourgraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference myRef;
    User u;
    Button showViolations,Mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        showViolations = (Button)findViewById(R.id.showBtn);
        Mail = (Button)findViewById(R.id.mailBtn);
        u = new User();
        listView = (ListView) findViewById(R.id.listview);
        ArrayList<String> arrayList = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        DatabaseReference ref = dataSnapshot1.getRef().child("Violations");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postsnapshot1 : dataSnapshot.getChildren()) {
                                    violationClass vc = postsnapshot1.getValue(violationClass.class);
                                    arrayList.add(vc.getText()+" "+vc.getDate().toString());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        showViolations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String>listAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(listAdapter);
            }
        });
        Mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , arrayList.toString());
                i.putExtra(Intent.EXTRA_SUBJECT, "Traffic Violations Report");
                i.putExtra(Intent.EXTRA_TEXT   , arrayList.toString());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
