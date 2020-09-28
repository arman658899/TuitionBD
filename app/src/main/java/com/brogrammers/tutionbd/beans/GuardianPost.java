package com.brogrammers.tutionbd.beans;

public class GuardianPost {
    String tittle,salary,location,subject,studentClass,postId,documentId;
    String userUid;
    long createdTime;

    public GuardianPost() {
    }

    public GuardianPost(String tittle, String salary, String location, String subject, String studentClass, String postId, String documentId, String userUid, long createdTime) {
        this.tittle = tittle;
        this.salary = salary;
        this.location = location;
        this.subject = subject;
        this.studentClass = studentClass;
        this.postId = postId;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
