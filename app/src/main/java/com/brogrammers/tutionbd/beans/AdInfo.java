package com.brogrammers.tutionbd.beans;

import java.io.Serializable;

public class AdInfo implements Serializable {
    String tittle,salary,location,subject,studentClass,language,schedule,documentId;
    String userUid;
    long createdTime;

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
