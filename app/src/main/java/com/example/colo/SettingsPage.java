package com.example.colo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class SettingsPage extends AppCompatActivity {

    TextView EmployeeID, DOB, Name;
    EditText Email;
    Button SaveButton, ResetPassword, SignOut;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";
    private UserHelperClass userHelperClass;
    private String CompanyName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        EmployeeID = (TextView) findViewById(R.id.showEmployeeID);
        DOB = (TextView) findViewById(R.id.showEmployeeDOB);
        Name = (TextView) findViewById(R.id.showEmployeeFullName);
        Email = (EditText) findViewById(R.id.etEmployeeEmail);
        SaveButton = (Button) findViewById(R.id.saveEmployeeEmail);
        ResetPassword = (Button) findViewById(R.id.btnResetPassword);
        SignOut = (Button) findViewById(R.id.btnSignOut);


        CompanyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(EMPLOYEE);
        mAuth = FirebaseAuth.getInstance();


        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();

                if (validateEmail()) {
                    // TODO: UPDATE EMAIL HERE TO DATABASE
                }
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Use toast to say password reset email sent, doesn't need actual functionality
                Context message = getApplicationContext();
                CharSequence text = "Password reset email sent";
                int duration = Toast.LENGTH_SHORT;
                Toast updatePassword = Toast.makeText(message, text, duration);
                updatePassword.show();
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Make them sign out
                // Took it from main page idk if it can still work like that
                mAuth.signOut();
                startActivity(new Intent(SettingsPage.this, LogIn.class));
            }
        });
    }

    private boolean validateName()
    {
        String val = Name.getText().toString();

        if (val.isEmpty())
        {
            Name.setError("Field can not be empty");
            return false;
        } else
        {
            Name.setError(null);
            return true;
        }
    }

    private boolean validateEmail()
    {
        String val = Email.getText().toString();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty())
        {
            Email.setError("Field can not be empty");
            return false;

        } else if (!val.matches(checkEmail))
        {
            Email.setError("Invalid Email");
            return false;
        } else
        {

            Email.setError(null);
            return true;
        }
    }


}
