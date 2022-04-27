package com.example.colo.Projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateProject extends AppCompatActivity implements QuantityListener {

    EditText name, description;
    private LinearLayout create_project_btn;
    private String companyName = "";
    ProjectHelperClass projectHelperClass;

    RecyclerView recyclerView;
    ProjectEmployeeAdapter myAdapter;
    ArrayList<ManagerProjectListEmployeeData> list;
    ArrayList<String> employees, EmployeeUIDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        // get company name
        companyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyName);

        recyclerView = findViewById(R.id.employee_list);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        name = (EditText) findViewById(R.id.get_project_name);
        description = (EditText) findViewById(R.id.get_project_desc);

        list = new ArrayList<>();
        employees = new ArrayList<>();
        EmployeeUIDS = new ArrayList<>();
        //myAdapter = new ManagerProjectListEmployeeAdapter(this,list, this);
        //recyclerView.setAdapter(myAdapter);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // used to make sure PROJECTS and maybe ANNOUNCEMENTS are not posted to list
                    if(!dataSnapshot.getKey().equals("Projects") ) {
                        if(!dataSnapshot.getKey().equals("Announcements")) {
                            if(dataSnapshot.child("role").getValue().equals("Employee")) {
                                ManagerProjectListEmployeeData user = dataSnapshot.getValue(ManagerProjectListEmployeeData.class);
                                list.add(user);
                                EmployeeUIDS.add(dataSnapshot.getKey());
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setRecyclerView();

        create_project_btn = findViewById(R.id.createProject);
        create_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addProject();}
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ProjectEmployeeAdapter(this, list, EmployeeUIDS, (QuantityListener) this);
        recyclerView.setAdapter(myAdapter);


    }

    // submit new project to database !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void addProject() {
        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pManagerUI = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("before Valid check");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyName).child("Projects");
        projectHelperClass = new ProjectHelperClass(pName, pDescription, pManagerUI, employees);
        if(validateDesc() && validateName() && validateEmployees()){
            ref.child(name.getText().toString()).setValue(projectHelperClass);
            System.out.println("Pass Valid Check");
            startActivity(new Intent(CreateProject.this, ManagerProjects.class));
            finish();
        }


    }

    private boolean validateEmployees() {
        if(employees.isEmpty()) {
            Toast.makeText(CreateProject.this, "At Lease ONE employee must be chosen", Toast.LENGTH_LONG).show();
            return false;
        } else
        {
            return true;
        }
    }


    // check if fields are blank OR name dup
    private boolean validateName() {
        String val = name.getText().toString();
        final boolean[] flag = {true};
        // get ref
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyName).child("Projects");
        if(val.isEmpty()) {
            name.setError("Field can not be empty");
            return false;
        } else {
            // test for dup names
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    for(DataSnapshot snapshot : datasnapshot.getChildren()){
                        if(snapshot.getKey().equals(val)) {
                            name.setError("Name is already taken");
                            recreate();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            return flag[0];

        }
    }

    private boolean validateDesc () {
        String val = description.getText().toString();
        if (val.isEmpty())
        {
            description.setError("Field can not be empty");
            return false;
        } else
        {
            description.setError(null);
            return true;
        }
    }

    @Override
    public void onQuantityChange(ArrayList<String> list) {
        employees = list;
        //Toast.makeText(CreateProject.this, employees.toString(), Toast.LENGTH_LONG).show();
    }
}