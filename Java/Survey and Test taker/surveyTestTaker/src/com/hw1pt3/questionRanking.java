package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class questionRanking extends questionMatching implements Serializable {

    public questionRanking(input in, output out) {
        super(in, out, "Enter a series of answers which the user can rank.");
    }

    protected questionRanking(input in, output out, String instructions) {
        super(in, out, instructions);
    }

    @Override
    public void addAnswer(input in, output out, int index) {
        out.printOut("Please enter answer #", Integer.toString(index + 1), ":");
        out.newLine();
        String tempLeft = in.getLineFromUser(out); out.newLine();
        this.answerList.add(new answerMultiple(tempLeft, index));
    }


    //array of answers to show to user
    //scramble the left column; the right column is not used
    //when we add answers, we use constructor for answerMultiple that doesnt add info to
    // the right answerMultipleHalf inside the answerMultiple


    //only need use this after the question has been created, or has just been edited
    public void scrambleDisplayArray() {
        answerMultipleHalf[][] tempArray;

        int n = this.answerList.size(); //how many answers

        tempArray = new answerMultipleHalf[n][2]; //temporary array
        for (int i = 0; i < n; i++) { //make everything null
            tempArray[i][0] = null;
            tempArray[i][1] = null;
        }

        //only working with the left column and leftAnswer here
        for(int i = 0; i < n; i++) { //for each answer[i]

            //get a random unused x between 0 and number of answers
            int x = ThreadLocalRandom.current().nextInt(n);

            //x is now an random index between 0 and n-1

            //look for a row where the left column hasn't been filled yet
            int b = 0;
            while(b == 0) {
                if(tempArray[x][0] == null) { //if we find an open spot
                    //then put right into the xth row of the right column
                    tempArray[x][0] = this.answerList.get(i).getLeftAnswer();
                    b = 1; //break
                }
                else { //else lets try a different x
                    x = ThreadLocalRandom.current().nextInt(n);
                }
            }
        }
        //copy tempArray back to displayArray
        this.displayArray = tempArray;
    }

    //prints from answerList
    @Override
    public void print(output out) {
        out.printOut(this.prompt); out.newLine();

        int i = 1;

        for(answerMultiple a: this.answerList){
            out.printOut(Integer.toString(i),") ", a.getLeftField()); //print an answer
            out.newLine();
            i++;
        }
    }

    public void printWithNumbers(output out) {
        out.printOut(this.prompt); out.newLine();

        int i = 1;
        for(answerMultiple a: this.answerList){
            out.printOut(Integer.toString(i),") ", a.getLeftField()); //print an answer
            out.newLine();
            i++;
        }
    }

    @Override
    public void addResponses() {
        //convert the current permutation to a string and save to the savedResponses array

        String currentPermutation = "";

        for(int i = 0; i < this.userAnswers.length; i++) {
            //construct string with letter X) number N \n for each line
            currentPermutation += Integer.toString(this.userAnswers[i][0] + 1) + "\n";;
        }

        this.savedResponses.add(currentPermutation);
    }

    //prints from displayArray
    @Override
    public void printScrambled(output out) {
        out.printOut(this.prompt); out.newLine();

        int n = this.displayArray.length;

        for(int i = 0; i < n; i++) {
            out.printCapitalLetter(i);
            out.printOut(") ", this.displayArray[i][0].getField());
            out.newLine();
        }
    }

    @Override
    public void ask(input in, output out) {
        int n = this.answerList.size();
        out.printOut("Please rank the following answers from 1 to ", Integer.toString(n)); out.newLine();
        out.printOut("For each letter, type in it's ranking, followed by a comma. Only type numbers, commas, and spaces.");
        out.newLine();
        out.printOut("For example, type \"4, 5, 3, 1, 2 ...\" for letters A, B, C, D, E ..."); out.newLine(2);
        this.print(out);

        //get user input into an array of ints
        int[] input = in.parseNumberList(n,n,true, out);
        //answers to A,B,C,D,E...

        //first int is guess for A, then B, then C, etc.

        //for ranking, the correct indices are determined by the order in which the
        // answers were added into the answerList array
        //really, we are just adding 0, 1, 2, 3 into the array in numerical order
        int[] correctIndices;
        correctIndices = new int[n];
        for(int i = 0; i < n; i++) { //for each row
            correctIndices[i] = i; //the "correct index" is just where it is in the original list
        }


        //user chose answers in the order they thought they should be ranked
        //these are the indices in index[]
        //if user chose properly, then the order they chose should correspond
        // with the order in which they were originally added when the question was created
        //so, two columns
        //first: the indices user picked
        //second: 0 -> n-1 (aka indices for 1 to n)

        for(int i = 0; i < n; i++) {
            this.userAnswers[i][0] = input[i];      //user ranking (might be wrong; like 2,5,1,4,3...)
            userAnswers[i][1] = correctIndices[i];  //correct ranking (1,2,3,4,5...)

            //if they match, then user got it right
        }
    }

    //this will erase any stored answers and rescramble the displayArray
    @Override
    public void edit(input in, output out) {
        int b = 0;
        while(b == 0) {
            //print answers
            out.printOut("Please select an answer to edit: "); out.newLine();
            this.printWithNumbers(out);
            out.printOut("p) Change prompt"); out.newLine();
            out.printOut("x) Done editing"); out.newLine();

            //get input
            String input = in.getLineFromUser(out);

            //check if x or p
            if(input.equals("x")) {
                b = 1; //break main loop
            } else if(input.equals("p")) {
                out.printOut("Please type a new prompt, or x to cancel."); out.newLine();
                String s = in.getLineFromUser(out);
                if(s.equals("x")) { //dont set prompt
                } else {
                    this.prompt = s; //set new prompt
                }
            } else { //otherwise check number
                int index = in.parsePositiveNumber(input); //try converting input string to an int

                if(index > 0 && index <= answerList.size()) { //if input valid
                    index--; //subtract 1 to convert to an index

                    out.printOut("Please type the new answer text, or x to cancel."); out.newLine();
                    String newinput = in.getLineFromUser(out); //get the new answer text
                    if(newinput.equals("x")) { //if user cancels prompt edit, dont set prompt
                        out.printOut("Cancelling.");
                    } else {
                        answerList.set(index, new answerMultiple(newinput, index)); //change the answer
                    }
                } else { //input not a number and is invalid; maybe it was another letter; who knows...
                    out.printOut("Invalid input. Please try again.");
                    out.newLine();
                }


            }
        }
        //rescramble display array
        scrambleDisplayArray();
    }
}
