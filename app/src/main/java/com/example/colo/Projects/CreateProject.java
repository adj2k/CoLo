package com.example.colo.Projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateProject extends AppCompatActivity {

    EditText name, description;
    private Button create_projcet_btn;
    private String companyName = "";
    boolean flag = true;


    RecyclerView recyclerView;
    ManagerProjectListEmployeeAdapter myAdapter;
    ArrayList<ManagerProjectListEmployeeData> list;

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

        list = new ArrayList<>();
        myAdapter = new ManagerProjectListEmployeeAdapter(this,list);
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


        create_projcet_btn = (Button) findViewById(R.id.create_projcet_btn);
        create_projcet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addProject();}
        });
    }

    // submit new project to database
    private void addProject() {

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
}