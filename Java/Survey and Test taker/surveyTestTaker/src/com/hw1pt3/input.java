package com.hw1pt3;

public abstract class input {

    input() {

    }

    public abstract int parseIndexFromNumber(String inputNum);

    public abstract String getUnparsedLine(output out);

    public abstract String getLineFromUser(output out);

    public abstract int[] parseNumberList(int howManyAnswers, int numberOfAnswersUserCanPick, boolean mustMatchNumber, output out);

    public abstract int parsePositiveNumber(String s);

    public abstract <E> E loader(String path, output out);

    //this doesn't actually interact with the console or anything else specific; it only interacts with inputs and outputs
    public static int getUserIndexInput(int usrMin, int usrMax, input in, output out) {
        int b = -1;
        String response = "";
        while(b == -1) {
            response = in.getLineFromUser(out); //get user input
            b = in.parsePositiveNumber(response); //parse the input
            if(b == -1) {
                out.printOut("Please type a positive number.");
                out.newLine();
            }
            else if (b > usrMax|| b < usrMin) {
                out.printOut("Please type a number between ", Integer.toString(usrMin), " and ", Integer.toString(usrMax), ".");
                out.newLine();
                b = -1;
            }
        }//parsePositiveNumber will keep returning -1 until user types a number
        //userInput = indices of the chosen answers
        return (b - 1);
        }
}
