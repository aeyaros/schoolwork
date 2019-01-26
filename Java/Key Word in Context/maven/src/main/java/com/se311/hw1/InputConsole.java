/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Console input class
 * get text input from the console
 * * * * * * * */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class InputConsole extends Input {
    @Override
    public void getInput(String pathArg, LineStorage lineStorage, String delimiter) {
        Output.printout("========================\n");
        Output.printout("Input lines to be sorted\n");
        Output.printout("========================\n");
        Output.printout("Please input the data to be sorted, line by line.\n");
        Output.printout("Type a blank line to finish!\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String currentInput;
        int successfulLines = 0;
        boolean continueLoop = true;
        while(continueLoop) {
            Output.printout("Please type a line, or press enter if finished:\n");
            try {
                //user types a line into console
                currentInput = reader.readLine();
            } catch(Exception e) {
                e.printStackTrace();
                Output.printout("\n");
                continue;
            }

            //if typed line is blank
            if(currentInput.isEmpty()) {
                //if we have typed at least 1 line, then end
                if(successfulLines > 0) {
                    //break the loop
                    continueLoop = false;
                } else { //otherwise, display message and continue
                    Output.printout("You must type at least one line.\n");
                }
            } else { //if the user has typed something
                //check to see if text is safe text
                //I'm limiting it to basic ASCII text right now
                if(currentInput.matches("[^A-Za-z0-9 ,.?!;:]")) {
                    Output.printout("You may type A-Z, a-z, 0-9, spaces, commas and periods, question marks and exclamation points, semicolons and colons, and single and double quotes");
                } else { //if acceptable input
                    //create a tokenizer and count number of tokens
                    StringTokenizer tokenizer = new StringTokenizer(currentInput, delimiter);
                    int numWords = tokenizer.countTokens();

                    //add a new line to the line storage, and add words to it
                    lineStorage.addNewLine();

                    for(int i = 0; i < numWords; i++) {
                        lineStorage.addWordToLine(lineStorage.getSize()-1, cleanUpText(tokenizer.nextToken(" ")));
                    }

                    successfulLines++; //add 1 to successful line count
                }
            }
        }

        Output.printout("\n===============================");
        Output.printout("\nReceived user input. Sorting...");
        Output.printout("\n===============================\n\n");
    }
}
