package com.octagon.octagondu;

public class InfoBusLocation {
    private String regNum;
    private String busName;
    private String busTime;
    private String busID;
    private String lastLocation;
    private  String time;
    private String date;
    private String voteCount;
    private String lat;
    private String lon;

    public InfoBusLocation() {

    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public InfoBusLocation(String busID, String regNum, String busName, String busTime, String lastLocation, String time, String date, String voteCount, String lat, String lon) {
        this.regNum = regNum;
        this.busName = busName;
        this.busTime = busTime;
        this.lastLocation = lastLocation;
        this.time = time;
        this.date = date;
        this.voteCount = voteCount;
        this.lat = lat;
        this.lon = lon;
        this.busID = busID;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusTime() {
        return busTime;
    }

    public void setBusTime(String busTime) {
        this.busTime = busTime;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
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
