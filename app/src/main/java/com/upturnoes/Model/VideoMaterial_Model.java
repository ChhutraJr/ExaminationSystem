package com.upturnoes.Model;

public class VideoMaterial_Model {

    private String video_id,video_title,video_file,thumbnail_img,description,deadline,video_type,video_fee,dept_name,class_name,subject,
            video_status,download_allowed;

    public VideoMaterial_Model(String video_id, String video_title,String video_file,String thumbnail_img,String description, String deadline ,String video_type,String video_fee,
                               String dept_name,String class_name,String subject,String video_status,String download_allowed) {
        this.video_id = video_id;
        this.video_title = video_title;
        this.video_file = video_file;
        this.thumbnail_img= thumbnail_img;
        this.description= description;
        this.deadline = deadline;
        this.video_type = video_type;
        this.video_fee = video_fee;
        this.dept_name = dept_name;
        this.class_name = class_name;
        this.subject = subject;
        this.video_status = video_status;
        this.download_allowed = download_allowed;

    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getThumbnail_img() {
        return thumbnail_img;
    }

    public void setThumbnail_img(String thumbnail_img) {
        this.thumbnail_img = thumbnail_img;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVideo_fee() {
        return video_fee;
    }

    public void setVideo_fee(String video_fee) {
        this.video_fee = video_fee;
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

    public String getVideo_status() {
        return video_status;
    }

    public void setVideo_status(String video_status) {
        this.video_status = video_status;
    }

    public String getDownload_allowed() {
        return download_allowed;
    }

    public void setDownload_allowed(String download_allowed) {
        this.download_allowed = download_allowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
