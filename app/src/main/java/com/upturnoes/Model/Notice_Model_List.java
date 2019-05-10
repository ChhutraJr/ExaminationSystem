package com.upturnoes.Model;

public class Notice_Model_List {

    private String id,title,notice,posted_date,admin_email,admin_avator;
    public boolean expanded = false;

    public Notice_Model_List(String id, String title, String notice,String posted_date ,String admin_email,String admin_avator) {
        this.id = id;
        this.title = title;
        this.notice = notice;
        this.posted_date = posted_date;
        this.admin_email = admin_email;
        this.admin_avator= admin_avator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_avator() {
        return admin_avator;
    }

    public void setAdmin_avator(String admin_avator) {
        this.admin_avator = admin_avator;
    }
}
