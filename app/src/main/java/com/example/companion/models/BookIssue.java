package com.example.companion.models;

import com.google.firebase.Timestamp;

import java.util.Date;


public class BookIssue {
    String bookName;
    Timestamp issueDate;
    Timestamp dueDate;

    public  BookIssue(){

    }

    public BookIssue(String bookName, Date iDate, Date dDate){
        this.bookName = bookName;
        issueDate = new Timestamp(iDate);
        dueDate = new Timestamp(dDate);
    }

    public String getBookName(){
        return bookName;
    }
    public Timestamp getIssueDate(){
        return issueDate;
    }
    public Timestamp getDueDate(){
        return dueDate;
    }

    public void setBookName(String bookName){
        this.bookName = bookName;
    }

    public void setIssueDate(Timestamp issueDate){
        this.issueDate = issueDate;
    }

    public void setDueDate(Timestamp dueDate){
        this.dueDate = dueDate;
    }

}
