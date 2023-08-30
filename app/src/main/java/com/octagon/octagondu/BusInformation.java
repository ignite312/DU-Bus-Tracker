package com.octagon.octagondu;

public class BusInformation {
    private String busType;
    private String busId;
    private String busTime;
    private String startLocation;
    private String destinationLocation;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public BusInformation() {
        // Default constructor required for Firebase Realtime Database
    }

    public BusInformation(String busType, String busId, String startLocation, String destinationLocation, String busTime, String status) {
        this.busType = busType;
        this.busId = busId;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.busTime = busTime;
        this.status = status;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getTime() {
        return busTime;
    }

    public void setTime(String busTime) {
        this.busTime = busTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }
}

