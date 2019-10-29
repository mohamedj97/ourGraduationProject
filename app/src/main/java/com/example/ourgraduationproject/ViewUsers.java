package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {
Button b;
ListView list;
TextView t;
    ArrayList<User>  users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_users );
        b=(Button)findViewById( R.id.viewU );
        list=(ListView)findViewById( R.id.ListUsers );
        t=(TextView)findViewById( R.id.textView9 );
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setVisibility( View. VISIBLE);
                getusres();
            refresh();

            }
        } );

    }
    public void refresh()
    {
        UserAdpter adpter_prouduct = new UserAdpter( ViewUsers.this, users );
        list.setAdapter( adpter_prouduct );
    }
    public void getusres() {

        FirebaseDatabase.getInstance().getReference().child( "DataBase" ).child( "User" ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snp : dataSnapshot.getChildren()) {
                    User c = snp.getValue( User.class );
                    users.add( c );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
