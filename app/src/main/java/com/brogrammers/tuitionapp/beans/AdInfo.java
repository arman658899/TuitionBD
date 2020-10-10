package com.brogrammers.tuitionapp.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdInfo implements Serializable {
    @Expose
    @SerializedName("tittle")
    private String tittle;
    @Expose
    @SerializedName("salary")
    private String salary;
    @Expose
    @SerializedName("location")
    private String location;
    @Expose
    @SerializedName("subject")
    private String subject;
    @Expose
    @SerializedName("studentClass")
    private String studentClass;
    @Expose
    @SerializedName("language")
    private String language;
    @Expose
    @SerializedName("schedule")
    private String schedule;
    @Expose
    @SerializedName("documentId")
    private String documentId;
    @Expose
    @SerializedName("userUid")
    private String userUid;
    @Expose
    @SerializedName("createdTime")
    private long createdTime;

    public AdInfo() {
    }

    public AdInfo(String tittle, String salary, String location, String subject, String studentClass, String language, String schedule, String documentId, String userUid, long createdTime) {
        this.tittle = tittle;
        this.salary = salary;
        this.location = location;
        this.subject = subject;
        this.studentClass = studentClass;
        this.language = language;
        this.schedule = schedule;
        this.documentId = documentId;
        this.userUid = userUid;
        this.createdTime = createdTime;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
