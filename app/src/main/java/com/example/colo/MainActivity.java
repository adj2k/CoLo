package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.Condition;

public class MainActivity extends AppCompatActivity
{

    TextView conditionTextView;
    Button ButtonSunny;
    Button ButtonFoggy;
    Button ButtonCreateAccount;
    Button ToLogin;

    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = RootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conditionTextView = (TextView) findViewById(R.id.textViewCondition);
        ButtonSunny = (Button) findViewById(R.id.buttonSunny);
        ButtonFoggy = (Button) findViewById(R.id.buttonFoggy);
        ButtonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        ToLogin = (Button) findViewById(R.id.btnToLogin);
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

        ButtonSunny.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                conditionRef.setValue("Clocked In");
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
                Intent toCreateAccount = new Intent(MainActivity.this, CreateAccount.class);
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

    }
}