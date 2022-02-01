package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerHub extends AppCompatActivity
{

    public Button LogOut;
    public TextView Email;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_hub);

        LogOut = (Button) findViewById(R.id.btnLogOut);
        Email = (TextView) findViewById(R.id.tvEmail) ;

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Employee");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String email = dataSnapshot.child(UID).child("email").getValue(String.class);
                    Email.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }else
        {
            startActivity(new Intent(ManagerHub.this, MainActivity.class));
            finish();
        }

        LogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                startActivity(new Intent(ManagerHub.this, MainActivity.class));
            }
        });

    }



}