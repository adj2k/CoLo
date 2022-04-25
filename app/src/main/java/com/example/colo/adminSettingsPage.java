package com.example.colo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;

public class adminSettingsPage extends AppCompatActivity {

    TextView EmployeeID, DOB, Name;
    EditText Email;
    Button SaveNameButton, SaveEmailButton, ResetPassword, SignOut, DeleteCompany;
    FirebaseUser user;
    String UID, full_name, displayEmail, displayCompany, displayID;
    TextInputLayout hint_name, hint_email;
    TextView CompanyText, EmployeeIDText;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "CreateAccount";
    private UserHelperClass userHelperClass;
    private String companyNameRef = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_settings_page);

        EmployeeID = (TextView) findViewById(R.id.showEmployeeID);
        DOB = (TextView) findViewById(R.id.showEmployeeDOB);
        Name = (EditText) findViewById(R.id.etAdminName);
        Email = (EditText) findViewById(R.id.etEmployeeEmail);
        SaveNameButton = (Button) findViewById(R.id.saveAdminName);
        SaveEmailButton = (Button) findViewById(R.id.saveAdminEmail);
        ResetPassword = (Button) findViewById(R.id.btnResetPassword);
        SignOut = (Button) findViewById(R.id.btnSignOut);
        DeleteCompany = (Button) findViewById(R.id.btnDeleteCompany);
        hint_name = (TextInputLayout) findViewById(R.id.showAdminName);
        hint_email = (TextInputLayout) findViewById(R.id.showAdminEmail);
        CompanyText = (TextView) findViewById(R.id.showCompany);
        EmployeeIDText = (TextView) findViewById(R.id.showEmployeeID);



        companyNameRef = ((GlobalCompanyName) this.getApplication()).getGlobalCompanyName();

        // Database Setup
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Manager");
        mAuth = FirebaseAuth.getInstance();
        // Get's current user
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        Log.i("UID: ", UID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(UID);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(EMPLOYEE);
        mAuth = FirebaseAuth.getInstance();

        // This function sets the Admin Name Hint Field
        ref.child("name").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                full_name = snapshot.getValue(String.class);
                hint_name.setHint(full_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // This function sets the Admin Email Hint Field
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

        // This function sets the Company Display Name
        displayCompany = companyNameRef.toString();
        CompanyText.setText(displayCompany);

        // This function sets the Admin ID Display
        ref.child("employeeID").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                displayID = snapshot.getValue(String.class);
                EmployeeIDText.setText(displayID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        SaveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(UID);
                if (validateName()) {
                    ref.child("name").setValue(name);
                }
            }
        });

        SaveEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies/" + companyNameRef).child(UID);
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
                startActivity(new Intent(adminSettingsPage.this, LogIn.class));
            }
        });

        DeleteCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Delete Company from DB
                Toast.makeText(getApplicationContext(), "Please verify via Email company deletion!", Toast.LENGTH_SHORT).show();
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
