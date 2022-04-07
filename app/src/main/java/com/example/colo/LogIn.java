package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;


public class LogIn extends AppCompatActivity
{
    EditText Email, Password, Company;
    TextView Login;
    TextView AddCompany;

    //Firebase variables
    private String m_Text = "";
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";
    AutoCompleteTextView autocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (TextView) findViewById(R.id.btnToLogin);
        Company = (EditText) findViewById(R.id.etCompany);
        AddCompany = (TextView) findViewById(R.id.tvAddCompany);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(EMPLOYEE);
        mAuth = FirebaseAuth.getInstance();
        String arr[] = {};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));
        //Auto complete
        //Get company list
        DatabaseReference allCompanyref =  FirebaseDatabase.getInstance().getReference("Companies");
        allCompanyref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    arrayList.add(snapshot.getKey());
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        AutoCompleteTextView autocomplete =  (AutoCompleteTextView) Company;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arrayList);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);





        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logIn();
            }
        });

        AddCompany.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LogIn.this, AddCompany.class));
            }
        });

    }

    //Method that allows the user to login
    private void logIn()
    {
        //Getting the variables from the database and setting them to a string
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String company = Company.getText().toString();

        //if the company field is empty, throw an error and tell the user to enter the correct information
        if(company.isEmpty())
        {
            Company.setError("Company is required");
            Company.requestFocus();
            return;
        }
        //if the email field is empty, throw an error and tell the user to enter the correct information
        if(email.isEmpty())
        {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        //if the password field is empty, throw an error and tell the user to enter the correct information
        if(password.isEmpty())
        {
            Password.setError("Password is required");
            Password.requestFocus();
        }

        //sighInWithEmailAndPassword takes two parameters email and password and checks the firebase database database to see if the username, password belongs to the same UID
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            DatabaseReference referenceCompany =  FirebaseDatabase.getInstance().getReference("Companies").child(company);

                            DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Companies").child(company)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                            referenceCompany.addValueEventListener(new ValueEventListener() {
                                @Override

                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                    for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                        //checks if there is a UID for the email and password combo.
                                        if(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            // Auth Key = Key in Company => proceed to check for role

                                            //Use a reference right at the User ID level, so you can look through all the users and check their keys for easy access to the "role" attribute we use later
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                                    for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                                        String localRole = "";
                                                        //Every attribute in the database has a key, we are checking the key that is equal to role
                                                        //if the snapshot key is equal to role, set the check role to the value of role as a string so it can be used compare the different company roles.
                                                        if(snapshot.getKey().equals("role")) {
                                                            String checkRole = snapshot.getValue().toString();
                                                            localRole = checkRole;
                                                            Log.i("StupidLocalRole",localRole);
                                                            Log.i("Role: ", checkRole);

                                                            //if the role is equal to Admin and their company matches the one in the database, send the user to the admin hub
                                                            if(checkRole.equals("Admin")){
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, AdminHub.class));
                                                            //if the role is equal to Manager and their company matches the one in the database, send the user to the manager hub
                                                            }else if(checkRole.equals("Manager")){
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, ManagerHub.class));
                                                            //if the role is equal to Employee and their company matches the one in the database, send the user to the employee hub
                                                            } else if (checkRole.equals("Employee")){
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, EmployeeHub.class));
                                                                } else{
                                                                Toast.makeText(LogIn.this, "YOU DON'T WORK FOR THIS COMPANY.", Toast.LENGTH_LONG).show();
                                                            }
                                                            }
                                                        }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        else {
                                           //Toast.makeText(LogIn.this, "YOU DON'T WORK FOR THIS COMPANY.", Toast.LENGTH_LONG).show();
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
