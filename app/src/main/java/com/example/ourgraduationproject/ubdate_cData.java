package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ubdate_cData extends AppCompatActivity {
ArrayList<sponser>sponsers;
Spinner sp;
    EditText onam,addres,phon,mail;
    Button sa;
    sponser ad;
    ImageButton ib;
    admin adm;
    String[] cnamn;
    ArrayList<String> stringArrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ubdate_c_data );
        onam=(EditText)findViewById( R.id.Oname );
        stringArrayList = new ArrayList<String>();
        addres=(EditText)findViewById( R.id.Adresss );
        phon=(EditText)findViewById( R.id.Phon );
        mail=(EditText)findViewById( R.id.Mail );
        ib=(ImageButton)findViewById( R.id.imageButton );
        sa=(Button)findViewById( R.id.button2 );
        adm=new admin(this);
        sponsers=new ArrayList<>(  );
        cnamn=new String[]{""};
        sp=(Spinner)findViewById( R.id.spinner );
        for(int i=0;i<sponsers.size();i++)
        {
            stringArrayList.add( sponsers.get( i ).snam );
        }
        cnamn=stringArrayList.toArray(new String[stringArrayList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cnamn);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
ib.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getsponsers();
       if(sponsers.size()!=0)
       {
           for(int i=0;i<sponsers.size();i++)
           {
               stringArrayList.add( sponsers.get( i ).snam );
           }
           cnamn=stringArrayList.toArray(new String[stringArrayList.size()]);
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext() , android.R.layout.simple_spinner_item, cnamn);
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           sp.setAdapter(adapter);
       }
    }
} );
        sp.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onam.setText( "" );
                addres.setText( "" );
                phon.setText( "" );
                mail.setText( "" );

                for(int i=0;i<sponsers.size();i++)
                {
                    String selectOp = sp.getSelectedItem().toString();
                    if(selectOp.equals( sponsers.get( i ).snam ));
                    {
                        onam.setText( sponsers.get( i ).ownerNAm );
                        addres.setText( sponsers.get( i ).addres );
                        phon.setText( sponsers.get( i ).phonenum );
                        mail.setText( sponsers.get( i ).email );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        sa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad=new sponser( sp.getSelectedItem().toString(),onam.getText().toString(),addres.getText().toString(),phon.getText().toString(),mail.getText().toString() );
                adm.updatedata( ad );
                Toast.makeText( getApplicationContext(),"Data updated",Toast.LENGTH_LONG ).show();

            }
        } );
    }
    public void getsponsers() {

        FirebaseDatabase.getInstance().getReference().child( "Sponsors" ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snp : dataSnapshot.getChildren()) {
                    sponser c = snp.getValue( sponser.class );
                    sponsers.add( c );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
