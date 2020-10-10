package com.brogrammers.tuitionapp.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @Expose
    @SerializedName("userName")
    private String userName;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("year")
    private String year;
    @Expose
    @SerializedName("userMobile")
    private String userMobile;
    @Expose
    @SerializedName("userUid")
    private String userUid;
    @Expose
    @SerializedName("userImageLink")
    private String userImageLink;
    @Expose
    @SerializedName("documentId")
    private String documentId;

    //additional data for teacher
    @Expose
    @SerializedName("college")
    private String college="";
    @Expose
    @SerializedName("subject")
    private String subject="";
    @Expose
    @SerializedName("idCardLink")
    private String idCardLink="";

    public User() {
    }

    public User(String userName, String gender, String year, String userMobile, String userUid, String userImageLink, String documentId, String college, String subject, String idCardLink) {
        this.userName = userName;
        this.gender = gender;
        this.year = year;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
