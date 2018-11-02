package com.hw1pt3;

import java.io.Serializable;

public class answerSimple extends answer implements Serializable {
    private String userInput;

    answerSimple(String text) {
        this.userInput = text;
    }

    @Override
    public void print(output out) {
        out.printOut("Text: ", this.userInput); out.newLine();
    }

    public String getUserInput() {
        return this.userInput;
    }

    public void setUserInput(String text) {
        this.userInput = text;
    }
}
