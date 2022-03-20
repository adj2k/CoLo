package com.example.colo.employeePages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.GlobalCompanyName;
import com.example.colo.LogIn;
import com.example.colo.MainActivity;
import com.example.colo.ManagerHub;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeHub extends AppCompatActivity {

    ImageButton clock_in_out;
    Button  emp_logout, e_ann_btn;
    private FirebaseAuth eAuth;
    private String companyNameRef = "";
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hub);

        clock_in_out = (ImageButton) findViewById(R.id.clock_in_out_btn);
        emp_logout = (Button) findViewById(R.id.emp_logout_btn);
        eAuth = FirebaseAuth.getInstance();
        e_ann_btn = (Button) findViewById(R.id.e_ann_btn);

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

        emp_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eAuth.signOut();
                startActivity(new Intent(EmployeeHub.this, MainActivity.class));
            }
        });
    }
}