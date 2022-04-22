package com.example.colo.Announcements;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

public class CreateAnnouncement extends AppCompatActivity {

    // Variable Setup
    EditText title, description;
    private Button create_announcement_btn;
    boolean flag = true;

    // Defines the announcementHelperClass and announcements list, which is not currently used
    AnnouncementHelperClass announcementHelperClass;
    ArrayList<String> announcements;
    RecyclerView recyclerView;
    AnnouncementAdapter myAdapter;
    ArrayList<AnnouncementList> list;
    private String companyNameRef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_page);

        // Sets the database reference to the "Announcements"
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Announcements");

        // Sets up the RecyclerView
        recyclerView = findViewById(R.id.announcement_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Sets up the list, announcements list (not currently used), and adapter
        list = new ArrayList<>();
        announcements = new ArrayList<>();
        myAdapter = new AnnouncementAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        // Grabs company name and reference for the company's announcements
        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/"+companyNameRef).child("Announcements");

        // Listens for data change in database and updates new entries to the list
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AnnouncementList announcement = dataSnapshot.getValue(AnnouncementList.class);

                    list.add(announcement);
                }
                // Reverses list to put latest announcement at the top
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
