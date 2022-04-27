package com.example.colo.Projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerProjects extends AppCompatActivity implements ManagerProjectAdapter.OnNoteListener {

    private LinearLayout new_project_btn;

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
        setContentView(R.layout.activity_manager_projects);

        companyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();

        // Set up recycler before
        recyclerView = findViewById(R.id.projects_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new ManagerProjectAdapter(this, list, this);
        recyclerView.setAdapter(myAdapter);

        ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyName + "/Projects");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ProjectData data = dataSnapshot.getValue(ProjectData.class);
                    System.out.println(data.toString());
                    list.add(data);


                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        new_project_btn =  findViewById(R.id.moveToCreateProject);
        new_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerProjects.this, CreateProject.class));
                finish();
            }
        });

    }


    @Override
    public void onNoteClick(int position) {

    }
}