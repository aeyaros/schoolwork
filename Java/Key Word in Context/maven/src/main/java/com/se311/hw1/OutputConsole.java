/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Console output class
 * Output a list of lines to the console
 * * * * * * * */
public class OutputConsole extends Output {
    @Override
    public void doOutput(LineStorage output, String pathArg, String delimeter) {
        int numLines = output.getSize(); //number of lines

        for(int i = 0; i < numLines; i++) {
            //get number of words in current line
            int wordsInCurLine = output.getLine(i).getSize();

            //print the current line
            for(int j = 0; j < wordsInCurLine; j++) {
                //print current word in current line
                System.out.print(output.getLine(i).getWord(j).getText());

                //print space if item isn't last word
                if(j != wordsInCurLine - 1) {
                    System.out.print(delimeter);
                }
            }
            System.out.print("\n"); //go to next line
        }
    }
}