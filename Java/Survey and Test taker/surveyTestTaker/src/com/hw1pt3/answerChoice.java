package com.hw1pt3;

import java.io.Serializable;

public class answerChoice extends answer implements Serializable {
    private String answerText;
    private boolean isCorrect;

    answerChoice(String text) {
        this.answerText = text;
        this.isCorrect = false;
    }

    answerChoice(String text, boolean isAnswerCorrect) {
        this.answerText = text;
        this.isCorrect = isAnswerCorrect;
    }

    @Override
    public void print(output out) {
        out.printOut(this.answerText);
        out.printOut(Boolean.toString(this.isCorrect));
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public void setAnswerText(String newText) {
        this.answerText = newText;
    }

    public boolean getIsCorrect() {
        return this.isCorrect;
    }

    public void setIsCorrect(boolean isAnswerCorrect) {
        this.isCorrect = isAnswerCorrect;
    }
}
