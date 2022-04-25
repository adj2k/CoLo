package com.example.colo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.example.colo.Projects.ManagerProjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHub extends AppCompatActivity
{

    //public Button LogOut;
    //public TextView Email;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference refAnnouncements;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID, first_name;
    TextView screen_name;
    TextView announcement_text, announcement_desc_text;
    ImageView LogoutButton;
    LinearLayout EmployeeButton;
    //LinearLayout AnnouncementButton;
    LinearLayout ManagersButton;
    LinearLayout ProjectButton;
    //LinearLayout ActivityLogButton;
    LinearLayout SettingsButton;
    ConstraintLayout AnnouncementButton;


    private String companyNameRef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hub);


        //

        //LogoutButton = (Button) findViewById(R.id.LogOut_btn);
        EmployeeButton = findViewById(R.id.layoutEmployees_Admin);
        //ActivityLogButton = findViewById(R.id.layoutActivityLog_Admin);
        ProjectButton = findViewById(R.id.layoutProjects_Admin);
        SettingsButton = findViewById(R.id.layoutSettings_Admin);
        screen_name = findViewById(R.id.textUsername_Admin);
        announcement_text = findViewById(R.id.announcement_title_text_Admin);
        announcement_desc_text = findViewById(R.id.announcement_description_text_Admin);
        AnnouncementButton = findViewById(R.id.announcement_popup_admin);
        ManagersButton = findViewById(R.id.layoutManagers);
        LogoutButton = findViewById(R.id.imageMenu_Admin);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Admin");
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


        /*ActivityLogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // CHANGE TO ACTIVITY LOG CLASS
                startActivity(new Intent(AdminHub.this, ManagerCreateAnnouncement.class));
            }
        });*/

        AnnouncementButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdminHub.this, ManagerCreateAnnouncement.class));
            }
        });

        EmployeeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdminHub.this, ViewEmployees.class));
            }
        });

        ProjectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdminHub.this, ManagerProjects.class));
            }
        });

        SettingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdminHub.this, adminSettingsPage.class));
            }
        });

        ManagersButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHub.this, ViewManagers.class));
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                startActivity(new Intent(AdminHub.this, LogIn.class));
            }
        });


    }
}