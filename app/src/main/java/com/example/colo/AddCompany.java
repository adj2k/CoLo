package com.example.colo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.core.Tag;

import java.util.Calendar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class AddCompany extends AppCompatActivity
{

    EditText CompanyName,Name, Email, Username, Password, VerifyPassword, EmployeeID;
    TextView DateText;
    Button DatePicker, CreateAccountBTN;
    DatePickerDialog.OnDateSetListener dateSetListener;
    android.widget.RadioGroup RadioGroupGender;
    RadioButton RadioButtonGender;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
//    private static final String EMPLOYEE = "Employee";
    private static final String TAG = "Add Company";
    private AdminHelperClass adminHelperClass;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);


        CompanyName = (EditText) findViewById(R.id.etAddCompany);
        Name = (EditText) findViewById(R.id.etNameEntry);
        Email = (EditText) findViewById(R.id.etEmailEntry);
        Username = (EditText) findViewById(R.id.etUserNameEntry);
        Password = (EditText) findViewById(R.id.etPasswordEntry);
        VerifyPassword = (EditText) findViewById(R.id.etPasswordConfirmation);
        EmployeeID = (EditText) findViewById(R.id.etEmployeeID);
        DatePicker = (Button) findViewById(R.id.btnSelectDate);
        DateText = (TextView) findViewById(R.id.tvDateText);
        CreateAccountBTN = (Button) findViewById(R.id.btnCreateAccount);

        RadioGroupGender = (RadioGroup) findViewById(R.id.RadioGroupGender);
        RadioButtonGender = (RadioButton) findViewById(R.id.rbNoAnswer);


        database = FirebaseDatabase.getInstance();
//        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();


        //Date of Birth Button
        DatePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCompany.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //Date of Birth Text View
        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                DateText.setText(date);
            }
        };

        //save data in Firebase on button click
        CreateAccountBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyName = CompanyName.getText().toString();
                String name = Name.getText().toString();
                String email = Email.getText().toString();
                String userName = Username.getText().toString();
                String password = Password.getText().toString();
                String employeeID = EmployeeID.getText().toString();
                String dateText = DateText.getText().toString();
                String gender = RadioButtonGender.getText().toString();
                String role = "Admin";

                if (validateName() & validateEmail() & validateUserName() & validatePassword() & validateVerificationPassword() & validateID() & validateDate() & validateGender())
                {
                    adminHelperClass = new AdminHelperClass(companyName, name, email, userName, "", employeeID, dateText, gender, role, null, null);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "createUserWithEmail:success");
//                                      FirebaseDatabase.getInstance().getReference("Employees "+uidpath);

//                                FirebaseDatabase.getInstance().getReference("Companies").child(companyName);
                                FirebaseDatabase.getInstance().getReference("Companies").child(companyName).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(adminHelperClass);
                            } else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AddCompany.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
//                    startActivity(new Intent(AddCompany.this, MainActivity.class));
                    finish();
                }
            }
        });
    }


    public void checkButtonGender(View v)
    {
        int radioId = RadioGroupGender.getCheckedRadioButtonId();
        RadioButtonGender = findViewById(radioId);
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

    private boolean validateUserName()
    {
        String val = Username.getText().toString();
        String checkSpaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty())
        {
            Username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20)
        {
            Username.setError("Username is too big");
            return false;
        } else if (!val.matches(checkSpaces))
        {
            Username.setError("No white spaces are allowed");
            return false;
        } else
        {

            Username.setError(null);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String val = Password.getText().toString();

        if (val.isEmpty())
        {
            Password.setError("Field can not be empty");
            return false;

        } else if (Password.length() < 6)
        {
            Password.setError("Password needs to be at least 6 characters long");
            return false;
        } else
        {

            Password.setError(null);
            return true;
        }
    }

    private boolean validateVerificationPassword()
    {
        String val = VerifyPassword.getText().toString();

        if (val.isEmpty())
        {
            VerifyPassword.setError("Field can not be empty");
            return false;

        } else if (!(Password.getText().toString().equals(VerifyPassword.getText().toString())))
        {
            VerifyPassword.setError("The passwords do not match");
            return false;
        } else
        {
            VerifyPassword.setError(null);
            return true;
        }
    }

    private boolean validateID()
    {
        String val = EmployeeID.getText().toString();

        if (val.isEmpty())
        {
            EmployeeID.setError("Field can not be empty");
            return false;
        } else
        {
            EmployeeID.setError(null);
            return true;
        }
    }

    private boolean validateDate()
    {
        String val = DateText.getText().toString();

        if (val.equals("Date"))
        {
            DateText.setError("Please enter your birthday");
            return false;
        } else
        {
            DateText.setError(null);
            return true;
        }
    }

    private boolean validateGender()
    {
        if (RadioGroupGender.getCheckedRadioButtonId() == -1)
        {
            RadioButtonGender.setError("Please select the gender");
            return false;
        } else
        {
            RadioButtonGender.setError(null);
            return true;
        }
    }





}


//Figure out JSON later
//JSONObject jsonObject = new JSONObject();
//                try {
//                        jsonObject.put("name", Name);
//                        jsonObject.put("email", Email);
//                        jsonObject.put("username", Username);
//                        jsonObject.put("password", Password);
//                        jsonObject.put("employeeage", EmployeeAge);
//                        } catch (JSONException e) {
//                        e.printStackTrace();
//                        }