package com.example.colo.Projects;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeProjects extends AppCompatActivity {


    // set up recycler
    RecyclerView recyclerView;
    ManagerProjectAdapter myAdapter;
    ArrayList<ProjectData> list;

    // get company name form global
    private String companyName = "";
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_projects);

        companyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();

        // Set up recycler before
        recyclerView = findViewById(R.id.projects_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new ManagerProjectAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        // goes through and finds the projects that are assigned to the logged in user.
        // then adds these projects to the employee list
        ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/Projects");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot deeperSnapshot : dataSnapshot.child("pEmployees").getChildren()) {
                        if (deeperSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            ProjectData data = dataSnapshot.getValue(ProjectData.class);
                            list.add(data);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // TODO: Check for current working project and display

    }

    // TODO: Make clicker to select projects currently worked on AND complete after finished
}