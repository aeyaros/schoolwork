package com.hw1pt3;

import java.io.Serializable;

public class testQuestionMMC extends questionMultipleMultipleChoice implements Serializable, testQuestion {
    testQuestionMMC(input in, output out) {
        super(in, out);
    }


    @Override
    public void ask(input in, output out) {
        //print out question
        this.printWithoutAnswers(out);
        out.newLine();
        int numCorrect = this.getNumberOfCorrectAnswers();

        if(numCorrect > 1) {
            out.printOut("Please pick ", Integer.toString(numCorrect) , " choices.");
        } else {
            out.printOut("Please pick a choice.");
        }

        out.printOut(" Type in the numbers of the answers you want, seperated by commas (\",\")."); out.newLine();
        out.printOut("Do not type spaces, letters, or any other character; use numbers and commas only.");
        out.newLine(2);

        //get user input; array with size of the number of correct
        int[] userInput = in.parseNumberList(this.answerList.size(), numCorrect, true, out);

        //userInput = indices of the chosen answers

        for(int i = 0; i < userInput.length; i++) {
            //userInput: list of indices of answers to add
            //userInput[i] current index to reference
            //get the item at that index and add it to the chosen answers
            this.userAnswers.add(this.answerList.get(userInput[i]));
        }
        //answers now added
    }

    //override since we have to get whether answers are true or not
    @Override
    protected void addAnswer(input in, output out) {
        out.printOut("Please type an answer"); out.newLine();
        String anAnswer = in.getLineFromUser(out);
        out.newLine();
        out.printOut("Is this answer a correct answer?"); out.newLine();
        boolean iscorrect = false;
        int b = 0;
        while(b == 0) {
            String yorn = in.getLineFromUser(out);
            if(yorn.equals("y")) {
                iscorrect = true;
                b = 1;
            }
            else if(yorn.equals("n")) {
                b = 1;
            }else {
                out.printOut("Please type y or n."); out.newLine();
            }
        }
        this.answerList.add(new answerChoice(anAnswer, iscorrect));
        if(iscorrect) {
            out.printOut("Added correct answer ", anAnswer); out.newLine();
        } else {
            out.printOut("Added incorrect answer ", anAnswer); out.newLine();
        }
    }

    @Override
    public void print(output out) { //print with correct answers shown
        super.print(out);

        int n = 0;
        for(answerChoice a : this.answerList) { if(a.getIsCorrect()) { n++; } } //are there one or more correct answers
        if(n > 1) { out.printOut("The correct answers are: "); out.newLine(); }
        else { out.printOut("The correct answer is: "); out.newLine(); }

        int i = 1;
        for(answerChoice a: this.answerList) {
            if(a.getIsCorrect()) {
                out.printOut(Integer.toString(i),") ", a.getAnswerText());
                out.newLine();
                i++;
            }
        }
    }

    public void printWithoutAnswers(output out) {
        super.print(out);
    }

    public void edit(input in, output out) { //when user adds answer, they are asked if it is a correct answer
        out.printOut("Editing the following question:"); out.newLine();
        this.print(out);
        out.newLine();

        int b = 0;
        while(b == 0) {
            out.printOut("Editing menu"); out.newLine();
            out.printOut("p) edit prompt"); out.newLine();
            out.printOut("x) exit"); out.newLine();
            out.printOut("...or type the number of the answer you want to change:");
            out.newLine();

            String request = in.getLineFromUser(out);
            int num = in.parsePositiveNumber(request);

            if(request.equals("p")) { //change prompt
                this.prompt = in.getLineFromUser(out);
                out.printOut("Changed prompt to ", this.prompt);
                out.newLine();
            }
            else if(request.equals("x")) { //exit
                b = 1;
            }
            else if (num != -1 && (num > 0 && num <= answerList.size())) { //change an answer
                out.printOut("You selected ", this.answerList.get(num).getAnswerText()); out.newLine();
                out.printOut("Please enter a new answer:"); out.newLine();
                String newAns = in.getLineFromUser(out);


                //is the new answer a correct answer?
                out.newLine();
                out.printOut("Is this a correct answer? Type y or n, or x to cancel.");
                out.newLine();
                int c = 0;
                while(c == 0) {
                    String isAtrue = in.getLineFromUser(out); out.newLine();

                    if(isAtrue.equals("y")) {
                        this.answerList.set(num, new answerChoice(newAns, true));
                        out.printOut("Changed answer to ", newAns, " which is true.");
                        out.newLine();
                    } else if(isAtrue.equals("n")) {
                        this.answerList.set(num, new answerChoice(newAns, false));
                        out.printOut("Changed answer to ", newAns, " which is false.");
                        out.newLine();
                    } else if(isAtrue.equals("x")) {
                        c = 1; //we want to break out of this loop, and maybe edit the prompt or a question.
                    } else {
                        out.printOut("Please type y, n, or x to cancel.");
                    }



                }

            } else {
                out.printOut("Please type p, x, or a number between 1 and ", Integer.toString(answerList.size()), ".");
                out.newLine();
            }
        }
    }

    @Override
    public double grade() {

        //We know how many answers are correct
        //User can only pick as many answers as there are correct ones
        //If all the answers the user chose are correct, then the question is correct

        for(answerChoice a: this.userAnswers) { //loop through answers
            if(!a.getIsCorrect()) { //if current answer is wrong, user picked a wrong answer
                return 0.0; //return grade of 0
            }
        }

        //if we are here, then all answers that were chosen are correct
        return 1.0;
    }

    //get number of answers that are correct
    public int getNumberOfCorrectAnswers() {
        int n = 0;
        for(answerChoice a: this.answerList) {
            if(a.getIsCorrect()) { n++; }
        }
        return n;
    }
}


