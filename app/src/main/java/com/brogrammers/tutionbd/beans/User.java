package com.brogrammers.tutionbd.beans;

import java.io.Serializable;

public class User implements Serializable {
    private String userName, userMobile, userUid, userImageLink, documentId;

    public User() {
    }

    public User(String userName, String userMobile, String userUid, String userImageLink, String documentId) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userUid = userUid;
        this.userImageLink = userImageLink;
        this.documentId = documentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserImageLink() {
        return userImageLink;
    }

    public void setUserImageLink(String userImageLink) {
        this.userImageLink = userImageLink;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
