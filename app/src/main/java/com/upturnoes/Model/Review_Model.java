package com.upturnoes.Model;

public class Review_Model {
    private String quetion_id,selected_id,selected_answer,question,type,op1,op2,op3,op4,answer
            ,question_image,op1_image,op2_image,op3_image,op4_image;

    public Review_Model(String quetion_id,String selected_id,String selected_answer, String question, String type,String op1
            ,String op2,String op3,String op4,String answer,
                        String question_image,String op1_image,String op2_image,String op3_image,String op4_image) {
        this.quetion_id = quetion_id;
        this.selected_id = selected_id;
        this.selected_answer = selected_answer;
        this.question = question;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
        this.op3= op3;
        this.op4 = op4;
        this.answer = answer;
        this.question_image = question_image;
        this.op1_image =  op1_image;
        this.op2_image =  op2_image;
        this.op3_image= op3_image;
        this.op4_image = op4_image;
    }

    public String getQuetion_id() {
        return quetion_id;
    }

    public void setQuetion_id(String quetion_id) {
        this.quetion_id = quetion_id;
    }

    public String getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(String selected_id) {
        this.selected_id = selected_id;
    }

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }

    public String getOp1_image() {
        return op1_image;
    }

    public void setOp1_image(String op1_image) {
        this.op1_image = op1_image;
    }

    public String getOp2_image() {
        return op2_image;
    }

    public void setOp2_image(String op2_image) {
        this.op2_image = op2_image;
    }

    public String getOp3_image() {
        return op3_image;
    }

    public void setOp3_image(String op3_image) {
        this.op3_image = op3_image;
    }

    public String getOp4_image() {
        return op4_image;
    }

    public void setOp4_image(String op4_image) {
        this.op4_image = op4_image;
    }

}
