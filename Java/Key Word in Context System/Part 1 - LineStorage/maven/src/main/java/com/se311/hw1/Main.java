/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Main class
 * i.e. master control program
 * * * * * * * */

public class Main {
    public static void main(String[] args) {
        //create modules
        Input inputModule = new InputConsole(); //input
        Output outputModule = new OutputConsole(); //output
        LineStorage lineStorage = new LineStorage(); //line storage
        CircularShifter circularShifter = new CircularShifter(); //circular shifter
        AlphabeticalSorter alphaSorter = new AlphabeticalSorter(); //alphabetical sorter

        Output.printout("+--------------+\n");
        Output.printout("| Andrew Yaros |\n");
        Output.printout("| SE 311 HW #1 |\n");
        Output.printout("+--------------+\n\n");

        final String space = " ";
        final String EMPTY_PATH = "";
        String inputPath = EMPTY_PATH;
        String outputPath = EMPTY_PATH;

        /* * * * * * * * * * * * * * * * * * * * *
        ==========================================
        * to keep it simple, we can configure I/O
          methods via command line arguments

        * list of arguments
          1. use file input? "true" or "false"
          2. input file path or, if 1 is false, type "none"
          3. use file output? "true" or "false"
          4. output file path or, if 3 is false, type "none"
        ==========================================
        * * * * * * * * * * * * * * * * * * * * */

        if(args.length == 4 && args[0].equals("true")) {
            inputModule = new InputFile(); //change input module
            inputPath = args[1]; //set input path
            Output.printout("Using file input.\n");
        } else { Output.printout("Using console input.\n"); }

        if(args.length == 4 && args[2].equals("true")) {
            outputModule = new OutputFile(); //change output module
            outputPath = args[3]; //set output path
            Output.printout("Using file output.\n\n");
        } else { Output.printout("Using console output.\n\n"); }

        //get lines from input module and put into line storage
        inputModule.getInput(inputPath, lineStorage, space);

        //perform circular shifting on line storage
        circularShifter.shiftLines(lineStorage);

        //perform alphabetizing operation on line storage
        alphaSorter.alphabetize(lineStorage);

        //get lines from line storage for output
        outputModule.doOutput(lineStorage, outputPath, space);

        Output.printout("\n...Done.\n:D\n\n");
    }
}