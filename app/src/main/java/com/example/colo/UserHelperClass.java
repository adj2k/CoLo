package com.example.colo;

import android.provider.MediaStore;

import java.util.Calendar;

public class UserHelperClass<Id>
{
    String CompanyName, Name, Email, UserName, Password, EmployeeID, DateText, Gender, Role;
    Boolean OneTimePassword;
    Calendar ClockInTime;
    Calendar ClockOutTime;
    //Constructor. New instance
    public UserHelperClass() { }

    //Constructor
    public UserHelperClass(String companyName, String name, String email, String userName, String password, String employeeID, String dateText, String gender, String role,
                           Calendar clockInTime, Calendar clockOutTime, Boolean oneTimePassword)
    {
        CompanyName = companyName;
        Name = name;
        Email = email;
        UserName = userName;
        Password = password;
        EmployeeID = employeeID;
        DateText = dateText;
        Gender = gender;
        Role = role;
        ClockInTime = clockInTime;
        ClockOutTime = clockOutTime;
        OneTimePassword = oneTimePassword;
    }

    //getters and setters for all of the inputs


    public String getCompanyName() { return CompanyName; }

    public void setCompanyName(String companyName) { CompanyName = companyName; }

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

    public String getEmployeeID() { return EmployeeID; }

    public void setEmployeeID(String employeeID) { EmployeeID = employeeID; }

    public String getDateText() { return DateText; }

    public void setDateText(String dateText) { DateText = dateText; }

    public String getGender() { return Gender; }

    public void setGender(String gender) { Gender = gender; }

    public String getRole() { return Role; }

    public void setRole(String role) { Role = role; }

    public Boolean getOneTimePassword() { return OneTimePassword; }

    public void setOneTimePassword(Boolean oneTimePassword) { OneTimePassword = oneTimePassword; }
}