package com.example.colo.Projects;

import java.util.ArrayList;

public class ProjectHelperClass {
    String pName, pDescription, pManagerUID;
    ArrayList<String> pEmployees;

    public ProjectHelperClass(String pName, String pDescription, String pManagerUID, ArrayList<String> pEmployees) {
        this.pName = pName;
        this.pDescription = pDescription;
        this.pManagerUID = pManagerUID;
        this.pEmployees = pEmployees;
    }

    public String getpName() {
        return pName;
    }

    public String getpDescription() {
        return pDescription;
    }

    public String getpManagerUID() {
        return pManagerUID;
    }

    public ArrayList<String> getpEmployees() {
        return pEmployees;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public void setpManagerUID(String pManagerUID) {
        this.pManagerUID = pManagerUID;
    }

    public void setpEmployees(ArrayList<String> pEmployees) {
        this.pEmployees = pEmployees;
    }
}