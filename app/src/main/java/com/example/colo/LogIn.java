package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity
{
    //Firebase
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public EditText UserName, Password;
    public Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("employees");

        UserName = (EditText) findViewById(R.id.etUserName);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn(UserName.getText().toString(), Password.getText().toString());
            }
        });
    }
    private void signIn(String username, String password)
    {
        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(username).exists())
                {
                    if(!username.isEmpty())
                    {
                        UserHelperClass login = dataSnapshot.child(username).getValue(UserHelperClass.class);
                        if(login.getPassword().equals(password))
                        {
                            Intent toHomeScreen = new Intent (LogIn.this, MainActivity.class);
                            startActivity(toHomeScreen);
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Username or Password is incorrect!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Username or Password is incorrect!", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}