package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public class testQuestionSA extends questionMultipleMultipleChoice implements Serializable, testQuestion {
    testQuestionSA(input in, output out)
    {
        this.answerList = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        out.printOut("Please enter prompt:");
        out.newLine();
        this.prompt = in.getLineFromUser(out);

        out.printOut("Please type in the correct answer.");
        out.newLine();

        //add correct answers one by one
        int b = 0;
        while(b == 0) {
            this.addAnswer(in, out); //add an answer

            //ask user if they want to add another answer
            out.printOut("Do you want to add additional correct answers? Type y or n");
            out.newLine();
            int c = 0;
            while(c == 0) {
                String response = in.getLineFromUser(out); //get response
                if(response.equals("n")) { //then no more answers
                    c = 1; //user typed in valid response
                    b = 1; //stop adding answers
                } else if(response.equals("y")) {
                    c = 1; //user typed in valid response, keep adding answers
                } else {
                    out.printOut("Please type y or n"); //user typed invalid response
                }
            }
        }
    }

    @Override
    public void ask(input in, output out) {
        out.printOut(this.prompt);
        out.newLine(2);
        out.printOut("Please type your answer:"); out.newLine();
        String response = in.getLineFromUser(out); //user types in a response
        this.userAnswers.add(new answerChoice(response)); //add as a new answer
    }

    @Override
    public void print(output out) {
        //print the question, and each of the options
        out.printOut(this.prompt); out.newLine();
        if(this.answerList.size() > 1) {
            out.printOut("The correct answers are:");
        } else {
            out.printOut("The correct answer is:");
        } out.newLine();
        int i = 0;
        for(answerChoice ans : this.answerList) {
            out.printIndex(i);
            out.printOut(": ", ans.getAnswerText());
            out.newLine();
            i++;
        }
    }

    @Override
    public double grade() {
        //compare user's answer to each of the answers in the answerList

        //loop through answers in answerList
        for(answerChoice a: this.answerList) {
            //does the text of the answer match what the user typed in?
            String userText = this.userAnswers.get(0).getAnswerText(); //users answer
            String ansrText = a.getAnswerText(); //current answer

            if(userText.equals(ansrText)) { //if strings match
                return 1.0;   //then user typed a correct answer; return grade of 1
            }
        }

        //if we reach this point, the users answer didnt match any of the possible answers
        return 0.0; //return grade of 0
    }
}
