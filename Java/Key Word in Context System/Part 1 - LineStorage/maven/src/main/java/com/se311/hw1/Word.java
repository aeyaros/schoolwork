/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Word class
 * very self explanatory... really just a string
 * * * * * * * */
public class Word {
    private String text;

    //constructor
    public Word(String wordText) {
        this.text = wordText;
    }

    //accessor
    public String getText() {
        return this.text;
    }
}
