package com.octagon.octagondu;

public class InfoBus {
    String name;
    int locationOrClock;
    int image;

    public InfoBus(String name, int image, int locationOrClock) {
        this.name = name;
        this.image = image;
        this.locationOrClock = locationOrClock;
    }

    public int getLocationOrClock() {
        return locationOrClock;
    }

    public void setLocationOrClock(int locationOrClock) {
        this.locationOrClock = locationOrClock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}