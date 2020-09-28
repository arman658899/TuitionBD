package com.brogrammers.tutionbd.beans;

import java.io.Serializable;

public class User implements Serializable {
    private String userName, gender,userMobile, userUid, userImageLink, documentId;

    //additional data for teacher
    private String college="",subject="",idCardLink="";

    public User() {
    }

    public User(String userName, String gender, String userMobile, String userUid, String userImageLink, String documentId, String college, String subject, String idCardLink) {
        this.userName = userName;
        this.gender = gender;
        this.userMobile = userMobile;
        this.userUid = userUid;
        this.userImageLink = userImageLink;
        this.documentId = documentId;
        this.college = college;
        this.subject = subject;
        this.idCardLink = idCardLink;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdCardLink() {
        return idCardLink;
    }

    public void setIdCardLink(String idCardLink) {
        this.idCardLink = idCardLink;
    }
}
