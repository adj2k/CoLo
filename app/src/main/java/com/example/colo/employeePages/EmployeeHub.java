package com.example.colo.employeePages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.EmployeeActivityLog;
import com.example.colo.GlobalCompanyName;
import com.example.colo.LogIn;
import com.example.colo.Projects.EmployeeProjects;
import com.example.colo.R;
import com.example.colo.SettingsPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeHub extends AppCompatActivity
{


    LinearLayout clock_in_out,e_projects_btn,e_settings_btn, layoutActivityLog;
    ConstraintLayout e_ann_btn;
    ImageView emp_logout;
    LinearLayout settingsButton;

    private DatabaseReference ann_ref;
    TextView hello_name, ann_title, ann_desc;
    String UID, first_name;
    private FirebaseAuth eAuth;
    FirebaseUser user;

    private String company_ref = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hub);

        hello_name = findViewById(R.id.e_username);
        clock_in_out = findViewById(R.id.clock_in_out_btn);
        emp_logout = findViewById(R.id.e_logout_btn);
        e_ann_btn = findViewById(R.id.e_ann_btn);
        e_projects_btn = findViewById(R.id.e_projects_btn);
        ann_title = findViewById(R.id.ann_title);
        ann_desc = findViewById(R.id.ann_desc);
        settingsButton = findViewById(R.id.e_settings_btn);
        //layoutActivityLog = findViewById(R.id.layoutActivityLog);

        eAuth = FirebaseAuth.getInstance();
        user = eAuth.getCurrentUser();
        UID = user.getUid();

        //import GlobalCompany class so we can access the database globally
        company_ref = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference d_ref = FirebaseDatabase.getInstance().getReference("Companies/" + company_ref).child(UID);
        ann_ref = FirebaseDatabase.getInstance().getReference("Companies/" + company_ref).child("Announcements");

        d_ref.child("name").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                first_name = snapshot.getValue(String.class);
                hello_name.setText(first_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies/" + company_ref).child(UID);
        DatabaseReference referenceCompany = FirebaseDatabase.getInstance().getReference("Companies/" + company_ref);

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
                                            LayoutInflater linf = LayoutInflater.from(EmployeeHub.this);
                                            final View inflator = linf.inflate(R.layout.onetime_popup, null);
                                            //setting the properties of the popup dialog
                                            AlertDialog dialog = new AlertDialog.Builder(EmployeeHub.this)
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
                                                                //startActivity(new Intent(EmployeeHub.this, EmployeeHub.class));
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


        clock_in_out.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, ClockIn.class));
            }
        });

        e_ann_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, CreateAnnouncement.class));
            }
        });

        ann_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String text = dataSnapshot.child("aTitle").getValue(String.class);
                    String description = dataSnapshot.child("aDescription").getValue(String.class);
                    ann_title.setText(text);
                    ann_desc.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        e_projects_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, EmployeeProjects.class));
            }
        });

        emp_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eAuth.signOut();
                startActivity(new Intent(EmployeeHub.this, LogIn.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, SettingsPage.class));
            }
        });

 /*       layoutActivityLog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, EmployeeActivityLog.class));
            }
        });*/

    }
}