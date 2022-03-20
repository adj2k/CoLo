package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.example.colo.Projects.ManagerProjects;
import com.example.colo.employeePages.EmployeeHub;
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
    Button LogoutButton;
    ImageButton EmployeeButton;
    Button AnnouncementButton;
    ImageButton ProjectButton;
    ImageButton ActivityButton;
    ImageButton SettingsButton;
    private String companyNameRef = "";
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(userKey);

        DatabaseReference referenceCompany =  FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef);



        referenceCompany.addValueEventListener(new ValueEventListener() {
            @Override



            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    if(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        // Auth Key = Key in Company => proceed to check for role
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                    if(snapshot.getKey().equals("oneTimePassword")) {
                                        if(snapshot.getValue().equals((true))) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerHub.this);
                                            builder.setTitle("Title");

                                            // Set up the input
                                            final EditText input = new EditText(ManagerHub.this);
                                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            builder.setView(input);


                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text = input.getText().toString();
                                                    reference.child("oneTimePassword").setValue(false);
                                                }
                                            });

                                            builder.show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        // Return
//                                            Toast.makeText(LogIn.this, "YOU DON'T WORK FOR THIS COMPANY.", Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













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
                startActivity(new Intent(ManagerHub.this, ManagerCreateAnnouncement.class));
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