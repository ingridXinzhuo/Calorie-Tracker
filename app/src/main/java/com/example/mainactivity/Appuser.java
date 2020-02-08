package com.example.mainactivity;

import java.io.Serializable;

public class Appuser implements Serializable {
    private Integer userid;
    private String firstname;
    private String surname;
    private String email;

    private String dob;

    private String height;

    private String weight;

    private String gender;
    private String address;
    private String postcode;
    private String levelOfActivity;
    private String stepsPerMile;

    public Appuser()
    {

    }

    public Appuser(Integer userid, String firstname, String surname, String email, String dob, String height, String weight, String gender, String address, String postcode, String levelOfActivity, String stepsPerMile) {
        this.userid = userid;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLevelOfActivity() {
        return levelOfActivity;
    }

    public void setLevelOfActivity(String levelOfActivity) {
        this.levelOfActivity = levelOfActivity;
    }

    public String getStepsPerMile() {
        return stepsPerMile;
    }

    public void setStepsPerMile(String stepsPerMile) {
        this.stepsPerMile = stepsPerMile;
    }
}
