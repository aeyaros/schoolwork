//Andrew Yaros
//SE 311 HW 2
//ConsoleGenerator class
//Get user input from the console
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConsoleGenerator extends Filter {
    public ConsoleGenerator(Pipe input_, Pipe output_) {
        super(input_, output_);
    }

    //includes some code from my homework 1
    public void transform() {
        //the delimiter
        String delimiter = " ";

        printout("========================\n");
        printout("Input lines to be sorted\n");
        printout("========================\n");
        printout("Please input the data to be sorted, line by line.\n");
        printout("Type a blank line to finish!\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //we dont need linestorage anymore
        //we dont have to abstract words and lines anymore
        ArrayList<ArrayList<String>> lines = new ArrayList<>();

        String currentInput;
        int successfulLines = 0;
        boolean continueLoop = true;
        while(continueLoop) {
            printout("Please type a line, or press enter if finished:\n");
            try {
                //user types a line into console
                currentInput = reader.readLine();
            } catch(Exception e) {
                e.printStackTrace();
                printout("\n");
                continue;
            }

            //if typed line is blank
            if(currentInput.isEmpty()) {
                //if we have typed at least 1 line, then end
                if(successfulLines > 0) {
                    //break the loop
                    continueLoop = false;
                } else { //otherwise, display message and continue
                    printout("You must type at least one line.\n");
                }
            } else { //if the user has typed something
                //check to see if text is safe text
                //I'm limiting it to basic ASCII text right now
                if(currentInput.matches("[^A-Za-z0-9 ,.?!;:]")) {
                    printout("You may type A-Z, a-z, 0-9, spaces, commas and periods, question marks and exclamation points, semicolons and colons, and single and double quotes");
                } else { //if acceptable input
                    //create a tokenizer and count number of tokens
                    StringTokenizer tokenizer = new StringTokenizer(currentInput, delimiter);
                    int numWords = tokenizer.countTokens();

                    //create array of strings
                    ArrayList<String> currentLine = new ArrayList<>();

                    for(int i = 0; i < numWords; i++) {
                        currentLine.add(cleanUpText(tokenizer.nextToken(" ")));
                    }
                    //add the current line to the list of typed lines
                    lines.add(currentLine);
                    successfulLines++; //add 1 to successful line count
                }
            }
        }

        printout("\nReceived user input. Passing to next module...\n");
        //send lines from array to pipe
        for(int i = 0; i < lines.size(); i++) this.output_.put(lines.get(i));

        //send a "null terminator"
        //this causes the other filter to give an exception
        //the exception is caught by the other filter, which breaks the loop it uses to get new lines
        this.output_.put(null);
        //printout("Done with console input.\n");
        //stop the filter
        this.stop();
    }
}
