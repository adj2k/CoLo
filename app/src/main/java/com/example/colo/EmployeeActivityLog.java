package com.example.colo;

import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeActivityLog extends AppCompatActivity {

    //list to hold day of the week hours
    ArrayList<String> dOWHours = new ArrayList<>();
    //default day of the week label names
    String[] dOWLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    //variables to hold TextViews for each day of the week in activity log table
    TextView elSu_tv, elMo_tv, elTu_tv, elWe_tv, elTh_tv, elFr_tv, elSa_tv, elTot_tv, elProj_tv;
    //variables to hold TextViews for each day of the week label in activity log table
    TextView elSu_tvl, elMo_tvl, elTu_tvl, elWe_tvl, elTh_tvl, elFr_tvl, elSa_tvl, elTot_tvl, elProj_tvl;
    //variable to hold TextView for toolbar header default text
    TextView el_header;
    //default labels text
    String weekString = "Total Hours for the week: ";
    String projString = "Total Hours on  " + "|||||" + ": ";
    String headerString = "Activity Log for week of:";

    //format copied from ClockIn class for referencing DB
    private String companyNameRef = "";
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_log);

        //accessing DB ref of specific employee
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(userKey);
        //creating default entries so list will have 7 elements (should all be 0
        //and then replaced from DB by hours of each day
        //currently has hard coded numbers for example data in app)
        dOWHours.add("0");
        dOWHours.add("5");
        dOWHours.add("6");
        dOWHours.add("7");
        dOWHours.add("7");
        dOWHours.add("5");
        dOWHours.add("0");

        //assigning TextViews for employee hours
        elSu_tv = (TextView) findViewById(R.id.sunday_hours);
        elMo_tv = (TextView) findViewById(R.id.monday_hours);
        elTu_tv = (TextView) findViewById(R.id.tuesday_hours);
        elWe_tv = (TextView) findViewById(R.id.wednesday_hours);
        elTh_tv = (TextView) findViewById(R.id.thursday_hours);
        elFr_tv = (TextView) findViewById(R.id.friday_hours);
        elSa_tv = (TextView) findViewById(R.id.saturday_hours);
        elTot_tv = (TextView) findViewById(R.id.act_week_total);
        elProj_tv = (TextView) findViewById(R.id.act_project_total);

        //assigning TextViews for labels of days of the week
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

        //setting labels upon page start
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

        //for accessing employee's data, specifically hours worked
        //via a snapshot of their db entry.
        //final intent should be that a branch in the DB is made under
        //each employee which will store their hours worked for each day.
        //ie Company->EmployeeID->HoursWorkedThisWeek-> Mon:8,Tue:8,Wed:9, etc.
        //these numbers from each day could then fill the table where needed
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timeWorked = 0;
                //check to see if they have any time worked
                if(snapshot.child("timeWorked").getValue() !=null) {
                    timeWorked = ((long) snapshot.child("timeWorked").getValue());
                    //convert time worked in milliseconds to hours
                    String answer = "" + TimeUnit.MILLISECONDS.toHours(timeWorked);
                    //temporarily just putting in first slot.
                    //ideally would loop through and set each
                    //day of the week with loop
                    dOWHours.set(0,answer);
                    System.out.println("sunday: " + dOWHours.get(0));

                    //once logged hours extracted from DB
                    //set TextViews to correct days hours worked
                    elSu_tv.setText(dOWHours.get(0));
                    elMo_tv.setText(dOWHours.get(1));
                    elTu_tv.setText(dOWHours.get(2));
                    elWe_tv.setText(dOWHours.get(3));
                    elTh_tv.setText(dOWHours.get(4));
                    elFr_tv.setText(dOWHours.get(5));
                    elSa_tv.setText(dOWHours.get(6));

                    //equation and formatting for getting the total of the week
                    String totalAnswer = "" + (Integer.parseInt(dOWHours.get(0)) + Integer.parseInt(dOWHours.get(1))
                            + Integer.parseInt(dOWHours.get(2)) + Integer.parseInt(dOWHours.get(3)) + Integer.parseInt(dOWHours.get(4))
                            + Integer.parseInt(dOWHours.get(5)) + Integer.parseInt(dOWHours.get(6)));

                    //setting TextView of total hours worked for the week
                    elTot_tv.setText(totalAnswer);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}