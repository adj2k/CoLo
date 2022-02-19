package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.colo.employeePages.EmployeeHub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LogIn extends AppCompatActivity
{
    EditText Email, Password;
    Button Login;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(EMPLOYEE);
        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logIn();
            }
        });
    }

    private void logIn()
    {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(email.isEmpty())
        {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            Password.setError("Password is required");
            Password.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {

                    DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Employee")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                if(snapshot.getKey().equals("role")) {
                                    String checkRole = snapshot.getValue().toString();
                                    Log.i("Role: ", checkRole);

                                    if(checkRole.equals("Manager")){
                                        startActivity(new Intent(LogIn.this, ManagerHub.class));
                                    } else if (checkRole.equals("Employee")){
                                        startActivity(new Intent(LogIn.this, EmployeeHub.class));
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(LogIn.this, "Failed to login. Please check credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
