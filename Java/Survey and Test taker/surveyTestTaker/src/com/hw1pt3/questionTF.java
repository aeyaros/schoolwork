package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public class questionTF extends questionMultiple implements Serializable {
    questionTF(input in, output out) {
        this.savedResponses = new ArrayList<>();
        this.answerList = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        out.printOut("Please enter prompt:"); out.newLine();
        this.prompt = in.getLineFromUser(out);

        this.answerList.add(new answerChoice("False")); // 0 false
        this.answerList.add(new answerChoice("True"));  // 1 true
    }

    //print true before false...
    @Override
    public void print(output out) {
        //print the question, and each of the options
        out.printOut(this.prompt); out.newLine(2);
    }

    public void ask(input in, output out) {
        out.printOut(this.prompt); out.newLine();

        int b = 0;
        while(b == 0) {
            out.printOut("Type t for true or f for false:"); out.newLine();
            String s = in.getLineFromUser(out);

            if(s.equals("t")) {
                this.userAnswers.add(this.answerList.get(1)); //set user's answer to true
                b = 1;
            }
            else if(s.equals("f")) {
                this.userAnswers.add(this.answerList.get(0)); //set user's answer to false
                b = 1;
            }
            else {
                out.printOut("Please type t or f:"); out.newLine();
            }
            //else, ask again
        }

    }

    @Override
    protected void addAnswer(input in, output out) {
        //no need to use on this type of question; we always have the same two options
    }

    public void edit(input in, output out) {


        int b = 0;
        while(b == 0) {
            out.printOut("Press e to edit the prompt or x to cancel."); out.newLine();
            String request = in.getLineFromUser(out);

            if (request.equals("e")) { //edit
                out.newLine(2);
                out.printOut("Please type a new prompt for the true/false question.");
                out.newLine();
                String p = in.getLineFromUser(out);
                this.prompt = p;
                out.printOut("Prompt changed to ", p); out.newLine();
                b = 1;
            } else if (request.equals("x")) { //exit
                b = 1;
            }
            else {
                out.printOut("Invalid input. ");
                out.newLine();
            }
        }
    }
}
