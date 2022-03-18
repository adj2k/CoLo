package com.example.colo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.colo.Projects.ManagerProjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerHub extends AppCompatActivity
{

    public Button LogOut;
    public TextView Email;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;
    Button LogoutButton;
    ImageButton EmployeeButton;
    Button AnnouncementButton;
    ImageButton ProjectButton;
    ImageButton ActivityButton;
    ImageButton SettingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_hub);

        LogoutButton = (Button) findViewById(R.id.LogOut_btn);
        EmployeeButton = (ImageButton) findViewById(R.id.employee_btn);
        AnnouncementButton = (Button) findViewById(R.id.announcement_btn);
        ProjectButton = (ImageButton) findViewById(R.id.managers_btn);
        ActivityButton = (ImageButton) findViewById(R.id.activity_btn);
        SettingsButton = (ImageButton) findViewById(R.id.projects_btn);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Manager");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        AnnouncementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //startActivity(new Intent(ManagerHub.this, Announcement.class));
            }
        });

        EmployeeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, CreateAccount.class));
            }
        });

        ProjectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ManagerProjects.class));
            }
        });

        ActivityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ManagerClockLog.class));
            }
        });

        SettingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //startActivity(new Intent(ManagerHub.this, ManagerSettings.class));
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                startActivity(new Intent(ManagerHub.this, LogIn.class));
            }
        });
        /*
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
    */
    }



}