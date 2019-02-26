//Andrew Yaros
//SE 311 HW 2
//Uppercase converter class
//Converts lines to uppercase; outputs either to console or file based on argument
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class UpperCaseConverter extends Filter {
    private boolean toFile_;
    private String outputPath_ = null;

    public UpperCaseConverter(Pipe input_, Pipe output_, boolean toFile, String outputPath) {
        super(input_, output_);
        this.toFile_ = toFile;
        if(toFile_) this.outputPath_ = outputPath; //set the file output if it is enabled
    }

    @Override
    protected void transform() {
        String delimiter = " ";
        //read lines from pipe until the pipe sends a null
        boolean shouldAppend = false; //dont append to file for the first line
        //that way, the existing file is overwritten
        //otherwise, if I run 5 times I'll get the same text in the file but 5 times in a row
        while(true) {
            try {
                //get from the pipe
                Object currentObject = input_.get();
                //when the generator finishes, it sends a null, which means that this throws an exception
                //the exception is caught below, which breaks this loop

                //cast to an arraylist
                ArrayList<String> currentLine = (ArrayList<String>)currentObject;

                //build a string from the current line
                StringBuilder sb = new StringBuilder();
                for(String s: currentLine) {
                    sb.append(s);
                    sb.append(delimiter);
                }
                String currentString = sb.toString(); //get string from the object
                currentString = currentString.toUpperCase(); //convert string to uppercase

                if(!toFile_) printout(currentString + "\n"); //print the string to the console
                else {
                    //export to file
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath_, shouldAppend));
                        writer.write(currentString + "\n"); //write the next line, and then a newline char
                        writer.close();
                        //for all lines after the first, we must append
                        shouldAppend = true;
                    } catch (Exception e) {
                        System.err.println("\nERROR - I/O Exception. Couldn't write to output file.");
                    }
                }
            } catch (Exception e) { //the generator sends a null when it is done
                break;
            }
        } //printout("\nDone converting.");

        //stop this filter
        this.stop();
    }
}
