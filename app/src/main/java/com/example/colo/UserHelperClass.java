package com.example.colo;

import android.provider.MediaStore;

public class UserHelperClass<Id>
{

    long employeeId;
    String Name, Email, UserName, Password, DateText, RadioButton;


    //Constructor. New instance
    public UserHelperClass()
    {
    }

    //Constructor
    public UserHelperClass(long id, String name, String email, String userName, String password, String dateText, String radioButton)
    {

        employeeId = id;
        Name = name;
        Email = email;
        UserName = userName;
        Password = password;
        DateText = dateText;
        RadioButton = radioButton;

    }

    //getters and setters for all of the inputs
    public long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(long employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getUserName()
    {
        return UserName;
    }

    public void setUserName(String userName)
    {
        UserName = userName;
    }

    public String getPassword()
    {
        return Password;
    }

    public void setPassword(String password)
    {
        Password = password;
    }

    public String getDateText() { return DateText; }

    public void setDateText(String dateText) { DateText = dateText; }

    public String getRadioButton() { return RadioButton; }

    public void setRadioButton(String radioButton) { RadioButton = radioButton; }
}

