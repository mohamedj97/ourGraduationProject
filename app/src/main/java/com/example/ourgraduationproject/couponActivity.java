package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class couponActivity extends AppCompatActivity {
    ArrayList<couponClass> list;
    ArrayList<couponClass> list1;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rf = database.getReference("Coupons");
    couponClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        final EditText cid = (EditText) findViewById(R.id.editText1);
        final EditText ctype = (EditText) findViewById(R.id.editText2);
        final EditText cown = (EditText) findViewById(R.id.editText3);
        final Button addcop = (Button) findViewById(R.id.Addbutton);
        list = new ArrayList<couponClass>();
        list1 = new ArrayList<couponClass>();
        Integer x;
        addcop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// here we must get the number of voilations if is specific number then we add coupon to user


                Calendar c = Calendar.getInstance();
                String today = DateFormat.getDateInstance().format(c.getTime());

                c.add(Calendar.DATE, 3);

                String future = DateFormat.getDateInstance().format(c.getTime());


                cc = new couponClass();
                rf.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            cc = ds.getValue(couponClass.class);
                            list.add(cc);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                int sze = Integer.parseInt(cid.getText().toString());

                cc = new couponClass(sze, ctype.getText().toString(),
                        cown.getText().toString(), today, future);
                rf.push().setValue(cc);
                Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_LONG).show();


            }

        });

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cc = ds.getValue(couponClass.class);
                    list1.add(cc);
                }

                for (int i = 0; i < list.size(); i++) {
                    if (list1.get(i).date == list1.get(i).vaidation) {
                        rf.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    cc = ds.getValue(couponClass.class);
                                    list1.remove(cc);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

