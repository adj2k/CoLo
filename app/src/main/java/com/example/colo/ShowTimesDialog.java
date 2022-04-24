package com.example.colo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.colo.employeePages.ClockIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ShowTimesDialog extends AppCompatDialogFragment {

    String UID, companyName;
    boolean clockStatus;

    // Table Stuff
    TextView eName, D1, D2, D3, D4, D5, D6, D7;
    TextView currentProject, Total, H1, H2, H3, H4, H5, H6, H7;
    ArrayList<String> Hours;
    String p = "MM-dd";
    String clockOption = "";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(p);

    // Clock in Stuff
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");



    public ShowTimesDialog(String UID, String companyName, boolean clockStatus) {
        this.UID = UID;
        this.companyName = companyName;
        this.clockStatus = clockStatus;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // see if user is clocked in
        if(clockStatus) {
            clockOption = "Clock Out";
        } else
            clockOption = "Clock In";

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/" + UID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.employee_hours_popup, null);

        builder.setView(view).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.setView(view).setNeutralButton(clockOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(clockStatus) {
                            // clock out stuff
                            LocalDateTime clock = LocalDateTime.now();
                            String end = clock.format(formatter);

                            String p = "MM-dd";
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(p);

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
                                            if(snapshot.child("timeWorked").child(LocalDate.now().format(dtf)).getValue() != null) {
                                                long currentDaysWork = (long) snapshot.child("timeWorked").child(LocalDate.now().format(dtf)).getValue();
                                                ref.child("timeWorked").child(LocalDate.now().format(dtf)).setValue(currentDaysWork + timeInMillis);
                                            } else {
                                                ref.child("timeWorked").child(LocalDate.now().format(dtf)).setValue(timeInMillis);
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
                            Toast display = Toast.makeText(getActivity(), "Clocked Out", Toast.LENGTH_LONG);
                            display.show();

                        } else {
                            // clock in stuff
                            LocalDateTime clock = LocalDateTime.now();
                            String currentTime = clock.format(formatter);
                            HashMap map = new HashMap();
                            map.put("clockInTime", currentTime);
                            ref.updateChildren(map);
                            Toast display = Toast.makeText(getActivity(), "Clocked In", Toast.LENGTH_LONG);
                            display.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/" + UID);
        System.out.println("Companies/" + companyName + "/" + UID);
        // Set Up table for selected employee's times
        D1=(TextView) getDialog().findViewById(R.id.D1);
        D2=(TextView) getDialog().findViewById(R.id.D2);
        D3=(TextView) getDialog().findViewById(R.id.D3);
        D4=(TextView) getDialog().findViewById(R.id.D4);
        D5=(TextView) getDialog().findViewById(R.id.D5);
        D6=(TextView) getDialog().findViewById(R.id.D6);
        D7=(TextView) getDialog().findViewById(R.id.D7);

        D7.setText(LocalDate.now().format(dtf));
        D6.setText(LocalDate.now().minusDays(1).format(dtf));
        D5.setText(LocalDate.now().minusDays(2).format(dtf));
        D4.setText(LocalDate.now().minusDays(3).format(dtf));
        D3.setText(LocalDate.now().minusDays(4).format(dtf));
        D2.setText(LocalDate.now().minusDays(5).format(dtf));
        D1.setText(LocalDate.now().minusDays(6).format(dtf));

        H1=(TextView) getDialog().findViewById(R.id.H1);
        H2=(TextView) getDialog().findViewById(R.id.H2);
        H3=(TextView) getDialog().findViewById(R.id.H3);
        H4=(TextView) getDialog().findViewById(R.id.H4);
        H5=(TextView) getDialog().findViewById(R.id.H5);
        H6=(TextView) getDialog().findViewById(R.id.H6);
        H7=(TextView) getDialog().findViewById(R.id.H7);
        Hours = new ArrayList<String>(Collections.nCopies(7,"00:00"));

        Total=(TextView) getDialog().findViewById(R.id.Total);
        eName=(TextView) getDialog().findViewById(R.id.eName);
        currentProject=(TextView) getDialog().findViewById(R.id.currentProject);


        // access Database to get name, currentProject, and working times
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eName.setText(snapshot.child("name").getValue(String.class));
                if(snapshot.child("currentProject").exists())
                    currentProject.setText(snapshot.child("currentProject").getValue(String.class));

                for(int i = 0; i < 7; i++)
                    if(snapshot.child("timeWorked").child(LocalDate.now().minusDays(i).format(dtf)).exists()) {
                        long daysTime = (long) snapshot.child("timeWorked").child(LocalDate.now().minusDays(i).format(dtf)).getValue();
                        String daysTimeFormatted = TimeUnit.MILLISECONDS.toHours(daysTime) + ":" + String.format("%02d",TimeUnit.MILLISECONDS.toMinutes(daysTime)%60);
                        Hours.set(i,daysTimeFormatted);
                        System.out.println(Hours.get(i));
                    }
                String totalTimeFormatted = "00:00";
                if((snapshot.child("timeWorked").child("Total").getValue() != null))
                    totalTimeFormatted = TimeUnit.MILLISECONDS.toHours((long) snapshot.child("timeWorked").child("Total").getValue()) + ":" + String.format("%02d",TimeUnit.MILLISECONDS.toHours((long) snapshot.child("timeWorked").child("Total").getValue()));

                H1.setText(Hours.get(6));
                H2.setText(Hours.get(5));
                H3.setText(Hours.get(4));
                H4.setText(Hours.get(3));
                H5.setText(Hours.get(2));
                H6.setText(Hours.get(1));
                H7.setText(Hours.get(0));
                Total.setText(totalTimeFormatted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
