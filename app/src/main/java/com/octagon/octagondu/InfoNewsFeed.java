package com.octagon.octagondu;

public class InfoNewsFeed {
    private  int image;
    private String busName;
    private String posted_by;
    private String userType;
    private String dept;
    private String date;
    private String desc;
    private int cnt;
    public InfoNewsFeed(int image, String busName, String posted_by, String dept, String date, String desc, int cnt, String userType) {
        this.image = image;
        this.busName = busName;
        this.userType = userType;
        this.posted_by = posted_by;
        this.dept = dept;
        this.date = date;
        this.desc = desc;
        this.cnt = cnt;
    }
    public InfoNewsFeed() {

    }
    public int getImage() {
        return image;
    }

    public String getUserType() {
        return userType;
    }

    public String getBusName() {
        return busName;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public String getDept() {
        return dept;
    }
    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public int getCnt() {
        return cnt;
    }
}
