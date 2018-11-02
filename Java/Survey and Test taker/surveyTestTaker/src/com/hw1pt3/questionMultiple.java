package com.hw1pt3;

import java.io.Serializable;

public class questionMultiple extends questionMultipleMultipleChoice implements Serializable {

    questionMultiple() {
        //for initializing arrays :P
    }

    questionMultiple(input in, output out) {
        super(in, out);
    }

    //asking only allows user to select a single answer
    @Override
    public void ask(input in, output out) {
        //print out question
        this.print(out);
        out.newLine();
        //print instructions
        out.printOut("Type in the number of the answer you want");
        out.newLine();
        out.printOut("Enter your answer below: ");
        out.newLine(2);

        //get user input
        int b = -1;
        while (b == -1) {
            String response = in.getLineFromUser(out); //get user input
            b = in.parsePositiveNumber(response); //parse the input
            if (b == -1) {
                out.printOut("Please type a positive number.");
                out.newLine();
            } else if (b < 1 || b > answerList.size()) {
                out.printOut("Please enter a number between 1 and ", Integer.toString(answerList.size()));
                out.newLine();
                b = -1; //dont break loop
            } else {
                int index = b - 1;
                this.userAnswers.add(this.answerList.get(index));
            }//parsePositiveNumber will keep returning -1 until user types a number
            //userInput = indices of the chosen answers
        }
    }
}
