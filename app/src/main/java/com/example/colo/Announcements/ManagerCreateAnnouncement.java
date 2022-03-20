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

import com.example.colo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerCreateAnnouncement extends AppCompatActivity implements AnnouncementAdapter.OnNoteListener {
    EditText title, description;
    private Button create_announcement_btn;
    boolean flag = true;

    AnnouncementHelperClass announcementHelperClass;
    ArrayList<String> announcements;

    RecyclerView recyclerView;
    AnnouncementAdapter myAdapter;
    ArrayList<AnnouncementList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_manager_page);

        // get company name
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Announcements");

        recyclerView = findViewById(R.id.announcement_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        announcements = new ArrayList<>();
        myAdapter = new AnnouncementAdapter(this,list, this);
        recyclerView.setAdapter(myAdapter);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AnnouncementList announcement = dataSnapshot.getValue(AnnouncementList.class);

                    list.add(announcement);
                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        create_announcement_btn = (Button) findViewById(R.id.create_announcement_btn);
        create_announcement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addAnnouncement();}
        });

    }

    // submit new announcement to database

    private void addAnnouncement() {
        String aTitle = title.getText().toString();
        String aDescription = description.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Announcements");
        announcementHelperClass = new AnnouncementHelperClass(aTitle, aDescription);
        ref.child(title.getText().toString()).setValue(announcementHelperClass);
        ref.child(description.getText().toString()).setValue(announcementHelperClass);

    }

    @Override
    public void onNoteClick(int position) {
        String announcementName = list.get(position).getTitle();
        boolean clickFlag = true;
        for (String i : announcements) {
            if(announcementName.equals(i)) {
                Toast.makeText(ManagerCreateAnnouncement.this, "Announcement Chosen", Toast.LENGTH_LONG).show();
                clickFlag = false;
            }
        }

        if(clickFlag) {
            announcements.add(announcementName);
        }
    }

}
