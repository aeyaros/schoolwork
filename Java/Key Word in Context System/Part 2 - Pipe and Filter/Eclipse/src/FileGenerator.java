//Andrew Yaros
//SE 311 HW 2
//FileGenerator class
//Get user input from file
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileGenerator extends Filter {
    private String inputPath_;

    public FileGenerator(Pipe input_, Pipe output_, String inputPath) {
        super(input_, output_);
        this.inputPath_ = inputPath;
    }

    //includes some code from my homework 1
    public void transform() {
        String delimiter = " ";

        System.out.println("Using pathname " + this.inputPath_ + "\n");

        //create string to hold the entire input file
        String input = "";
        try { //use a scanner to get the contents of the file into a string
            Scanner inputScanner = new Scanner(new File(this.inputPath_));
            //use a delimiter that only matches the end of the file; this will give us the whole file
            input = inputScanner.useDelimiter("\\Z").next();
            inputScanner.close();
        } catch (Exception e) {
            System.err.println("Couldn't read " + this.inputPath_);
            System.exit(-100);
        }
        //if we are here, then there wasn't an exception

        //make sure not empty file
        if(input.isEmpty()) {
            System.err.print("Error: Empty file.");
            System.exit(-200);
        }

        //create an array for the line strings
        ArrayList<String> inputLines = new ArrayList<>();

        //next, separate the input string into multiple strings, one for each line
        //delimiter string regex - only match line terminators
        String lineDelim = "(\n|\r|\r\n|\u0085|\u2028|\u2029)";
        StringTokenizer inputLineTokenizer = new StringTokenizer(input, lineDelim); //create a string tokenizer

        //count number of lines
        int numberOfLines = inputLineTokenizer.countTokens();

        //add the lines to the array
        for(int i = 0; i < numberOfLines; i++) inputLines.add(inputLineTokenizer.nextToken(lineDelim));


        //split line into words

        //create arraylist of strings
        ArrayList<ArrayList<String>> lines = new ArrayList<>();

        for(int i = 0; i < inputLines.size(); i++) {
            //printout("Read line: " + inputLines.get(i) + "\n");

            //create a tokenizer and count number of tokens
            StringTokenizer tokenizer = new StringTokenizer(inputLines.get(i), delimiter);
            int numWords = tokenizer.countTokens();

            //create array of strings
            ArrayList<String> currentLine = new ArrayList<>();

            for(int j = 0; j < numWords; j++) { //add a word if it is valid
                String currentWord = cleanUpText(tokenizer.nextToken(delimiter));
                if(!currentWord.isEmpty()) currentLine.add(currentWord);
            }

            //add the current line to the list of typed lines if it had any valid words
            if(currentLine.size() > 0) lines.add(currentLine);
        }

        if(lines.size() <= 0) {
            System.err.println("ERROR! File did not contain a line with any valid words. Please try again with a different inpput file.");
            System.exit(-444);
        } else {
            //send lines from array to pipe
            for(int i = 0; i < lines.size(); i++) {
                this.output_.put(lines.get(i));
            }



            //send a null terminator?
            //this causes the other filter to give an exception
            //the exception is caught by the other filter, which breaks the loop it uses to get new lines
            this.output_.put(null);
            //printout("Done with file input.\n");
            //stop the filter
            this.stop();
        }


    }
}
