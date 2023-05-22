package com.example.woofwisdomapplication.Users;

public class UserObject {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String message;
    private String sessionID;

    private String dogName = null;
    private Integer dogWeight = null;
    private Integer dogAge = null;
    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public Integer getDogWeight() {
        return dogWeight;
    }

    public void setDogWeight(Integer dogWeight) {
        this.dogWeight = dogWeight;
    }

    public Integer getDogAge() {
        return dogAge;
    }

    public void setDogAge(Integer dogAge) {
        this.dogAge = dogAge;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSessionId(String sessionId) {
        this.sessionID = sessionId;
    }
    public String getSessionID(){
        return sessionID;
    }

}