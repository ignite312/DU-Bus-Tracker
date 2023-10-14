package com.octagon.octagondu;

public class InfoUser{
    private String fullName;
    private String regNum;
    private String department;
    private String session;
    private String email;
    private String phoneNumber;
    private int contributionCount;
    private String userType;
    private String userImage;

    public InfoUser() {

    }
    public InfoUser(String fullName, String regNum, String department, String session, String email, String phoneNumber, int contributionCount, String userType, String userImage) {
        this.fullName = fullName;
        this.regNum = regNum;
        this.department = department;
        this.session = session;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.contributionCount = contributionCount;
        this.userType = userType;
        this.userImage = userImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getContributionCount() {
        return contributionCount;
    }

    public void setContributionCount(int contributionCount) {
        this.contributionCount = contributionCount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}


