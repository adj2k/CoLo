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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class SettingsPage extends AppCompatActivity {

    TextView EmployeeID, DOB, Name;
    EditText Email;
    Button SaveButton, ResetPassword, SignOut;
    String UID, full_name, displayEmail, displayDOB, displayID;
    TextInputLayout hint_name, hint_email;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";
    private UserHelperClass userHelperClass;
    private String CompanyName = "";
    private FirebaseUser user;


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
        hint_email = (TextInputLayout) findViewById(R.id.showEmployeeEmail);


        CompanyName = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(EMPLOYEE);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + CompanyName).child(UID);


        // This function sets the Employee ID Display
        ref.child("employeeID").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                displayID = snapshot.getValue(String.class);
                EmployeeID.setText(displayID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function sets the Employee DOB Display
        ref.child("dateText").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                displayDOB = snapshot.getValue(String.class);
                DOB.setText(displayDOB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function sets the Employee Name Display
        ref.child("name").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                full_name = snapshot.getValue(String.class);
                Name.setText(full_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function sets the Employee Email Hint Field
        ref.child("email").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                displayEmail = snapshot.getValue(String.class);
                hint_email.setHint(displayEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + CompanyName).child(UID);
                if (validateEmail()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    ref.child("email").setValue(email);
                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email Changed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
