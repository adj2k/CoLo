package com.example.colo.employeePages;

import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.colo.R;
import com.example.colo.GlobalCompanyName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeActivityLog extends AppCompatActivity {

    //String elSu, elMo, elTu, elWe, elTh, elFr, elSa, elTot;
    ArrayList<String> dOWHours = new ArrayList<>();
    String[] dOWLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    TextView elSu_tv, elMo_tv, elTu_tv, elWe_tv, elTh_tv, elFr_tv, elSa_tv, elTot_tv, elProj_tv;
    TextView elSu_tvl, elMo_tvl, elTu_tvl, elWe_tvl, elTh_tvl, elFr_tvl, elSa_tvl, elTot_tvl, elProj_tvl;
    TextView el_header;
    String weekString = "Total Hours for the week: ";
    String projString = "Total Hours on  " + "|||||" + ": ";
    String headerString = "Activity Log for week of:";

    private String companyNameRef = "";
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    long temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_log);

        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(userKey);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timeWorked = 0;
                if(snapshot.child("timeWorked").getValue() !=null) {
                    timeWorked = ((long) snapshot.child("timeWorked").getValue());
                    temp = timeWorked;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String sunAnswer = "" + TimeUnit.MILLISECONDS.toMinutes(temp);

        dOWHours.add("6");
        dOWHours.add("6");
        dOWHours.add("7");
        dOWHours.add("5");
        dOWHours.add("5");
        dOWHours.add("0");

        elSu_tv = (TextView) findViewById(R.id.sunday_hours);
        elMo_tv = (TextView) findViewById(R.id.monday_hours);
        elTu_tv = (TextView) findViewById(R.id.tuesday_hours);
        elWe_tv = (TextView) findViewById(R.id.wednesday_hours);
        elTh_tv = (TextView) findViewById(R.id.thursday_hours);
        elFr_tv = (TextView) findViewById(R.id.friday_hours);
        elSa_tv = (TextView) findViewById(R.id.saturday_hours);
        elTot_tv = (TextView) findViewById(R.id.act_week_total);
        elProj_tv = (TextView) findViewById(R.id.act_project_total);

        elSu_tvl = (TextView) findViewById(R.id.sunday);
        elMo_tvl = (TextView) findViewById(R.id.monday);
        elTu_tvl = (TextView) findViewById(R.id.tuesday);
        elWe_tvl = (TextView) findViewById(R.id.wednesday);
        elTh_tvl = (TextView) findViewById(R.id.thursday);
        elFr_tvl = (TextView) findViewById(R.id.friday);
        elSa_tvl = (TextView) findViewById(R.id.saturday);
        elTot_tvl = (TextView) findViewById(R.id.act_week_total_text);
        elProj_tvl = (TextView) findViewById(R.id.act_project_total_text);
        el_header = (TextView) findViewById(R.id.el_header);

        elSu_tv.setText(sunAnswer);
        elMo_tv.setText(dOWHours.get(0));
        elTu_tv.setText(dOWHours.get(1));
        elWe_tv.setText(dOWHours.get(2));
        elTh_tv.setText(dOWHours.get(3));
        elFr_tv.setText(dOWHours.get(4));
        elSa_tv.setText(dOWHours.get(5));

        String totalAnswer = "" + (Integer.parseInt(sunAnswer) + Integer.parseInt(dOWHours.get(0))
                + Integer.parseInt(dOWHours.get(1)) + Integer.parseInt(dOWHours.get(2)) + Integer.parseInt(dOWHours.get(3))
                + Integer.parseInt(dOWHours.get(4)) + Integer.parseInt(dOWHours.get(5)));

        elTot_tv.setText(totalAnswer);

        elSu_tvl.setText(dOWLabels[0]);
        elMo_tvl.setText(dOWLabels[1]);
        elTu_tvl.setText(dOWLabels[2]);
        elWe_tvl.setText(dOWLabels[3]);
        elTh_tvl.setText(dOWLabels[4]);
        elFr_tvl.setText(dOWLabels[5]);
        elSa_tvl.setText(dOWLabels[6]);
        elTot_tvl.setText(weekString);
        elProj_tvl.setText(projString);
        el_header.setText(headerString);


    }
}