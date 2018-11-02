package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class questionMatching extends Question implements Serializable {
    protected ArrayList<answerMultiple> answerList; //holds the original answers, unscrambled
    protected answerMultipleHalf[][] displayArray;  //determines how question will be printed and asked
    protected int[][] userAnswers; //left column: user inputs, right column, correct inputs

    questionMatching() {}

    public questionMatching(input in, output out) {
        this(in, out, "Please enter in some answers. Each has two parts: a left-column field and a right column field. \nRemember, the right column will be scrambled when the question is asked.");
    }

    protected questionMatching(input in, output out, String instructions) {
        super();
        this.answerList = new ArrayList<>();

        //set prompt
        out.printOut("Please enter a prompt:"); out.newLine();
        this.prompt = in.getLineFromUser(out);

        out.newLine(2);
        out.printOut(instructions);
        out.newLine(2);

        int b = 0; //for the following loop to get input
        int index = 0; //index of the answer, so items on the left can match items on the right because they share an index
        while(b == 0) {
            this.addAnswer(in, out, index);
            index++;
            int c = 0;
            while(c == 0) {
                out.printOut("Would you like to add another answer?");
                out.newLine();
                String tempResponse = in.getLineFromUser(out);
                if(tempResponse.equals("y")) {
                    c = 1; //user responded with a y/n, so break this loop
                }
                else if(tempResponse.equals("n")) {
                    b = 1; //no more questions, so break the outer loop
                    c = 1; //user responded with a y/n, so break this loop
                }
                else {
                    out.printOut("Please type y or n:"); out.newLine();
                }
            }
        }
        int rows = this.answerList.size();
        int columns = 2;
        this.displayArray = new answerMultipleHalf[rows][columns]; //array of type answermultiplehalf to display table

        scrambleDisplayArray();
        this.userAnswers = new int[rows][columns];
    }

    @Override
    public int clearPastResponses() {
        this.savedResponses.clear(); //clean up the past responses
        this.clearUserAnswers(); //clear out anything currently here
        return 0;
    }

    @Override
    public int clearUserAnswers() { //clear displayarray, and useranswers
        int rows = this.answerList.size();
        int cols = 2;
        this.displayArray = new answerMultipleHalf[rows][cols];
        this.userAnswers = new int[rows][cols];
        return 0;
    }



    @Override
    public void addResponses() {
        //convert the current permutation to a string and save to the savedResponses array

        String currentPermutation = "";

        for(int i = 0; i < this.userAnswers.length; i++) {
            //construct string with letter X) number N \n for each line
            currentPermutation += output.getCapitalLetter(i) + ") " + Integer.toString(this.userAnswers[i][0] + 1) + "\n";;
        }

        this.savedResponses.add(currentPermutation);
    }

    //index of the answer must be supplied; must be 1 greater than the highest index already added
    //currently only used in constructor; be careful if you use this in edit function
    public void addAnswer(input in, output out, int index) {
        out.printOut("Adding a new answer."); out.newLine();
        out.printOut("Enter a LEFT-column field:"); out.newLine();
        String tempLeft = in.getLineFromUser(out); out.newLine();
        out.printOut("Enter a RIGHT-column field:"); out.newLine();
        String tempRight = in.getLineFromUser(out); out.newLine();
        this.answerList.add(new answerMultiple(tempLeft, tempRight, index));
    }

    //array of half answers, with left column normal, and right column scrambled
    //only need use this after the question has been created, or has just been edited
    public void scrambleDisplayArray() {
        answerMultipleHalf[][] tempArray;

        int n = this.answerList.size(); //how many answers
        int columns = 2;

        tempArray = new answerMultipleHalf[n][columns]; //temporary array
        for (int i = 0; i < n; i++) { //make everything blank with index -1
            for(int j = 0; j < columns; j++) {
                tempArray[i][j] = new answerMultipleHalf("null", -1);
            }
        }

        for(int i = 0; i < n; i++) { //for each answer[i]

            //put left into the ith row of left column
            tempArray[i][0] = this.answerList.get(i).getLeftAnswer();

            //get a random unused x between 0 and number of answers
            int x = ThreadLocalRandom.current().nextInt(n);

            //x is now an random index between 0 and n-1

            //look for a row where the right column hasnt been filled yet
            int b = 0;
            while(b == 0) {
                if(tempArray[x][1].getIndex() == -1) { //if we find an open spot
                    //then put right into the xth row of the right column
                    tempArray[x][1] = this.answerList.get(i).getRightAnswer();
                    //note that we havent changed the index of the right half, so it will match up with something on the left half
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
        out.printOut(this.prompt); //prompt
        out.newLine();

        String customFormat = "%-" + Integer.toString(this.getMaxLeftColumnWidth()) + "s";

        int i = 1;
        for(answerMultiple a: this.answerList){
            String strL = String.format(customFormat, a.getLeftField());
            String strR = a.getRightField();

            out.printOut(Integer.toString(i), ") L: ", strL, " R: ", strR);
            out.newLine();
            i++;
        }


    }

    //when printing, go through options; find length of longest answer in left column
    public int getMaxLeftColumnWidth() {
        int max = 1; //variable for max width
        int i = 0; //counter variable
        for(answerMultiple a: this.answerList){
            int curLength = this.answerList.get(i).getLeftField().length();
            if(curLength > max) max = curLength;
            i++;
        }
        return max;
    }

    //print scrambled, for test taking, from displayArray
    public void printScrambled(output out) {
        out.printOut(this.prompt); //prompt
        out.newLine(2);

        this.scrambleDisplayArray();

        int n = this.displayArray.length; //number of questions/rows
        String maxL = Integer.toString(this.getMaxLeftColumnWidth());

        //print row by row
        for (int i = 0; i < n; i++) { //ith row
            String sl = this.displayArray[i][0].getField();

            String strL = String.format("%1$" + maxL + "s", sl);
            String strR = this.displayArray[i][1].getField();

            out.printCapitalLetter(i); //print a letter
            out.printOut(") ", strL, "  " , Integer.toString(i + 1), ") ", strR);
            out.newLine(); //line break
        }

    }

    @Override
    public void ask(input in, output out) {
        int n = this.displayArray.length;
        this.printScrambled(out);
        //print question
        out.newLine(2);
        out.printOut("Please match the letters from the left column with the numbers from the right column."); out.newLine();
        out.printOut("Type in the numbers that match A, B, C, D,... below."); out.newLine();
        out.printOut("Only type numbers, spaces and commas."); out.newLine();
        out.printOut("For example: \"4, 1, 7, 12, 8, ...\""); out.newLine(2);
        out.printOut("A, B, C, D, E, F, G etc...");
        out.newLine();

        int[] input = in.parseNumberList(n,n,true, out);
        //answers to A,B,C,D,E...
        //these are guesses to what the user thinks are the indices of the right column

        int[] correctIndices;
        correctIndices = new int[n];
        for(int i = 0; i < n; i++) { //for each row
            correctIndices[i] = this.displayArray[i][1].getIndex(); //get the actual index from the right column
        }

        for(int i = 0; i < n; i++) {
            this.userAnswers[i][0] = input[i];
            this.userAnswers[i][1] = correctIndices[i];
        }
        //user's responses, and correct responses, now stored
        //to grade, just ensure that userAnswers[i][0] = userAnswers[i][1]
    }

    //this will erase any stored answers and rescramble the displayArray
    @Override
    public void edit(input in, output out) {
        int b = 0;
        while(b == 0) {
            //print answers
            out.printOut("Please select an answer to edit: "); out.newLine();
            this.print(out);
            out.printOut("p) Change prompt"); out.newLine();
            out.printOut("x) Cancel"); out.newLine();

            String input = in.getLineFromUser(out);
            if(in.parsePositiveNumber(input) > 0 && in.parsePositiveNumber(input) <= answerList.size()) { //if user types a positive number
                //then edit that question
                int index = in.parsePositiveNumber(input) - 1;
                //ask user if they want to edit the left or right answers
                int c = 0;
                while (c == 0) {
                    out.printOut("Press l to edit the left column, r for the right, or x to cancel:"); out.newLine();
                    String s = in.getLineFromUser(out);
                    //get left and right strings
                    String leftC = this.answerList.get(index).getLeftField();
                    String rightC = this.answerList.get(index).getRightField();

                    if(s.equals("l")){
                        out.printOut("Type a new answer for the left column:"); out.newLine();

                        String newL = in.getLineFromUser(out);
                        answerList.set(index, new answerMultiple(newL, rightC, index));
                        c = 1;
                    } else if(s.equals("r")) {
                        out.printOut("Type a new answer for the left column:"); out.newLine();

                        String newR = in.getLineFromUser(out);
                        answerList.set(index, new answerMultiple(leftC, newR, index));
                        c = 1;
                    } else if(s.equals("x")) {
                        c = 1; //edit something else
                    } else {
                        out.printOut("Please type l, r, or x"); out.newLine();
                    }
                }
            } else if(input.equals("x")) {     //if user types x
                b = 1; //cancel; break main loop
            } else if(input.equals("p")) { //if user types p
                out.printOut("Please type a new prompt, or x to cancel."); out.newLine();
                String s = in.getLineFromUser(out);
                if(s.equals("x")) { //dont set prompt
                } else {
                    this.prompt = s; //set new prompt
                }
            }
        }
        scrambleDisplayArray();
    }
}
