package com.example.ourgraduationproject;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin {

    public String usernae;
    public String password;
    Context c;

    public admin(Context con) {
        c = con;
    }

    public void addsponser(String snam, String ownerNAm, String addres, String phonenum, String mail) {
        sponser s = new sponser(snam, ownerNAm, addres, phonenum, mail);
        FirebaseApp.initializeApp(c);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sponsors");
        ref.push().setValue(s);
    }

    public void updatedata(final sponser c) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sponsors");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    sponser cu = postSnapshot.getValue(sponser.class);
                    if (c.snam.equals(cu.snam)) {
                        DatabaseReference re = postSnapshot.getRef().child("ownerNAm");
                        re.setValue(c.ownerNAm);
                        re = postSnapshot.getRef().child("addres");
                        re.setValue(c.addres);
                        re = postSnapshot.getRef().child("phonenum");
                        re.setValue(c.phonenum);
                        re = postSnapshot.getRef().child("email");
                        re.setValue(c.email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
