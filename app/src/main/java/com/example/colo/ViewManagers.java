package com.example.colo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.ViewEmployeesFolder.EmployeeList;
import com.example.colo.ViewEmployeesFolder.ViewEmployeeAdapter;
import com.example.colo.employeePages.EmployeeHub;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewManagers extends AppCompatActivity {


    UserHelperClass userHelperClass;
    ArrayList<String> employees;
    RecyclerView recyclerView;
    ViewEmployeeAdapter myAdapter;
    ArrayList<EmployeeList> list;
    private String companyNameRef = "";
    LinearLayout CreateEmployee;
    Dialog dialog;
    ImageView deleteEmployee;
    Button yes, cancel;
    boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_managers);

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

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // used to make sure PROJECTS and maybe ANNOUNCEMENTS are not posted to list
                    if(!dataSnapshot.getKey().equals("Projects") ) {
                        if(!dataSnapshot.getKey().equals("Announcements")) {
                            if(dataSnapshot.child("role").getValue().equals("Manager")) {
                                EmployeeList user = dataSnapshot.getValue(EmployeeList.class);
                                user.setFirebaseId(dataSnapshot.getKey());
                                list.add(user);
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
                // update list after editing of info
                /*
                if (!firstRun) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                } else {
                    firstRun = false;
                }*/
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
                startActivity(new Intent(ViewManagers.this, CreateManager.class));
            }
        });

        /*
        dialog = new Dialog(ViewEmployees.this);
        dialog.setContentView(R.layout.pop_delete);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.delete_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yes = dialog.findViewById(R.id.btn_yes);
        cancel = dialog.findViewById(R.id.btn_cancel);

        deleteEmployee = findViewById(R.id.employeeDeleteButton);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEmployees.this, "User Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEmployees.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        deleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        */


    }



}
