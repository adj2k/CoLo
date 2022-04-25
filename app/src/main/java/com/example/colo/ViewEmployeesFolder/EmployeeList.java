package com.example.colo.ViewEmployeesFolder;

public class EmployeeList {

    String name, employeeID, email, firebaseId, companyName, role;
    boolean clockStatus;

    public boolean getClockStatus() { return clockStatus; }

    public String getRole() { return role; }

    public String getCompanyName() { return companyName; }
    public String getFirebaseId() { return firebaseId; }
    public String getName() { return name; }
    public String getEmployeeID() { return employeeID; }
    public String getEmail() { return email; }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
    public void setClockStatus(boolean clockStatus) { this.clockStatus = clockStatus; }
}
