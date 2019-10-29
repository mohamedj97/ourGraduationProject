package com.example.ourgraduationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class deletSponser extends AppCompatActivity {
    Button previewBtn;
    ListView list;
    TextView t;
    ArrayList<User> users;
    ArrayList<sponser> sponsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_delet_sponser );
        previewBtn=(Button)findViewById( R.id.viewS );
        list=(ListView)findViewById( R.id.ListSpon );
        t=(TextView)findViewById( R.id.textView10 );
        sponsers=new ArrayList<sponser>(  );
        users=new ArrayList<User>(  );
        previewBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getusres();
                for (int i=0;i<sponsers.size();i++)
                {
                    User u=new User();
                    u.name=sponsers.get( i ).snam;
                    u.adress=sponsers.get( i ).addres;
                    u.Email=sponsers.get( i ).email;
                    u.phone=sponsers.get( i ).phonenum;
                    users.add( u );
                }
                t.setVisibility( View. VISIBLE);
                refresh();
            }
        } );
        list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final User u=users.get( position );
                final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Sponsors");
                ref.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            sponser cu=postSnapshot.getValue(sponser.class);
                            if(u.name.equals( cu.snam ))
                            {
                                DatabaseReference re=postSnapshot.getRef();
                                re.setValue( null );
                                break;
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
                users.remove( u );
                refresh();
                Toast.makeText( getApplicationContext(),"Company Deleted",Toast.LENGTH_LONG ).show();
                return false;
            }
        } );

    }
    public void refresh()
    {
        UserAdpter adpter_prouduct = new UserAdpter( deletSponser.this, users);
        list.setAdapter( adpter_prouduct );
    }
    public void getusres() {

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