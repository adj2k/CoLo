package com.example.colo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.colo.Announcements.CreateAnnouncement;
import com.example.colo.Announcements.ManagerCreateAnnouncement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{

    TextView conditionTextView;
    Button ButtonSunny;
    Button ButtonFoggy;
    Button ButtonCreateAccount;
    Button ToLogin;
    Button ButtonCreateAnnouncement;
    Button ButtonAnnouncements;
    String companyNameRef;
    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = RootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoLo);
        setContentView(R.layout.activity_main);

        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();


        conditionTextView = (TextView) findViewById(R.id.textViewCondition);
        ButtonSunny = (Button) findViewById(R.id.buttonSunny);
        ButtonFoggy = (Button) findViewById(R.id.buttonFoggy);
        ButtonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        ToLogin = (Button) findViewById(R.id.btnToLogin);
        ButtonCreateAnnouncement = (Button) findViewById(R.id.buttonCreateAnnouncement);
        ButtonAnnouncements = (Button) findViewById(R.id.buttonAnnouncements);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        conditionRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String text = dataSnapshot.getValue(String.class);
                conditionTextView.setText(text);
                //hello
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        ButtonFoggy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                conditionRef.setValue("Clocked Out");
            }
        });

        ButtonCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent toCreateAccount = new Intent(MainActivity.this, CreateEmployee.class);
                startActivity(toCreateAccount);
            }
        });

        ButtonAnnouncements.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent toCreateAccount = new Intent(MainActivity.this, CreateAnnouncement.class);
                startActivity(toCreateAccount);
            }
        });


        ButtonCreateAnnouncement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent toCreateAccount = new Intent(MainActivity.this, ManagerCreateAnnouncement.class);
                startActivity(toCreateAccount);
            }
        });

        ToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent toLogin = new Intent(MainActivity.this, LogIn.class);
                startActivity(toLogin);
            }
        });

        ButtonSunny.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {
                String UID = "sxTQNUVXjCNftdNt9OGuuvS2FJC2";

            }
        });
    }


}