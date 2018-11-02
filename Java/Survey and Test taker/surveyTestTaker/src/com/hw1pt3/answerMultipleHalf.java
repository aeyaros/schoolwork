package com.hw1pt3;

import java.io.Serializable;

public class answerMultipleHalf extends answer implements Serializable {
    private String field;
    private int index;

    answerMultipleHalf(String f, int i) {
        this.field = f;
        this.index = i;
    }

    @Override
    public void print(output out) {
        out.printOut("Field: ", this.field); out.newLine();
        out.printOut("Index: ", Integer.toString(this.index)); out.newLine();
    }

    public String getField() {
        return this.field;
    }

    public int getIndex() {
        return this.index;
    }

    public void setField(String f) {
        this.field = f;
    }

    public void setIndex(int i) {
        this.index = i;
    }

}
