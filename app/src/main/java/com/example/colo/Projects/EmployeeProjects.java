package com.example.colo.Projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.GlobalCompanyName;
import com.example.colo.Projects.CreateProject;
import com.example.colo.Projects.ManagerProjectAdapter;
import com.example.colo.Projects.ProjectData;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeProjects extends AppCompatActivity implements ManagerProjectAdapter.OnNoteListener{

    // set up current projects
    TextView currentpName, currentpDescription;
    ImageView complete_btn;
    // set up recycler
    RecyclerView recyclerView;
    ManagerProjectAdapter myAdapter;
    ArrayList<ProjectData> list;
    ProjectData currentProject;
    // get company name form global
    private String companyName = "";
    DatabaseReference projRef, userRef;
    String validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_projects);
        currentpName = findViewById(R.id.currentpName);
        currentpDescription = findViewById(R.id.currentpDescription);
        companyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        validator = null;
        // Set up recycler before
        recyclerView = findViewById(R.id.projects_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        myAdapter = new ManagerProjectAdapter(this, list, this);
        recyclerView.setAdapter(myAdapter);

        // goes through and finds the projects that are assigned to the logged in user.
        // then adds these projects to the employee list
        projRef = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/Projects");

        // find current project
        userRef = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/" + FirebaseAuth.getInstance().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("currentProject").exists()) {
                    validator = snapshot.child("currentProject").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // load viewer and find current proj
        projRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // loop through projects
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println("pNames: " + dataSnapshot.child("pName").getValue());
                    System.out.println("Current Proj: " + validator);

                    // test if project is "currentWorking" project
                    if(dataSnapshot.child("pName").getValue().equals(validator)) {
                        currentProject = dataSnapshot.getValue(ProjectData.class);
                    } else {
                        // loop through employees assigned to find if project is assigned to
                        for (DataSnapshot deeperSnapshot : dataSnapshot.child("pEmployees").getChildren()) {
                            if (deeperSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                ProjectData data = dataSnapshot.getValue(ProjectData.class);
                                list.add(data);
                            }
                        }
                    }
                }
                System.out.println("look at this: " + validator);
                if(validator == null) {
                    String noCurrent = "No Project Selected";
                    currentpName.setText(noCurrent);
                } else {
                    currentpName.setText(currentProject.getpName());
                    currentpDescription.setText(currentProject.getpDescription());
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        complete_btn = (ImageView) findViewById(R.id.complete_btn);
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeProject();
            }
        });


    }

    // TODO: currently projects are just deleted can move them to a completed directory at some point
    private void completeProject() {
        projRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // find name of project being COMPLETED
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.child("pName").getValue().equals(validator)){
                        dataSnapshot.getRef().setValue(null);
                        validator = null;
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.child("currentProject").getRef().setValue(null);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        // refreshes page
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onNoteClick(int position) {
        // Pressing on a project will update the Database with the new
        // project that is being worked on
        System.out.println(list.get(position).getpName());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("currentProject").getValue() == list.get(position).getpName()){
                    Toast.makeText(EmployeeProjects.this, "This project is currently being worked on", Toast.LENGTH_LONG);
                } else {
                    userRef.child("currentProject").setValue(list.get(position).getpName());
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}