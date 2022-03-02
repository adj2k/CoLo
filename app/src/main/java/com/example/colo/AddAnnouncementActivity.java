package com.example.colo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class AddAnnouncementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        MaterialButton postBtn = findViewById(R.id.postBtn);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();

                //Firebase Stuff Here
                // Set Title, Description, Time
                Toast.makeText(getApplicationContext(), "Announcement Posted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
