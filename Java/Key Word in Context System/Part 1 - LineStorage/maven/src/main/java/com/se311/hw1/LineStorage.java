/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Line storage class
 * Holds an array of lines, with functions to retrieve, add, remove, and edit lines
 * * * * * * * */

import java.util.ArrayList;
import java.util.Collections;

public class LineStorage {
    private ArrayList<Line> lines;

    /*** constructors ***/

    LineStorage() {
        this.lines = new ArrayList<>();
    }

    /* LineStorage(ArrayList<Line> newLines) {
        this.lines = newLines;
    } */

    /*** modifiers ***/

    /* add stuff */

    //add a new line
    public void addNewLine() {
        this.lines.add(new Line());
    }

    //add a word to a line
    public void addWordToLine(int lineIndex, String newText) {
        this.lines.get(lineIndex).appendWord(new Word(newText));
    }

    /* //append a new line to the stored lines
    public void appendLine(Line line) {
        this.lines.add(line);
    } */

    /* remove stuff */

    /* //remove a line
    public void removeLine(int i) {
        this.lines.remove(i);
    } */

    //remove a word from a line
    public void removeWordFromLine(int lineIndex, int wordIndex) {
        this.lines.get(lineIndex).removeWord(wordIndex);
    }

    /* more operations */

    //swap two lines, based on their indices
    public void swapLines(int i, int j) {
        Collections.swap(this.lines, i, j);
    }

    /* public void setWordInLine(int lineIndex, int wordIndex, String newText) {
        this.lines.get(lineIndex).setWord(wordIndex,newText);
    } */

    //duplicate an existing line
    public int duplicateLine(int lineIndex) {
        //Line newLine = new Line(this.lines.get(lineIndex));
        //this.lines.add(newLine);

        this.lines.add(new Line(this.lines.get(lineIndex)));
        return this.lines.size() - 1;
    }

    /*** accessors ***/

    //get a specific line
    public Line getLine(int i) {
        return this.lines.get(i);
    }

    //how many lines?
    public int getSize() {
        return this.lines.size();
    }

    //how many words in the line?
    public int getLineSize(int i) {
        return this.lines.get(i).getSize();
    }

    //get text of a word in a line
    public String getWordText(int lineIndex, int wordIndex) {
        return this.lines.get(lineIndex).getWord(wordIndex).getText();
    }

    //get the entire line as a string
    public String getLineText(int lineIndex) {
        return this.lines.get(lineIndex).getString();
    }

}