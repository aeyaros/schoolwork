/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * File input class
 * used to input line information from a text file
 * uses string tokenizer objects to extract lines from a file
 * uses a delimiter to tokenize the words
 * * * * * * * */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;

public class InputFile extends Input {
    @Override
    public void getInput(String pathArg, LineStorage lineStorage, String delimiter) {
        try {
            //-----
            //first, get text from a file

            //use a scanner to get the contents of the file into a string
            File inputFile = new File(pathArg);
            Scanner inputScanner = new Scanner(inputFile);
            //use a delimiter that only matches the end of the file
            //this will give us the whole file
            String input = inputScanner.useDelimiter("\\Z").next();
            inputScanner.close();
            
            //-----
            //next, separate the input string into multiple strings, one for each line

            //delimiter string regex - only match line terminators
            String lineDelim = "(\n|\r|\r\n|\u0085|\u2028|\u2029)";

            //create a string tokenizer
            StringTokenizer inputLineTokenizer = new StringTokenizer(input, lineDelim);

            //create an array for the line strings
            ArrayList<String> inputLines = new ArrayList<>();

            //count number of lines
            int numberOfLines = inputLineTokenizer.countTokens();

            //add the lines to the array
            for(int i = 0; i < numberOfLines; i++) {
                inputLines.add(inputLineTokenizer.nextToken(lineDelim));
            }

            //-----
            //finally, separate each line into words

            //tokenize each line into words
            for(int i = 0; i < numberOfLines; i++) {
                //create a string tokenizer for the current line
                StringTokenizer currentTokenizer = new StringTokenizer(inputLines.get(i), delimiter);

                //get number of words in current line
                int wordsInCurLine = currentTokenizer.countTokens();

                //create a new line in line storage
                lineStorage.addNewLine();

                //add those words to the current line
                //i.e. tokenize the current line into words
                for(int j = 0; j < wordsInCurLine; j++) {
                    //get next word from file text
                    String nextWord = currentTokenizer.nextToken(delimiter);
                    //remove unwanted characters
                    String cleanedWord = cleanUpText(nextWord);
                    //if not empty, then we can add the word
                    if(!cleanedWord.isEmpty()) {
                        lineStorage.addWordToLine(i, cleanedWord);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("====================");
            System.err.println("Error reading input:");
            System.err.println("YOU MUST SPECIFY AN ");
            System.err.println("INPUT FILE!  (-_-)  ");
            System.err.println("====================");
            //e.printStackTrace();
            System.exit(-100);
        }
    }
}