package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

    //Firebase
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

//                    Log.i("Company: ", snapshot.getKey());
                    arrayList.add(snapshot.getKey());

                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //
        System.out.println("HEELELELELELLE");


        /*
        AutoCompleteTextView autocomplete =  (AutoCompleteTextView) (R.id.etCompany);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arrayList);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);
        Log.i("Company: ","asdaasdads");
        */
        //


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

    private void logIn()
    {


        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String company = Company.getText().toString();



        if(company.isEmpty())
        {
            Company.setError("Company is required");
            Company.requestFocus();
            return;
        }

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
                            DatabaseReference referenceCompany =  FirebaseDatabase.getInstance().getReference("Companies").child(company);


                            DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Companies").child(company)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //FirebaseAuth.getInstance().getCurrentUser().getUid() => Key from auth
                            //Get ref from the company that they chose in login
                            //////// LOOP THRU EVERY KEY


                            referenceCompany.addValueEventListener(new ValueEventListener() {
                                @Override



                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                    for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                        if(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            // Auth Key = Key in Company => proceed to check for role
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override

                                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                                    for(DataSnapshot snapshot : datasnapshot.getChildren()){
                                                        if(snapshot.getKey().equals("role")) {
                                                            String checkRole = snapshot.getValue().toString();
                                                            Log.i("Role: ", checkRole);

                                                            if(checkRole.equals("Admin") || checkRole.equals("Manager")){
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, ManagerHub.class));
                                                            } else if (checkRole.equals("Employee")){
                                                                ((GlobalCompanyName) LogIn.super.getApplication()).setGlobalCompanyName(company);
                                                                startActivity(new Intent(LogIn.this, EmployeeHub.class));
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
                                            // Return
//                                            Toast.makeText(LogIn.this, "YOU DON'T WORK FOR THIS COMPANY.", Toast.LENGTH_LONG).show();

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
