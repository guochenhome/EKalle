package com.e_young.android.e_kalle;

/**
 * Created by YXVZB on 2016/6/20.
 *
 *
 "data": [{
 "optionE": "浙江北部、江苏安徽的南部",
 "optionF": null,
 "optionC": "江西",
 "optionD": "湖北",
 "optionA": "黑龙江",
 "answerAnalysis": "1",
 "optionB": "吉林",
 "title": "江南指的是",
 "correctAnswer": "A"
 }],
 "message": "操作成功",
 "status": "1"
 }
 */
public class ExaminationQuestions {

    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String optionF;
    private String correctAnswer;
    private String answerAnalysis;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOptionF() {
        return optionF;
    }

    public void setOptionF(String optionF) {
        this.optionF = optionF;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }
}
