package com.example.colo;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;

public class Announcements extends AppCompatActivity {
    String announcementTitle;
    String description;
    long createdTime;
    DatabaseReference database;
    RecyclerView rView;
    MyAdapter myAdapter;
    ArrayList<CurrentAnnouncement> list;
    MaterialButton addAnnouncementBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements);


        rView = findViewById(R.id.announcementList);
        database = FirebaseDatabase.getInstance().getReference("Announcements");
        rView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(linearLayoutManager);

        myAdapter = new MyAdapter(this, list);
        rView.setAdapter(myAdapter);


        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    list.clear();
                    CurrentAnnouncement announcement = data.getValue(CurrentAnnouncement.class);
                    list.add(announcement);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    /*
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    list.clear();
                    CurrentAnnouncement announcement = data.getValue(CurrentAnnouncement.class);
                    list.add(announcement);
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });*/


        //Add Announcement Button
        addAnnouncementBtn = findViewById(R.id.addNewAnnouncement_btn);
        addAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Announcements.this, AddAnnouncementActivity.class));
            }
        });
    }
}