package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public class questionEssay extends Question implements Serializable {
    protected ArrayList<answerSimple> userAnswers;
    protected int numberOfResponses;

    questionEssay(input in, output out) {
        super();
        this.userAnswers = new ArrayList<>();
        this.numberOfResponses = 3; //as specified in our instructions for this assignment
        out.printOut("Please enter a prompt for this essay question:");
        out.newLine();
        this.prompt = in.getLineFromUser(out);
    }

    @Override
    public void addResponses() { //add user responses to an array of strings
        for(answerSimple a: this.userAnswers) {
            this.savedResponses.add(a.getUserInput());
        }
    }

    @Override
    public int clearPastResponses() {
        this.savedResponses.clear();
        this.userAnswers.clear();
        return 0;
    }

    @Override
    public int clearUserAnswers() {
        this.userAnswers.clear();
        return 0;
    }

    public int getNumberOfResponses() {
        return numberOfResponses;
    }

    @Override
    public void print(output out) {
        out.printOut(this.prompt); out.newLine();
    }

    @Override
    public void ask(input in, output out) {
        //print question
        this.print(out);
        out.printOut("Please enter ");
        if(this.numberOfResponses > 1) {
            out.printOut(Integer.toString(this.numberOfResponses), " responses to the question. When you finish each response, press enter.");
        }
        else {
            out.printOut("a single response. Then press enter to finish.");
        }
        out.newLine(2);

        for(int i = 1; i <= this.numberOfResponses; i++) {
            out.printOut("Response #", Integer.toString(i), ":"); out.newLine();

            String r = in.getLineFromUser(out);
            answerSimple tempAnswer = new answerSimple(r);
            userAnswers.add(tempAnswer);
            out.newLine();
        }
    }

    @Override
    public void edit(input in, output out) {
        out.printOut("The current prompt is:"); out.newLine();
        out.printOut(this.prompt); out.newLine();
        out.printOut("Press e to edit the prompt or x to cancel."); out.newLine();

        int b = 0;
        while(b == 0) {
            String request = in.getLineFromUser(out);
            if (request.equals("e")) { //edit
                out.printOut("Type a new prompt:"); out.newLine();
                String p = in.getLineFromUser(out);
                this.prompt = p; out.newLine();
                out.printOut("Prompt changed to ", p);
                out.newLine();
                break;
            } else if (request.equals("x")) { //exit
                break;
            }
            else {
                out.printOut("Please type e or x"); out.newLine();
            }
        }
    }
}
