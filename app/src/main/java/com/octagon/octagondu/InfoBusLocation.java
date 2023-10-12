package com.octagon.octagondu;

public class InfoBusLocation {
    private int RegNum;
    private String Name;
    private String Dept;
    private int picture;
    private String lastLocation;
    private String time;
    private String date;
    private String lat;
    private String lon;

    public InfoBusLocation(int regNum, String name, String dept, int picture, String lastLocation, String time, String date, String lat, String lon) {
        RegNum = regNum;
        Name = name;
        Dept = dept;
        this.picture = picture;
        this.lastLocation = lastLocation;
        this.time = time;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
    }

    public InfoBusLocation() {

    }
    public int getRegNum() {
        return RegNum;
    }

    public void setRegNum(int regNum) {
        RegNum = regNum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setData(String data) {
        this.date = data;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
