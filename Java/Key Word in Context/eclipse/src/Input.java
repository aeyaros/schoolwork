/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Abstract input class
 * Outputs lines from input to line storage
 * Contains additional function to get rid of punctuation
 * * * * * * * */

public abstract class Input {
    //get user input and move into a line storage module
    public abstract void getInput(String pathArg, LineStorage lineStorage, String delimiter);

    protected String cleanUpText(String oldText) {
        return oldText.replaceAll("[\\W!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]+", "");
    }
}
