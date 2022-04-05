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

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.EmployeeActivityLog;
import com.example.colo.GlobalCompanyName;
import com.example.colo.LogIn;
import com.example.colo.MainActivity;
import com.example.colo.ManagerHub;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeHub extends AppCompatActivity {

    LinearLayout clock_in_out,e_projects_btn,e_settings_btn, layoutActivityLog;
    ConstraintLayout e_ann_btn;
    ImageView emp_logout;

    private DatabaseReference ann_ref;
    TextView hello_name,ann_title,ann_desc;
    String UID, first_name;
    private FirebaseAuth eAuth;
    FirebaseUser user;

    private String company_ref = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hub);

        hello_name=findViewById(R.id.e_username);
        clock_in_out = findViewById(R.id.clock_in_out_btn);
        emp_logout = findViewById(R.id.e_logout_btn);
        e_ann_btn = findViewById(R.id.e_ann_btn);
        ann_title = findViewById(R.id.ann_title);
        ann_desc = findViewById(R.id.ann_desc);
        layoutActivityLog = findViewById(R.id.layoutActivityLog);

        eAuth = FirebaseAuth.getInstance();
        user = eAuth.getCurrentUser();
        UID = user.getUid();

        company_ref = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference d_ref = FirebaseDatabase.getInstance().getReference("Companies/"+company_ref).child(UID);
        ann_ref = FirebaseDatabase.getInstance().getReference("Companies/"+company_ref).child("Announcements");

        d_ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first_name = snapshot.getValue(String.class);
                hello_name.setText(first_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        company_ref = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies/"+company_ref).child(UID);

        DatabaseReference referenceCompany =  FirebaseDatabase.getInstance().getReference("Companies/"+company_ref);


        referenceCompany.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    if (snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        // Auth Key = Key in Company => proceed to check for role
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                    if (snapshot.getKey().equals("oneTimePassword")) {
                                        if (snapshot.getValue().equals((true))) {
                                            LayoutInflater linf = LayoutInflater.from(EmployeeHub.this);
                                            final View inflator = linf.inflate(R.layout.onetime_popup, null);
                                            AlertDialog dialog = new AlertDialog.Builder(EmployeeHub.this)
                                                    .setTitle("Please update your password")
                                                    .setView(inflator)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .create();

                                            final EditText UpdatePassword = (EditText) inflator.findViewById(R.id.updatePassword);
                                            final EditText ConfirmUpdatePassword = (EditText) inflator.findViewById(R.id.confirmUpdatePassword);


                                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                                @Override
                                                public void onShow(DialogInterface dialogInterface) {

                                                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                                    button.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View view) {
                                                            // TODO Do something

                                                            String updatePassword = UpdatePassword.getText().toString();
                                                            String confirmUpdatePassword = ConfirmUpdatePassword.getText().toString();


                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                                            //Update password and oneTimePassword in database
                                                            if (updatePassword.isEmpty()) {
                                                                UpdatePassword.setError("Field can not be empty");
                                                            } else if (confirmUpdatePassword.isEmpty()) {
                                                                ConfirmUpdatePassword.setError("Field can not be empty");
                                                            } else if (UpdatePassword.length() < 6) {
                                                                UpdatePassword.setError("Password needs to be at least 6 characters long");
                                                            } else if (!(updatePassword.equals(confirmUpdatePassword))) {
                                                                ConfirmUpdatePassword.setError("The passwords do not match");
                                                            } else {
                                                                reference.child("password").setValue(updatePassword);
                                                                reference.child("oneTimePassword").setValue(false);
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
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        clock_in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHub.this,ClockIn.class));
            }
        });

        e_ann_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHub.this, CreateAnnouncement.class));
            }
        });

        ann_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String text = dataSnapshot.child("aTitle").getValue(String.class);
                    String description = dataSnapshot.child("aDescription").getValue(String.class);
                    ann_title.setText(text);
                    ann_desc.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        layoutActivityLog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(EmployeeHub.this, EmployeeActivityLog.class));
            }
        });
    }
}