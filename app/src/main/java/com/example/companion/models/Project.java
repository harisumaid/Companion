package com.example.companion.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Project {

    String projectName;
    String projectDescription;
    Timestamp projectDueDate;

    public Project(String projectName, String projectDescription, Date projectDueDate){
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectDueDate = new Timestamp(projectDueDate);
    }

    public Project(){

    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Timestamp getProjectDueDate() {
        return projectDueDate;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setProjectDueDate(Timestamp projectDueDate) {
        this.projectDueDate = projectDueDate;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
