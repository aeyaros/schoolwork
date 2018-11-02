package com.hw1pt3;

public abstract class output {

    output() {
    }

    public abstract void printOut(String... output);

    public abstract void printIndex(int index);

    public abstract void newLine();
    public abstract void newLine(int n);

    public abstract void printCapitalLetter(int i);

    public abstract <E> void saver(E thingToSave, String path, output out);

    public static String getCapitalLetter(int i) {
        if(i < 26){ //if i < 26, print a letter
            char C = (char) (65 + i); //get ascii character
            String L = Character.toString(C); //convert to string
            return L; //return the letter
        }
        else { //ASCII value too high to be capital letter
            int temp = i + 1; //convert from index
            return Integer.toString(temp); //just return the number
        }
    }

}
