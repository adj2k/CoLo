package com.example.colo.employeePages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.colo.MainActivity;
import com.example.colo.ManagerHub;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeeHub extends AppCompatActivity {

    ImageButton clock_in_out;
    Button  emp_logout;
    private FirebaseAuth eAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hub);

        clock_in_out = (ImageButton) findViewById(R.id.clock_in_out_btn);
        emp_logout = (Button) findViewById(R.id.emp_logout_btn);
        eAuth = FirebaseAuth.getInstance();

        clock_in_out.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHub.this,ClockIn.class));
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