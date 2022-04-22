package com.example.colo.employeePages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ClockIn extends AppCompatActivity {

    // items
    private Button clock_in_btn;
    private Button clock_out_btn;
    private static final String CLOCKIN = "Clock In";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    private String companyNameRef = "";

    DateTimeFormatter formatterdisplay = DateTimeFormatter.ofPattern("hh:mm a");

    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String first_name;
    TextView hello_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        hello_name=findViewById(R.id.e_username_c);
        //display date on top of the page
        EditText viewDate=findViewById(R.id.date);
        //get the date and day
        Calendar calendar= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        viewDate.setText(currentDate);

        // gets user's company name and user ref
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(userKey);

        //Display the first name after Hello
        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first_name = snapshot.getValue(String.class);
                hello_name.setText(first_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // see which button needs to be greyed out and deactivated.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("clockInTime").exists()) {
                    clock_in_btn.setEnabled(false);
                    clock_in_btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } else {
                    clock_out_btn.setEnabled(false);
                    clock_out_btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set up Firebase instance

        // listen for clock in
        clock_in_btn = (Button) findViewById(R.id.clock_in_btn);
        clock_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClockInTime();
            }
        });

        // listen for clock out
        clock_out_btn = (Button) findViewById(R.id.clock_out_btn);
        clock_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockOutTime();
            }
        });


    }

    private void addClockInTime() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(userKey);
        // get local time in hh:mm:ss format
        LocalDateTime clock = LocalDateTime.now();
        String currentTime = clock.format(formatter);
        HashMap map = new HashMap();
        map.put("clockInTime", currentTime);
        ref.updateChildren(map);
        Toast display = Toast.makeText(ClockIn.this, "Clocked In", Toast.LENGTH_LONG);
        display.show();
        clock_in_btn.setEnabled(false);
        clock_out_btn.setEnabled(true);
        System.out.println(companyNameRef);
        finish();
    }

    private void clockOutTime() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child(userKey);
        // get local time in hh:mm:ss format
        LocalDateTime clock = LocalDateTime.now();
        String end = clock.format(formatter);

        HashMap map = new HashMap();
        map.put("clockOutTime", end);
        ref.updateChildren(map);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String start = snapshot.child("clockInTime").getValue().toString();

                LocalDateTime clockIn = LocalDateTime.parse(start, formatter);
                System.out.println("Before Second Parse");

                LocalDateTime clockOut = LocalDateTime.parse(end, formatter);

                Duration shiftLength = Duration.between(clockIn, clockOut);
                long timeInMillis = shiftLength.toMillis();

                //how to get time from millis!!!!!!!!!!!!!!!!
                System.out.println("Shift was " + timeInMillis + "Milliseconds");
                System.out.print("Shift was== " + TimeUnit.MILLISECONDS.toHours(timeInMillis));
                System.out.print(":" + TimeUnit.MILLISECONDS.toMinutes(timeInMillis));
                System.out.println(":" + TimeUnit.MILLISECONDS.toSeconds(timeInMillis)%60);

                // add this amount to total in employee database.
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long timeWorked = 0;
                        if(snapshot.child("timeWorked").child("Total").getValue() !=null) {
                            timeWorked = ((long) snapshot.child("timeWorked").child("Total").getValue());
                        }

                        long newTotal = timeInMillis + timeWorked;
                        System.out.println("old time : " + timeWorked + "   New Time worked was                                                 " + timeInMillis);
                        // Add to total in DB and create new entry for work day
                        ref.child("timeWorked").child("Total").setValue(newTotal);

                        // test if date is already used and at to daily total if so
                        if(snapshot.child("timeWorked").child(LocalDate.now().toString()).getValue() != null) {
                            long currentDaysWork = (long) snapshot.child("timeWorked").child(LocalDate.now().toString()).getValue();
                            ref.child("timeWorked").child(LocalDate.now().toString()).setValue(currentDaysWork + timeInMillis);
                        } else {
                            ref.child("timeWorked").child(LocalDate.now().toString()).setValue(timeInMillis);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // clear out old clock in time

        ref.child("clockInTime").setValue(null);

        // add to weeks total hours in Firebase
        Toast display = Toast.makeText(ClockIn.this, "Clocked Out", Toast.LENGTH_LONG);
        display.show();

        clock_out_btn.setEnabled(false);
        clock_in_btn.setEnabled(true);
        System.out.println(companyNameRef);
        finish();
    }


}