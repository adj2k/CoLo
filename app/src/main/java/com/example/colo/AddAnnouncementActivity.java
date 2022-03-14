package com.example.colo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAnnouncementActivity extends AppCompatActivity {

    EditText titleInput;
    EditText descriptionInput;
    MaterialButton postBtn;
    DatabaseReference AnnouncementDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        postBtn = findViewById(R.id.postBtn);

        AnnouncementDBRef = FirebaseDatabase.getInstance().getReference().child("Announcements");

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Firebase Stuff Here
                insertAnnouncement();

                Toast.makeText(getApplicationContext(), "Announcement Posted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void insertAnnouncement(){
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String createdTime = String.valueOf(System.currentTimeMillis());

        CurrentAnnouncement Announcement = new CurrentAnnouncement(title, description, createdTime);

        AnnouncementDBRef.push().setValue(Announcement);
    }
}
