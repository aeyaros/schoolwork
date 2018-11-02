package com.hw1pt3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Integer.parseInt;

public class inputConsole extends input {

    inputConsole() {

    }

    @Override
    public int parseIndexFromNumber(String inputNum) {
        try{
            return parseInt(inputNum) - 1;

        }catch(Exception e) {
            return -1;
        }
    }

    public String getUnparsedLine(output out) {
        BufferedReader getLine = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        int b = 0;
        while(b == 0) {
            //keep trying to get input until it is valid
            try {
                s = getLine.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //if empty
            if (s.isEmpty()) {
                out.printOut("You typed an empty line. Please try again:"); out.newLine();
            }
            else {
                b = 1;
            }
        }
        return s;
    }

    @Override
    public String getLineFromUser(output out) {
        BufferedReader getLine = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        int b = 0;
        while(b == 0) {
            //keep trying to get input until it is valid
            try {
                s = getLine.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //if empty or if anything other than letters, numbers, commas, spaces
            if ((s.isEmpty()) || ( s.matches("[^A-Za-z0-9,.?! ]"))) {
                out.printOut("Please type letters, numbers, spaces, periods, ?, or !. No other characters allowed.");
                out.newLine(); out.printOut("Please try again:"); out.newLine();
            }
            else {
                b = 1;
            }
        }

        return s;
    }

    @Override
    public int[] parseNumberList(int howManyAnswersAreThere, int numberOfAnswersUserCanPick, boolean mustMatchNumber, output out) {

        ArrayList<Integer> finalArray = new ArrayList<>(); //to be returned


        int a = 0; //outer loop
        int b = 0; //inner loop

        //if input is valid, b is set to 1
        //otherwise, b is set to 2 and the loop is broken
        //at the end of the loop we check if b is 2 to know whether input was invalid, or not invalid

        while(a == 0) { //this is the outer loop, for checking the completed parsed array of inputs
            b = 0;
            while (b == 0) { //this inner loop is for checking each of the inputs
                String input = this.getLineFromUser(out); //get the input!!!

                ArrayList<Integer> numArray = new ArrayList<>(); //to be used temporarily

                //parse the input
                String[] inputArray = input.split(" *, *"); //split line into array
                if (inputArray.length > howManyAnswersAreThere) { //check the length of array
                    b = 2; //user typed too many answers, so we will need to restart
                    out.printOut("You entered too many answers.");
                    out.newLine();

                } else { //length ok, continue, check each item in array

                    numArray = new ArrayList<>();//reset numarray
                    for (int i = 0; i < inputArray.length; i++) { //check each item in array; if any item is wrong then break this loop and restart
                        if (inputArray[i].matches("[0-9]+")) { //if the current item consists only numbers
                            Integer newInt; // initialize placeholder integer object
                            try {
                                newInt = parseInt(inputArray[i]); //parse current item as an integer object
                            } catch (Exception e) {
                                out.printOut("Only type numbers, commas, or spaces. Don't leave an empty space between two commas.");
                                //System.err.print("Could not parse int " + inputArray[i]);
                                out.newLine();
                                b = 2; //if we are here, input was somehow invalid
                                break; //and if input invalid, then we need to break this loop
                            }

                            //if we are here, we parsed the integer
                            //now, we need to ensure it is greater than 0 and less than the max number of answers
                            int val = newInt.intValue(); //convert integer object to int primitive
                            if (val > 0 && val <= howManyAnswersAreThere) { //user must type from 1 to maxnumofanswers
                                numArray.add(newInt); //if so, then input is valid!
                                b = 1; //set b to 1 so loop is eventually broken
                            } else {
                                b = 2; //number is out of range
                                out.printOut("Error; there are only ", Integer.toString(howManyAnswersAreThere), " answers, but you typed in a ", newInt.toString(), "!");
                                out.newLine();
                                break; //break the for loop
                            }
                        } else { //otherwise, input is invalid!
                            b = 2; //there is an error
                            out.printOut("Type numbers seperated by commas. No letters or special characters");
                            out.newLine();
                            break; //this item is wrong, so this entire for loop needs to end
                        }
                    }
                }
                //done checking array
                if (b == 2) { //if b was set to 2, then input invalid, loop was broken, so try again
                    b = 0; //set b back to 0 so loop continues
                } else {//if b is set to 1, then input was valid and loop wasn't broken
                    finalArray = numArray; //we have a finished array to be returned

                    //if we are here, input should be valid

                    //checking length of the array of input... how many answers did user type?
                    //we either want exactly as many answers as the question has, or a subset of them

                    //check for duplicates by creating a set
                    HashSet<Integer> testSet = new HashSet<>(finalArray);

                    //if number of items in set is less than total number of item, then there must be duplicates
                    //if not, then there are none

                    if(!(testSet.size() < finalArray.size())) { //if no duplicates...
                        if (mustMatchNumber) { //if we want as many answers as questions
                            if (finalArray.size() == numberOfAnswersUserCanPick) {
                                a = 1; //there are exactly as many numbers as we need; break loop
                                break;
                            } else {
                                out.printOut("Please type in exactly ", Integer.toString(numberOfAnswersUserCanPick), " answers.");
                                out.newLine();
                            }
                        } else { //as long as we choose at least one answer, and at most the total number of answers
                            if (finalArray.size() <= numberOfAnswersUserCanPick && finalArray.size() > 0) {
                                a = 1; //number of items in list is in range; break loop
                                break;
                            } else {
                                out.printOut("You must type between 1 and ", Integer.toString(numberOfAnswersUserCanPick), " answers.");
                                out.newLine();
                            }
                        }
                    }
                    else { //there are duplicates
                        out.printOut("Please try again; make sure you don't type the same number twice.");
                        out.newLine();
                    }

                }
            }//end b loop; if input was valid, then b is set to 1 and loop ends


        }//end a loop



        //return array now has an array of numbers greater than 0
        //subtract 1 from each number to get an array of index values
        for(int i = 0; i < finalArray.size(); i++) {
            Integer newVal = new Integer( finalArray.get(i).intValue() - 1); //finalArray[i]--
            finalArray.set(i, newVal);
        }

        //convert to int[] and return the int[]
        int[] returnArray;
        returnArray = new int[finalArray.size()] ;

        for(int i = 0; i < finalArray.size(); i++) {
            returnArray[i] = finalArray.get(i).intValue();
        }

        //return an array of index values!!!
        return returnArray;
    }

    @Override
    //return an int if string is a positive number
    //else return -1
    public int parsePositiveNumber(String s) {
        try {
            int num = parseInt(s); //try to parse the string
            if(num > 0){ //check if positive
                return num; //if positive, return the number
            } else return -1; //otherwise it is negative, return -1
        } catch (Exception e) {
            return -1; //error, return -1
        }
    }

    @Override
    public <E> E loader(String path, output out) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            E loadedObject = (E) ois.readObject();
            ois.close();
            fis.close();
            return loadedObject;
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printOut("Error; could not load file.");
            out.newLine();
            return null;
        }
    }
}
