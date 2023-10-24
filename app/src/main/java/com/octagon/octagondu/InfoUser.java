package com.octagon.octagondu;

public class InfoUser{
    private String regNum;
    private String phoneNumber;
    private String fullName;
    private String department;
    private String session;
    private String userType;
    private String email;
    private String about;
    private String userImage;
    private String nickName;
    private int postCount;
    private int contributionCount;

    public InfoUser() {

    }

    public InfoUser(String regNum, String phoneNumber, String fullName, String department, String session, String userType, String email, String about, String userImage, String nickName, int postCount, int contributionCount) {
        this.regNum = regNum;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.department = department;
        this.session = session;
        this.userType = userType;
        this.email = email;
        this.about = about;
        this.userImage = userImage;
        this.nickName = nickName;
        this.postCount = postCount;
        this.contributionCount = contributionCount;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getContributionCount() {
        return contributionCount;
    }

    public void setContributionCount(int contributionCount) {
        this.contributionCount = contributionCount;
    }
}


