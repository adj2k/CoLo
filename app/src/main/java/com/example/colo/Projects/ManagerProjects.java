package com.example.colo.Projects;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colo.R;

public class ManagerProjects extends AppCompatActivity {

    private Button new_project_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_projects);












        new_project_btn = (Button) findViewById(R.id.new_project_btn);
        new_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(ManagerProjects.this, CreateProject.class)); }
        });

    }


}