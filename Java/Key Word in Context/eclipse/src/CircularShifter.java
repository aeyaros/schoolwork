/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * This is basically the Circular shifting class
 * Takes a bunch of lines
 * for each line, where number of words in line is n, shift line n-1 times
 * returns list with all original lines and all shifted iterations of them
 * * * * * * * */

public class CircularShifter {

    public void shiftLines(LineStorage lineStorage) {
        //get size of original input
        int inputSize = lineStorage.getSize();

        //for each line in input, create circular shifted iterations of lines
        //append each new iteration back to line storage

        //for each line in the input
        for(int i = 0; i < inputSize; i++) {
            //get size of current line
            int currentSize = lineStorage.getLineSize(i);

            int currentIteration = i;
            int newIteration;
            //the number of new iterations is one less than the number of words
            for(int j = 0; j < currentSize - 1; j++) {
                //duplicate current line:
                //appends duplicate line to storage and returns index of duplicate
                newIteration = lineStorage.duplicateLine(currentIteration);

                //get text of first word in the line
                String first = lineStorage.getWordText(newIteration, 0);

                //remove first word from beginning of the new line
                lineStorage.removeWordFromLine(newIteration, 0);

                //append text from first word at the end of the new line
                lineStorage.addWordToLine(newIteration,first);

                //the new iteration we just created is now the current iteration for the next loop
                currentIteration = newIteration;
            }
        }
    }
}
