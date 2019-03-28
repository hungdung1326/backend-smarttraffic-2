package com.smarttraffic.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.smarttraffic.pojo.userDetail;

import java.util.UUID;

@Entity

public class emergencyRequest {
    @Id
    private String id;
    @Index
    private String userID;
    @Index
    private String location;
    @Index
    private String problem;
    @Index
    private userDetail userDetail;

    public emergencyRequest() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public com.smarttraffic.pojo.userDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(com.smarttraffic.pojo.userDetail userDetail) {
        this.userDetail = userDetail;
    }
}