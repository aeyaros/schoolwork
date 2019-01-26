/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * File output class
 * Output a list of lines to a text file
 * * * * * * * */

import java.io.BufferedWriter;
import java.io.FileWriter;

public class OutputFile extends Output {
    @Override
    public void doOutput(LineStorage output, String pathArg, String delimeter) {
        StringBuilder outputStringBuilder = new StringBuilder();
        for(int i = 0; i < output.getSize(); i++) { //for ith line
            for(int j = 0; j < output.getLine(i).getSize(); j++) { //for jth word in ith line
                outputStringBuilder.append(output.getLine(i).getWord(j).getText());

                if(j != output.getLine(i).getSize() - 1) { //if not the last word, print space
                    outputStringBuilder.append(delimeter);
                }
            }
            //print newline character at end of line if not the last line
            if(i != output.getSize() - 1) {
                outputStringBuilder.append("\n");
            }
        }
        try {
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(pathArg));
            System.out.print("Writing file...\n");
            writer.write(outputStringBuilder.toString());
            System.out.print("File written.\n");
            writer.close();
        } catch (Exception e) {
            System.err.print("\n=====================================================\n");
            System.err.print("ERROR - I/O Exception. Couldn't write to output file.\n");
            System.err.print("=====================================================\n");
            e.printStackTrace();
        }
    }
}
