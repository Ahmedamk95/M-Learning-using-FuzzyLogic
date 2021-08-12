package com.saboo.mlearning;

/**
 * Created by Sohail on 15-04-2017.
 */

public class FeedbackProfs {

    private String staff_name = "";
    private String status = "";
    private boolean is_feedback_given = false;
    private String username = "";

    public FeedbackProfs(){

    }

    public String getStaffName() {
        return staff_name;
    }

    public void setStaffNname(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean is_feedback_given() {
        return is_feedback_given;
    }

    public void setIs_feedback_given(boolean is_feedback_given) {
        this.is_feedback_given = is_feedback_given;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
