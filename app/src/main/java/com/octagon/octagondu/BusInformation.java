package com.octagon.octagondu;

public class BusInformation {
    private String busType;
    private String busId;
    private String time;
    private String startLocation;
    private String destinationLocation;

    public BusInformation() {
        // Default constructor required for Firebase Realtime Database
    }
    public BusInformation(String busType, String busId, String startLocation, String destinationLocation, String time) {
        this.busType = busType;
        this.busId = busId;
        this.time = time;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
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
        return time;
    }
    public void setTime(String time) {
        this.time = time;
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

