package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;

public class questionMultipleMultipleChoice extends Question implements Serializable {
    protected ArrayList<answerChoice> answerList;
    protected ArrayList<answerChoice> userAnswers;

    questionMultipleMultipleChoice() {
    }

    protected void addAnswer(input in, output out) {
        out.printOut("Please type an answer"); out.newLine();
        String anAnswer = in.getLineFromUser(out);
        this.answerList.add(new answerChoice(anAnswer));
        out.printOut("Added answer ", anAnswer); out.newLine();
    }

    questionMultipleMultipleChoice(input in, output out) {
        super();
        this.answerList = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        out.printOut("Please enter prompt:");
        out.newLine();
        this.prompt = in.getLineFromUser(out);

        int b = 0;
        while(b == 0) {
            addAnswer(in, out); //add an answer

            //ask user if they want to add more
            int c = 0;
            while (c == 0) {
                out.printOut("Add an additional answer? Type y or n");
                out.newLine();
                String response = in.getLineFromUser(out);
                if (response.equals("n")) {
                    b = 1; //break
                    c = 1; //and stop asking
                } else if (response.equals("y")) {
                    c = 1; //stop asking
                } else {
                    out.printOut("Please type y or n");
                    out.newLine();
                }
                //else, user did not type y or n so ask again :/
            }
        }
        //all done
    }

    @Override
    public int clearUserAnswers() {
        this.userAnswers.clear();
        return 0;
    }

    @Override
    public int clearPastResponses() {
        this.savedResponses.clear(); //clear old answers
        this.clearUserAnswers(); //clear any current answers
        return 0;
    }

    @Override
    public void addResponses() {
        //add answers to the array as strings
        //tabulate will take all the answers out and count how many times each appears
        for(answerChoice a: this.userAnswers) {
            this.addResponseHelper(a.getAnswerText());
            //this.savedResponses. //add(a.getAnswerText());
            //try {

            //} catch(Exception e) {
              //  e.printStackTrace();

            //}

        }
    }

    public String getAnAnswer(int index) {
        return this.answerList.get(index).getAnswerText();
    }

    public void print(output out) {
        //print the question, and each of the options
        out.printOut(this.prompt); out.newLine(2);
        int i = 0;
        for(answerChoice ans : this.answerList) {
            out.printIndex(i);
            out.printOut(") ", ans.getAnswerText());
            out.newLine();
            i++;
        }
    }

    public void ask(input in, output out) {
        //print out question
        this.print(out);
        out.newLine();

        int num = answerList.size();
        out.printOut("Please pick at least one of the ", Integer.toString(num), " items."); out.newLine();
        out.printOut("Type in the numbers of the answers you want, seperated by commas (\",\")."); out.newLine();
        out.printOut("Do not type spaces, letters, or any other character; use numbers and commas only.");
        out.newLine(2);

        //get user input; array with size of the number of correct
        int[] userInput = in.parseNumberList(num, num ,false, out);

        //userInput = indices of the chosen answers

        for(int i = 0; i < userInput.length; i++) {
            //userInput: list of indices of answers to add
            //userInput[i] current index to reference
            //get the item at that index and add it to the chosen answers
            this.userAnswers.add(this.answerList.get(userInput[i]));
        }
        //answers now added
    }



    public void edit(input in, output out) {
        int b = 0;
        while(b == 0) {
            out.printOut("Editing the following question, type a number, p, or x:"); out.newLine();
            this.print(out);
            out.printOut("p) Edit prompt"); out.newLine();
            out.printOut("x) Done editing"); out.newLine();

            out.newLine();

            String request = in.getLineFromUser(out); //get input

            //check if p or x

            if(request.equals("p")) { //change prompt
                out.printOut("Type a new prompt:"); out.newLine();
                this.prompt = in.getLineFromUser(out);
                out.printOut("Changed prompt to ", this.prompt);
                out.newLine();
            } else if(request.equals("x")) { //break loop
                b = 1;
            } else { //check if a number
                int num = in.parsePositiveNumber(request);
                if(num > 0 && num <= answerList.size()) {
                    num--;//convert to index
                    out.printOut("You selected ", this.answerList.get(num).getAnswerText()); out.newLine();
                    out.printOut("Please enter a new answer:"); out.newLine();
                    String newAns = in.getLineFromUser(out);
                    this.answerList.set(num, new answerChoice(newAns, this.answerList.get(num).getIsCorrect()));
                    out.printOut("Changed answer to ", newAns); out.newLine();
                } else {
                    out.printOut("Invalid input. Please try again.");
                    out.newLine(2);
                }
            }
        }
    }
}
