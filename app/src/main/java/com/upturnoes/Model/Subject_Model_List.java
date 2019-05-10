package com.upturnoes.Model;

public class Subject_Model_List {

    private String sub_id,sub_name,dept_name,class_name,reg_date;

    public Subject_Model_List(String sub_id, String sub_name, String dept_name,String class_name ,String reg_date) {
        this.sub_id = sub_id;
        this.sub_name = sub_name;
        this.dept_name = dept_name;
        this.class_name = class_name;
        this.reg_date = reg_date;

    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
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

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
