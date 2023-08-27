package com.octagon.octagondu;

public class AdminInformation {
    private String busName;
    private String userName;
    private String password;

    public AdminInformation() {
        // Default constructor required for Firebase Realtime Database
    }

    public AdminInformation(String busName, String userName, String password) {
        this.busName = busName;
        this.userName = userName;
        this.password = password;
    }

    public String getBusName() {
        return busName;
    }

    public void setbusName(String busName) {
        this.busName = busName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

