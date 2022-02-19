package com.example.colo.employeePages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class ClockIn extends AppCompatActivity {

    private Button clock_in_btn;
    private static final String CLOCKIN = "Clock In";


    // Write time of clockIn/ out to Firebase
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        // set up Firebase instance


        clock_in_btn = (Button) findViewById(R.id.clock_in_btn);
        clock_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClockInTime();
            }
        });


    }

    private void addClockInTime() {
        // get userID
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // get ref to be changed.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Employee").child(userKey);

        HashMap map = new HashMap();
        map.put("clockInTime", Calendar.getInstance());
        ref.updateChildren(map);
        Toast.makeText(ClockIn.this, "Clocked In", Toast.LENGTH_LONG).show();
    }
}