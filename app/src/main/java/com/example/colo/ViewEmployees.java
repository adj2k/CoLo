package com.example.colo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.Announcements.AnnouncementAdapter;
import com.example.colo.Announcements.AnnouncementHelperClass;
import com.example.colo.Announcements.AnnouncementList;
import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.example.colo.Projects.ManagerProjectListEmployeeData;
import com.example.colo.ViewEmployeesFolder.EmployeeList;
import com.example.colo.ViewEmployeesFolder.ViewEmployeeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ViewEmployees extends AppCompatActivity {


    UserHelperClass userHelperClass;
    ArrayList<String> employees;
    RecyclerView recyclerView;
    ViewEmployeeAdapter myAdapter;
    ArrayList<EmployeeList> list;
    private String companyNameRef = "";
    LinearLayout CreateEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        // Sets up the RecyclerView
        recyclerView = findViewById(R.id.employee_list2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Sets up the list, announcements list (not currently used), and adapter
        list = new ArrayList<>();
        employees = new ArrayList<>();
        myAdapter = new ViewEmployeeAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        CreateEmployee = findViewById(R.id.layoutCreateEmployee);

        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef);

        // Listens for data change in database and updates new entries to the list

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    /*
                    if(dataSnapshot.getKey().equals("role")) {
                        String checkRole = dataSnapshot.getValue().toString();
                        Log.i("Role: ", checkRole);

                        if (checkRole.equals("Employee")) {
                            //String name = dataSnapshot.child("name").getValue(String.class);
                            //String ID = dataSnapshot.child("EmployeeID").getValue(String.class);
                            //String email =  dataSnapshot.child("email").getValue(String.class);
                            EmployeeList user = dataSnapshot.getValue(EmployeeList.class);
                            list.add(user);

                        }
                    } */
                    // used to make sure PROJECTS and maybe ANNOUNCEMENTS are not posted to list
                    if(!dataSnapshot.getKey().equals("Projects") ) {
                        if(!dataSnapshot.getKey().equals("Announcements")) {
                            EmployeeList user = dataSnapshot.getValue(EmployeeList.class);
                            list.add(user);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CreateEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // CHANGE TO ACTIVITY LOG CLASS
                startActivity(new Intent(ViewEmployees.this, CreateAccount.class));
            }
        });



    }



}
