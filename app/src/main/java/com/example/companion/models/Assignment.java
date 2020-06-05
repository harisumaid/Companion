package com.example.companion.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Assignment {

    String assignmentName;
    String assignmentDescription;
    Timestamp assignmentDueDate;

    public Assignment(String assignmentName, String assignmentDescription, Date assignmentDueDate){
        this.assignmentName = assignmentName;
        this.assignmentDescription = assignmentDescription;
        this.assignmentDueDate = new Timestamp(assignmentDueDate);
    }

    public Assignment(){

    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public void setAssignmentDueDate(Timestamp assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public Timestamp getAssignmentDueDate() {
        return assignmentDueDate;
    }

}

