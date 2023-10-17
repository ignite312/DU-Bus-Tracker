package com.octagon.octagondu;

public class InfoNewsFeed {
    private String userId;
    private String busName;
    private  String helpType;
    private String title;
    private String desc;
    private String vote;
    private String time;
    private String  date;
    private String  status;
    private  String postId;

    public InfoNewsFeed() {

    }
    public InfoNewsFeed(String userId, String busName, String helpType, String title, String desc, String vote, String time, String date, String status, String postId) {
        this.userId = userId;
        this.busName = busName;
        this.helpType = helpType;
        this.title = title;
        this.desc = desc;
        this.vote = vote;
        this.time = time;
        this.date = date;
        this.status = status;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getHelpType() {
        return helpType;
    }

    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
