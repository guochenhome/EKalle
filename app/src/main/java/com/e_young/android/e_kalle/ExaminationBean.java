package com.e_young.android.e_kalle;

import java.io.Serializable;
import java.util.List;

public class ExaminationBean implements Serializable {
    private String  message;
    private String status;
    private List<ExaminationQuestions> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ExaminationQuestions> getData() {
        return data;
    }

    public void setData(List<ExaminationQuestions> data) {
        this.data = data;
    }
}
