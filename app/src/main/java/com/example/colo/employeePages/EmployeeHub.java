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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
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

    LinearLayout clock_in_out,e_projects_btn,e_settings_btn;
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHub.this);
                                            builder.setTitle("Title");

                                            // Set up the input
                                            final EditText input = new EditText(EmployeeHub.this);
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
    }
}