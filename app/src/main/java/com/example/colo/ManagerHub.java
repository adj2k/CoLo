package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colo.Announcements.AnnouncementList;
import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.example.colo.Projects.ManagerProjects;
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
    private DatabaseReference refAnnouncements;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID, first_name;
    TextView screen_name;
    TextView announcement_text, announcement_desc_text;
    LinearLayout EmployeeButton;
    LinearLayout AnnouncementButton;
    LinearLayout ProjectButton;
    LinearLayout ActivityLogButton;
    ImageButton ActivityButton;
    LinearLayout SettingsButton;
    ConstraintLayout AnnouncementButton2;
    ImageView LogoutButton;

    private String companyNameRef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_hub);

        //LogoutButton = (Button) findViewById(R.id.LogOut_btn);
        EmployeeButton = findViewById(R.id.layoutEmployees);
        ActivityLogButton = findViewById(R.id.layoutActivityLog);
        ProjectButton = findViewById(R.id.layoutProjects);
        //ActivityButton = (ImageButton) findViewById(R.id.activity_btn);
        SettingsButton = findViewById(R.id.layoutSettings);
        screen_name = findViewById(R.id.textUsername);
        announcement_text = findViewById(R.id.announcement_title_text);
        announcement_desc_text = findViewById(R.id.announcement_description_text);
        AnnouncementButton2 = findViewById(R.id.announcement_popup);
        LogoutButton = findViewById(R.id.logoutImage);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Manager");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        Log.i("UID: ", UID);

        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(UID);
        refAnnouncements = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child("Announcements");

        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first_name = snapshot.getValue(String.class);
                screen_name.setText(first_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refAnnouncements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String text = dataSnapshot.child("aTitle").getValue(String.class);
                    String description = dataSnapshot.child("aDescription").getValue(String.class);
                    announcement_text.setText(text);
                    announcement_desc_text.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ActivityLogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // CHANGE TO ACTIVITY LOG CLASS
                startActivity(new Intent(ManagerHub.this, ManagerCreateAnnouncement.class));
            }
        });

        AnnouncementButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ManagerCreateAnnouncement.class));
            }
        });

        EmployeeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ViewEmployees.class));
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
    }



}