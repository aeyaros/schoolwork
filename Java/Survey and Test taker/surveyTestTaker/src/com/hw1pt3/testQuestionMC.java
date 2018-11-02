package com.hw1pt3;

import java.io.Serializable;

public class testQuestionMC extends testQuestionMMC implements Serializable, testQuestion {
    testQuestionMC(input in, output out) {
        super(in, out);
    }


    //asking only allows user to select a single answer
    //same as questionMultiple, but I can't inherit this from there :/
    @Override
    public void ask(input in, output out) {
        //print out question
        this.printWithoutAnswers(out);
        out.newLine();
        //print instructions
        out.printOut("Type in the number of the answer you want:"); out.newLine();

        //get user input
        int b = -1;
        while(b == -1) {
            String response = in.getLineFromUser(out); //get user input
            b = in.parsePositiveNumber(response); //parse the input
            if(b == -1) {
                out.printOut("Please type a positive number.");
                out.newLine();
            } else if (b < 1 || b > this.answerList.size()) {
                out.printOut("Please enter a number between 1 and ", Integer.toString(this.userAnswers.size()));
                out.newLine();
                b = -1; //dont break loop
            } else {
                int index = b - 1;
                this.userAnswers.add(this.answerList.get(index));
            }
        }//parsePositiveNumber will keep returning -1 until user types a number
        //userInput = indices of the chosen answers


    }

    //user can only pick a single answer; check to see if it was correct
    @Override
    public double grade() {
        //is the answer the user chose a correct answer?
        if(this.userAnswers.get(0).getIsCorrect()) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
