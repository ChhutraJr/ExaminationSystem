package com.upturnoes.Model;

public class Enroll_Model_List {
    private String enroll_num,first_name,last_name,exam_id,study_mterial_id,video_id,expiry_date;

    public Enroll_Model_List(String enroll_num, String first_name, String last_name,String exam_id ,
                             String study_mterial_id,String video_id,String expiry_date) {
        this.enroll_num = enroll_num;
        this.first_name = first_name;
        this.last_name = last_name;
        this.exam_id = exam_id;
        this.study_mterial_id= study_mterial_id;
        this.video_id= video_id;
        this.expiry_date = expiry_date;
    }

    public String getEnroll_num() {
        return enroll_num;
    }

    public void setEnroll_num(String enroll_num) {
        this.enroll_num = enroll_num;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getStudy_mterial_id() {
        return study_mterial_id;
    }

    public void setStudy_mterial_id(String study_mterial_id) {
        this.study_mterial_id = study_mterial_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
