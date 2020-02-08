package com.example.mainactivity;

public class Credential {
    private Integer credentialid;
    private String username;
    private String passwordHash;
    private String sighupDate;
    private Appuser userid;

    public Credential(Integer credentialid, String username, String passwordHash, String sighupDate, Appuser userid) {
        this.credentialid = credentialid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.sighupDate = sighupDate;
        this.userid = userid;
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSighupDate() {
        return sighupDate;
    }

    public void setSighupDate(String sighupDate) {
        this.sighupDate = sighupDate;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }
}
