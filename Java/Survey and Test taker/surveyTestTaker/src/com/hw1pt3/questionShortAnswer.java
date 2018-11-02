package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public class questionShortAnswer extends questionMultipleMultipleChoice implements Serializable {
    questionShortAnswer() {
    }

    questionShortAnswer(input in, output out) {
        this.savedResponses = new ArrayList<>();
        this.answerList = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        out.printOut("Please enter prompt:");
        out.newLine();
        this.prompt = in.getLineFromUser(out);
    }

    @Override
    protected void addAnswer(input in, output out) {
        out.printOut("Please type an acceptable answer:" ); out.newLine();
        String anAnswer = in.getLineFromUser(out);
        this.answerList.add(new answerChoice(anAnswer));
        out.printOut("Added answer ");
        out.printOut(anAnswer); out.newLine();
    }

    public void printWithoutAnswers(output out) {
        //print prompt
        //dont print answers
        out.printOut(this.prompt);
        out.newLine(2);
    }

    @Override
    public void ask(input in, output out) {
        this.printWithoutAnswers(out);
        out.printOut("Please type your answer:"); out.newLine();
        String response = in.getLineFromUser(out); //user types in a response
        this.userAnswers.add(new answerChoice(response)); //add as a new answer
    }

    /*@Override
    public void edit(input in, output out) {
        //this was crashing when I typed a number
        //I'm overriding it :/
        int b = 0;
        while(b == 0) {
            this.print(out);
            out.printOut("Choose an answer, or press p to edit the prompt or x to finish.");
            out.newLine();
            String input = in.getLineFromUser(out);




        }


        //print prompt
        //print answers
        //ask if they want to change answers

        //
    }*/
}
