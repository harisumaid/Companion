package com.example.companion.models;

public class User {

    String email;
    String name;
    String regd;
    String branch;
    String sem;

    public User(String email, String name, String regd, String branch, String sem) {
        this.email = email;
        this.name = name;
        this.regd = regd;
        this.branch = branch;
        this.sem = sem;
    }
    public User(){

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegd(String regd) {
        this.regd = regd;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSem(String sem){
        this.sem = sem;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public String getRegd(){
        return regd;
    }

    public String getBranch(){
        return branch;
    }

    public String getSem(){
        return sem;
    }
}
