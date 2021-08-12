package com.saboo.mlearning;

/**
 * Created by Sohail on 24-04-2017.
 */

public class QuizItems {

    private String quizTitle;
    private String quizBy;
    private String quizSem;
    private String quizContent;
    private String quizId;
    private boolean isQuizGiven;

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuizBy() {
        return quizBy;
    }

    public void setQuizBy(String quizBy) {
        this.quizBy = quizBy;
    }

    public String getQuizSem() {
        return quizSem;
    }

    public void setQuizSem(String quizSem) {
        this.quizSem = quizSem;
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setQuizContent(String quizContent) {
        this.quizContent = quizContent;
    }

    public boolean isQuizGiven() {
        return isQuizGiven;
    }

    public void setQuizGiven(boolean quizGiven) {
        isQuizGiven = quizGiven;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
