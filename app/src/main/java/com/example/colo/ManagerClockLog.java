package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerClockLog extends AppCompatActivity {
    private String companyNameRef = "";
    private Button reset_time_btn;
    private Button return_to_hub_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_clock_log);

        // get company of Manager
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();

        // get Database Ref
        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef);
        reset_time_btn = (Button) findViewById(R.id.reset_time_btn);
        reset_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetWorkTime();
            }
        });
        return_to_hub_btn = findViewById(R.id.return_to_hub_btn);
        return_to_hub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();}
        });
    }

    // loop through employees collecting there info and clearing timeWorked
    private void resetWorkTime() {
        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef);

        companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    if(snapshot.child("timeWorked").exists()) {
                        System.out.println("This is running????????");
                        companyRef.child(snapshot.getKey()).child("timeWorked").setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}