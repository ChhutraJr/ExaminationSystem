package com.upturnoes.Model;

public class Exam_Model_List {

    private String exam_id,duration,passmark,re_take_after,exam_title,type,exam_fee,dept_name,class_name,subject,
            deadline,exam_status,next_retake,quetions,exam_attended,student_retake,exam_allowed,next_retake_b,terms;

    public Exam_Model_List(String exam_id, String duration, String passmark,String re_take_after ,String exam_title,String type,
                           String exam_fee,String dept_name,String class_name,String subject,String deadline,String exam_status
                                ,String next_retake,String quetions,String exam_attended,String student_retake,
                                String exam_allowed,String next_retake_b,String terms) {
        this.exam_id = exam_id;
        this.duration = duration;
        this.passmark = passmark;
        this.re_take_after = re_take_after;
        this.exam_title = exam_title;
        this.type = type;
        this.exam_fee = exam_fee;
        this.dept_name = dept_name;
        this.class_name = class_name;
        this.subject = subject;
        this.deadline = deadline;
        this.exam_status = exam_status;
        this.next_retake = next_retake;
        this.quetions= quetions;
        this.exam_attended = exam_attended;
        this.student_retake = student_retake;
        this.exam_allowed = exam_allowed;
        this.next_retake_b = next_retake_b;
        this.terms = terms;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPassmark() {
        return passmark;
    }

    public void setPassmark(String passmark) {
        this.passmark = passmark;
    }

    public String getRe_take_after() {
        return re_take_after;
    }

    public void setRe_take_after(String re_take_after) {
        this.re_take_after = re_take_after;
    }

    public String getExam_title() {
        return exam_title;
    }

    public void setExam_title(String exam_title) {
        this.exam_title = exam_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExam_fee() {
        return exam_fee;
    }

    public void setExam_fee(String exam_fee) {
        this.exam_fee = exam_fee;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getExam_status() {
        return exam_status;
    }

    public void setExam_status(String exam_status) {
        this.exam_status = exam_status;
    }

    public String getNext_retake() {
        return next_retake;
    }

    public void setNext_retake(String next_retake) {
        this.next_retake = next_retake;
    }

    public String getQuetions() {
        return quetions;
    }

    public void setQuetions(String quetions) {
        this.quetions = quetions;
    }

    public String getExam_attended() {
        return exam_attended;
    }

    public void setExam_attended(String exam_attended) {
        this.exam_attended = exam_attended;
    }

    public String getStudent_retake() {
        return student_retake;
    }

    public void setStudent_retake(String student_retake) {
        this.student_retake = student_retake;
    }

    public String getExam_allowed() {
        return exam_allowed;
    }

    public void setExam_allowed(String exam_allowed) {
        this.exam_allowed = exam_allowed;
    }

    public String getNext_retake_b() {
        return next_retake_b;
    }

    public void setNext_retake_b(String next_retake_b) {
        this.next_retake_b = next_retake_b;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
