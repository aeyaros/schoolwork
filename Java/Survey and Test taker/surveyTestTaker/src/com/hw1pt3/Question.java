package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Question implements Serializable {
    protected String prompt;
    protected ArrayList<String> savedResponses;

    Question() {
        this.savedResponses = new ArrayList<>();
    }

    public abstract void print(output out);

    public abstract void ask(input in, output out);

    public abstract void edit(input in, output out);

    public abstract int clearPastResponses(); //clears all answers from previous times the test was taken
    public abstract int clearUserAnswers(); //clears temporary answers
    public abstract void addResponses(); //add a response to the saved responses array, return 0 on success
    public void addResponseHelper(String s){
        this.savedResponses.add(s);
    }
    public ArrayList<String> getSavedResponses() { //get the saved responses as a list of strings
        return this.savedResponses;
    }

    public String getPrompt() {
        return this.prompt;
    }
}
