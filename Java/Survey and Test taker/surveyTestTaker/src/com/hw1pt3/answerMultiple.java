package com.hw1pt3;

import java.io.Serializable;

public class answerMultiple extends answer implements Serializable {
    private answerMultipleHalf leftAnswer;
    private answerMultipleHalf rightAnswer;
    private int answerIndex;

    //this should be used for ranking questions
    answerMultiple(String left, int i) {
        this.answerIndex = i;
        this.leftAnswer = new answerMultipleHalf(left, i);
        this.rightAnswer = new answerMultipleHalf("", i); //not used for ranking questions
    }

    //this should be used for matching questions
    answerMultiple(String left, String right, int i) {
        this.answerIndex = i;
        this.leftAnswer = new answerMultipleHalf(left, i);
        this.rightAnswer = new answerMultipleHalf(right, i);
    }

    @Override
    public void print(output out) {
        out.printOut("L input: ", this.leftAnswer.getField()); out.newLine();
        out.printOut("R input: ", this.rightAnswer.getField()); out.newLine();
        out.printOut("Index #: ",Integer.toString(this.answerIndex)); out.newLine();
    }

    public answerMultipleHalf getLeftAnswer() {
        return this.leftAnswer;
    }

    public answerMultipleHalf getRightAnswer() {
        return this.rightAnswer;
    }

    public String getLeftField() {
        return this.leftAnswer.getField();
    }
    public String getRightField() {
        return this.rightAnswer.getField();
    }

    public int getAnswerIndex() {
        return this.answerIndex;
    }

    public void setLeftAnswer(String left) {
        this.leftAnswer.setField(left);
    }

    public void setRightAnswer(String right) {
        this.rightAnswer.setField(right);
    }

    public void setAnswerIndex(int i) {
        this.answerIndex = i;
        this.leftAnswer.setIndex(i);
        this.rightAnswer.setIndex(i);
    }
}
