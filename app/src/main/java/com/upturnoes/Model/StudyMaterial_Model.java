package com.upturnoes.Model;

public class StudyMaterial_Model {

    private String study_id,study_title,deadline,study_type,study_fee,pdf_file,allow_download,dept_name,class_name,subject,
            study_status,download_allowed;

    public StudyMaterial_Model(String study_id, String study_title, String deadline,String study_type ,String study_fee,
                               String pdf_file,String allow_download,
                           String dept_name,String class_name,String subject,String study_status,String download_allowed) {
        this.study_id = study_id;
        this.study_title = study_title;
        this.deadline = deadline;
        this.study_type = study_type;
        this.study_fee = study_fee;
        this.pdf_file = pdf_file;
        this.allow_download=allow_download;
        this.dept_name = dept_name;
        this.class_name = class_name;
        this.subject = subject;
        this.study_status = study_status;
        this.download_allowed = download_allowed;

    }

    public String getStudy_id() {
        return study_id;
    }

    public void setStudy_id(String study_id) {
        this.study_id = study_id;
    }

    public String getStudy_title() {
        return study_title;
    }

    public void setStudy_title(String study_title) {
        this.study_title = study_title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStudy_type() {
        return study_type;
    }

    public void setStudy_type(String study_type) {
        this.study_type = study_type;
    }

    public String getStudy_fee() {
        return study_fee;
    }

    public void setStudy_fee(String study_fee) {
        this.study_fee = study_fee;
    }

    public String getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(String pdf_file) {
        this.pdf_file = pdf_file;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudy_status() {
        return study_status;
    }

    public void setStudy_status(String study_status) {
        this.study_status = study_status;
    }

    public String getDownload_allowed() {
        return download_allowed;
    }

    public void setDownload_allowed(String download_allowed) {
        this.download_allowed = download_allowed;
    }

    public String getAllow_download() {
        return allow_download;
    }

    public void setAllow_download(String allow_download) {
        this.allow_download = allow_download;
    }
}
