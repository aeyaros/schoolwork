/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Line class
 * A list of words. Duh
 * * * * * * * */
import java.util.ArrayList;

public class Line {
    private ArrayList<Word> words;

    public Line() { //default constructor
        this.words = new ArrayList<>();
    }

    public Line(Line line) { //copy constructor
        this.words = new ArrayList<>();
        //deep copy
        for(int i = 0; i < line.getSize(); i++) {
            this.words.add(new Word(line.getWord(i).getText()));
        }
    }

    /* //create a new line with words
    public Line(ArrayList<Word> inputWords) {
        this.words = new ArrayList<>();
        for (Word w: inputWords) { this.words.add(w); }
    } */

    /* //get all the words
    public ArrayList<Word> getWords() {
        return this.words;
    } */

    //get a specific word
    public Word getWord(int i) {
        return this.words.get(i);
    }

    //how many words?
    public int getSize() {
        return this.words.size();
    }

    //append a word
    public void appendWord(Word word) {
        this.words.add(word);
    }

    //remove a word at the ith spot
    public void removeWord(int i) {
        if(i >= 0 && i < this.words.size()) {
            this.words.remove(i);
        }
    }

    public String getString() {
        StringBuilder output = new StringBuilder();
        for(Word w: this.words) {
            output.append(w.getText());
        }
        return output.toString();
    }

    /* public void setWord(int i, String newText) {
        this.words.set(i, new Word(newText));
    } */

    /* //insert a word at the ith spot
    public void insertWord(Word word, int i) {
        if(i >= 0 && i < this.words.size()) {
            this.words.add(i, word);
        }
    } */
}
