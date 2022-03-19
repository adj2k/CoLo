package com.example.colo.Projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.example.colo.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateProject extends AppCompatActivity implements ManagerProjectListEmployeeAdapter.OnNoteListener {

    EditText name, description;
    private Button create_project_btn;
    private String companyName = "";
    boolean flag = true;
    ProjectHelperClass projectHelperClass;

    RecyclerView recyclerView;
    ManagerProjectListEmployeeAdapter myAdapter;
    ArrayList<ManagerProjectListEmployeeData> list;
    ArrayList<String> employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        // get company name
        companyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyName);

        recyclerView = findViewById(R.id.employee_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        name = (EditText) findViewById(R.id.get_project_name);
        description = (EditText) findViewById(R.id.get_project_desc);

        list = new ArrayList<>();
        employees = new ArrayList<>();
        myAdapter = new ManagerProjectListEmployeeAdapter(this,list, this);
        recyclerView.setAdapter(myAdapter);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ManagerProjectListEmployeeData user = dataSnapshot.getValue(ManagerProjectListEmployeeData.class);

                    list.add(user);
                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        create_project_btn = (Button) findViewById(R.id.create_project_btn);
        create_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addProject();}
        });
    }

    // submit new project to database !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void addProject() {
        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pManagerUI = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyName).child("Projects");
        projectHelperClass = new ProjectHelperClass(pName, pDescription, pManagerUI, employees);
        if(validateDec() && validateName() && validateEmployees()){
            ref.child(name.getText().toString()).setValue(projectHelperClass);

        }


    }

    private boolean validateEmployees() {
        if(employees.isEmpty()) {
            create_project_btn.setError("Must select at lease ONE employee");
            return false;
        } else
        {
            create_project_btn.setError(null);
            return true;
        }
    }


    // check if fields are blank OR name dup
    private boolean validateName() {
        String val = name.getText().toString();
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
                            flag = false;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return flag;

        }
    }

    private boolean validateDec () {
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
    public void onNoteClick(int position) {
        String empName = list.get(position).getName();
        boolean clickFlag = true;
        for(String i : employees) {
            if(empName.equals(i)) {
                Toast.makeText(CreateProject.this, "Employee already Chosen", Toast.LENGTH_LONG).show();
                clickFlag = false;
            }
        }
        // add employee to list if they are not already on the list
        if(clickFlag) {
            employees.add(empName);
        }

    }
}