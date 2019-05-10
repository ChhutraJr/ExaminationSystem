package com.upturnoes.Model;

public class Assessment_Model_List {

    private String id,exam,date_taken,completed,score,status;

    public Assessment_Model_List(String id, String exam, String date_taken,String completed ,String score,String status) {
        this.id = id;
        this.exam = exam;
        this.date_taken = date_taken;
        this.completed = completed;
        this.score = score;
        this.status= status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
