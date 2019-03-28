package com.smarttraffic.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity
public class CredenticalUser {
    @Id
    private String userEmail;
    @Index
    private String tokenKey;
    @Index
    private String secretToken;
    @Index
    private long createdTime;
    @Index
    private long expiredTime;
    @Index
    private int status;


    public CredenticalUser(String email){
        userEmail= email;
        tokenKey = UUID.randomUUID().toString();
        secretToken = tokenKey;
        createdTime = System.currentTimeMillis();
        expiredTime = createdTime + 24*3600*1000;
        status = 1;
    }

    public CredenticalUser(){}

    public static CredenticalUser loadCredential(String secretToken) {
        if (secretToken == null) {
            return null;
        }
        CredenticalUser credential = ofy().load().type(CredenticalUser.class).filter("secretToken",secretToken).first().now();
        if (credential == null) {
            return null;
        }
//        if (System.currentTimeMillis() > credential.getExpiredTime()) {
//            credential.status = 0;
//            ofy().save().entity(credential).now();
//            return null;
//        }
        return credential;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
