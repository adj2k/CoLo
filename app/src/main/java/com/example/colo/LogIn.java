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
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";
    AutoCompleteTextView autocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoLo);
        setContentView(R.layout.activity_log_in);
        //linking the variable from xml to java so the buttons have functionality
        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (TextView) findViewById(R.id.btnToLogin);
        Company = (EditText) findViewById(R.id.etCompany);
        AddCompany = (TextView) findViewById(R.id.tvAddCompany);

        //instance to the authorization for firebase
        mAuth = FirebaseAuth.getInstance();

        //initializing the array and list for company names for the autocomplete
        String arr[] = {};
        ArrayList<String> companyList = new ArrayList<String>(Arrays.asList(arr));
        //Auto complete
        //Get company list
        DatabaseReference allCompanyref = FirebaseDatabase.getInstance().getReference("Companies");
        allCompanyref.addValueEventListener(new ValueEventListener()
        {
            @Override

            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                //traverse through all the companies in firebase and adds them to a list
                for (DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    companyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        //Autocomplete when user begins to type the company name in login
        AutoCompleteTextView autocomplete = (AutoCompleteTextView) Company;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, companyList);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);

        //call login function when user clicks on the login button
        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logIn();
            }
        });

        //navigate to the add company activity when user clicks on the create company button
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
        if (company.isEmpty())
        {
            Company.setError("Company is required");
            Company.requestFocus();
            return;
        }
        //if the email field is empty, throw an error and tell the user to enter the correct information
        if (email.isEmpty())
        {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        //if the password field is empty, throw an error and tell the user to enter the correct information
        if (password.isEmpty())
        {
            Password.setError("Password is required");
            Password.requestFocus();
        }

        //sighInWithEmailAndPassword takes two parameters email and password and checks the firebase database to see if the email and password are valid.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            //get reference for company in database
                            DatabaseReference referenceCompany = FirebaseDatabase.getInstance().getReference("Companies").child(company);
                            //get reference for user in database
                            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Companies").child(company)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //find information in the company child
                            referenceCompany.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override

                                public void onDataChange(@NonNull DataSnapshot datasnapshot)
                                {
                                    //loop through companies in the database

                                    for (DataSnapshot snapshot : datasnapshot.getChildren())
                                    {
                                        System.out.println("THIS IS WHY YOU ARE BACK AT THE HUB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        //if the current user has the same UID as the database key, proceed to check role
                                        if (snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        {

                                            //find information in the user ID child
                                            referenceUser.addListenerForSingleValueEvent(new ValueEventListener()
                                            {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot datasnapshot)
                                                {
                                                    System.out.println("OOOOOOOR THIS IS WHY YOU ARE BACK AT THE HUB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    //loop through the attributes for the selected user
                                                    for (DataSnapshot snapshot : datasnapshot.getChildren())
                                                    {
                                                        String localRole = "";
                                                        //Every attribute in the database has a key, we are checking the key that is equal to role
                                                        //if the snapshot key is equal to role, set the check role to the value of role as a string so it can be used compare the different company roles.
                                                        if (snapshot.getKey().equals("role"))
                                                        {
                                                            String checkRole = snapshot.getValue().toString();
                                                            localRole = checkRole;

                                                            //if the role is equal to Admin and their company matches the one in the database, send the user to the admin hub
                                                            if (checkRole.equals("Admin"))
                                                            {
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginEmail(email);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginPassword(password);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, AdminHub.class));
                                                                //if the role is equal to Manager and their company matches the one in the database, send the user to the manager hub
                                                            } else if (checkRole.equals("Manager"))
                                                            {
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginEmail(email);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginPassword(password);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, ManagerHub.class));
                                                                //if the role is equal to Employee and their company matches the one in the database, send the user to the employee hub
                                                            } else if (checkRole.equals("Employee"))
                                                            {
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginEmail(email);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setloginPassword(password);
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, EmployeeHub.class));
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error)
                                                {

                                                }
                                            });
                                        } else
                                        {
                                            //else for check UID
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {
                                }
                            });

                        } else
                        {
                            Toast.makeText(LogIn.this, "Failed to login. Please check credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
