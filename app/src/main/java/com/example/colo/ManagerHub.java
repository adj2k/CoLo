package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colo.Announcements.AnnouncementList;
import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.example.colo.Projects.ManagerProjects;
import com.example.colo.employeePages.EmployeeHub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ManagerHub extends AppCompatActivity
{

    // Variable Setup
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
    EditText UpdatePassword, ConfirmUpdatePassword;
    LinearLayout EmployeeButton;
    LinearLayout AnnouncementButton;
    LinearLayout ProjectButton;
    LinearLayout ActivityLogButton;
    ImageButton ActivityButton;
    LinearLayout SettingsButton;
    ConstraintLayout AnnouncementButton2;
    ImageView LogoutButton;
    private String companyNameRef = "";
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String m_Text = "";

    // On Page Creation (When Page is loaded do this: )
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //import GlobalCompany class so we can access the database globally
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(userKey);
        DatabaseReference referenceCompany = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef);

        //find information in the company child
        referenceCompany.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                //loop through companies in the database
                for (DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    //if the current user has the same UID as the database key, proceed to password change
                    if (snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        //find information in the user ID child
                        reference.addValueEventListener(new ValueEventListener()
                        {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot datasnapshot)
                            {
                                //loop through attributes for the user to find the oneTimePassword key
                                for (DataSnapshot snapshot : datasnapshot.getChildren())
                                {
                                    //if the key is equal to oneTimePassword
                                    if (snapshot.getKey().equals("oneTimePassword"))
                                    {
                                        //and if the value of oneTimePassword is true
                                        if (snapshot.getValue().equals((true)))
                                        {
                                            //show popup and prompt user to enter a new password
                                            LayoutInflater linf = LayoutInflater.from(ManagerHub.this);
                                            final View inflator = linf.inflate(R.layout.onetime_popup, null);
                                            //setting the properties of the popup dialog
                                            AlertDialog dialog = new AlertDialog.Builder(ManagerHub.this)
                                                    .setTitle("Please update your password")
                                                    .setView(inflator)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .create();

                                            //linking the xml widgets to the java
                                            final EditText UpdatePassword = (EditText) inflator.findViewById(R.id.updatePassword);
                                            final EditText ConfirmUpdatePassword = (EditText) inflator.findViewById(R.id.confirmUpdatePassword);

                                            //overwrite the positive button
                                            dialog.setOnShowListener(new DialogInterface.OnShowListener()
                                            {

                                                @Override
                                                public void onShow(DialogInterface dialogInterface)
                                                {

                                                    //add listener for the "ok" button
                                                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                                    button.setOnClickListener(new View.OnClickListener()
                                                    {

                                                        @Override
                                                        public void onClick(View view)
                                                        {
                                                            // TODO Do something

                                                            //taking the information the user entered and saving it as a string
                                                            String updatePassword = UpdatePassword.getText().toString();
                                                            String confirmUpdatePassword = ConfirmUpdatePassword.getText().toString();

                                                            //accessing the current user
                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                                            //Update password and oneTimePassword in database
                                                            if (updatePassword.isEmpty())
                                                            {
                                                                UpdatePassword.setError("Field can not be empty");
                                                            } else if (confirmUpdatePassword.isEmpty())
                                                            {
                                                                ConfirmUpdatePassword.setError("Field can not be empty");
                                                            } else if (UpdatePassword.length() < 6)
                                                            {
                                                                UpdatePassword.setError("Password needs to be at least 6 characters long");
                                                            } else if (!(updatePassword.equals(confirmUpdatePassword)))
                                                            {
                                                                ConfirmUpdatePassword.setError("The passwords do not match");
                                                            } else
                                                            {
                                                                reference.child("oneTimePassword").setValue(false);
                                                                reference.child("password").setValue(updatePassword);
                                                                //Dismiss once everything is OK.
                                                                user.updatePassword(updatePassword);
                                                                dialog.dismiss();

                                                            }
                                                        }

                                                        ;
                                                    });
                                                }
                                            });


                                            dialog.show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });
                    } else
                    {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_hub);

        // Grabs All Button/Text ID's from the XML and assigns them to corresponding variable
        //LogoutButton = (Button) findViewById(R.id.LogOut_btn);
        EmployeeButton = findViewById(R.id.layoutEmployees);
        //ActivityLogButton = findViewById(R.id.layoutActivityLog);
        ProjectButton = findViewById(R.id.layoutProjects);
        //ActivityButton = (ImageButton) findViewById(R.id.activity_btn);
        SettingsButton = findViewById(R.id.layoutSettings);
        screen_name = findViewById(R.id.textUsername);
        announcement_text = findViewById(R.id.announcement_title_text);
        announcement_desc_text = findViewById(R.id.announcement_description_text);
        AnnouncementButton2 = findViewById(R.id.announcement_popup);
        LogoutButton = findViewById(R.id.logoutImage);

        // Database Setup
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Manager");
        mAuth = FirebaseAuth.getInstance();
        // Get's current user
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        Log.i("UID: ", UID);

        // Grabs Company Name, and uses it to reference the user of the company and the announcements
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(UID);
        refAnnouncements = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child("Announcements");

        // This function sets the "Hello NameHere" to user's name
        ref.child("name").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                first_name = snapshot.getValue(String.class);
                screen_name.setText(first_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function sets the latest announcement to the top announcement in database
        refAnnouncements.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    // Gets text from database
                    String text = dataSnapshot.child("aTitle").getValue(String.class);
                    String description = dataSnapshot.child("aDescription").getValue(String.class);
                    // Sets text on the page XML
                    announcement_text.setText(text);
                    announcement_desc_text.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function will make the page go to Activity Log on button click
        /*ActivityLogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // CHANGE TO ACTIVITY LOG CLASS
                startActivity(new Intent(ManagerHub.this, ManagerCreateAnnouncement.class));
            }
        });*/

        // This function will make the page go to Manager Announcement on button click
        AnnouncementButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ManagerCreateAnnouncement.class));
            }
        });
// This function will make the page go to View Employee on button click
        EmployeeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ViewEmployees.class));
            }
        });

        // This function will make the page go to Manager Project Page on button click
        ProjectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, ManagerProjects.class));
            }
        });

        // This function will make the page go to Activity Log on button click
        SettingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManagerHub.this, SettingsPage.class));
            }
        });

        // This function will log the user out and make the page go to Login page on button click
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