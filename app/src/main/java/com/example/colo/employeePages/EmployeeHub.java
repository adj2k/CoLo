package com.example.colo.employeePages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.LogIn;
import com.example.colo.MainActivity;
import com.example.colo.ManagerHub;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeeHub extends AppCompatActivity {

    LinearLayout clock_in_out,e_projects_btn,e_settings_btn;
    ConstraintLayout e_ann_btn;
    ImageView emp_logout;
    private FirebaseAuth eAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hub);

        clock_in_out = findViewById(R.id.clock_in_out_btn);
        emp_logout = findViewById(R.id.e_logout_btn);
        eAuth = FirebaseAuth.getInstance();
        e_ann_btn = findViewById(R.id.e_ann_btn);

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
                startActivity(new Intent(EmployeeHub.this, LogIn.class));
            }
        });
    }
}