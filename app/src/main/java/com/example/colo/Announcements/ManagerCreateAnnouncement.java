package com.example.colo.Announcements;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.GlobalCompanyName;
import com.example.colo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ManagerCreateAnnouncement extends AppCompatActivity {

    // An exact copy of CreateAnnouncement except it has the ability to add to database
    // using the create announcement button only a manager would have
    // See CreateAnnouncement.class for previous comments

    EditText title, description;
    private Button create_announcement_btn;
    boolean flag = true;

    AnnouncementHelperClass announcementHelperClass;
    ArrayList<String> announcements;

    RecyclerView recyclerView;
    AnnouncementAdapter myAdapter;
    ArrayList<AnnouncementList> list;

    private String companyNameRef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_manager_page);

        // get company name
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Announcements");

        // Sets up Recycler View
        recyclerView = findViewById(R.id.announcement_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sets up list and adapter for recycler view to display announcements
        list = new ArrayList<>();
        announcements = new ArrayList<>();
        myAdapter = new AnnouncementAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        // Defines title/description as corresponding XML sections
        title = findViewById(R.id.get_announcement_title);
        description = findViewById(R.id.get_announcement_desc);

        // Gets company reference and
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child("Announcements");

        // This function creates a list of the announcements
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AnnouncementList announcement = dataSnapshot.getValue(AnnouncementList.class);

                    list.add(announcement);
                }
                // Reverses List to Put Announcements On Top
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Setups up create announcement button
        create_announcement_btn = (Button) findViewById(R.id.create_announcement_btn);
        create_announcement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add announcement and makes toast notification, then finishes the page
                addAnnouncement();
                Toast.makeText(getApplicationContext(), "Announcement Posted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    // This adds the announcement to the database by setting up local variables, referencing the database
    // and then creating an "announcement" via the helper class, which is then pushed to the database
    private void addAnnouncement() {
        String aTitle = title.getText().toString();
        String aDescription = description.getText().toString();

        // References the database, then pushes it to the database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child("Announcements");
        announcementHelperClass = new AnnouncementHelperClass(aTitle, aDescription);
        ref.push().setValue(announcementHelperClass);

    }

}