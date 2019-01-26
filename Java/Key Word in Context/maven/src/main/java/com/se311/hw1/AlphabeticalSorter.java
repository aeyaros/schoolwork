/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Alphabetical sorting class
 * Takes a list of lines, sorts them by the first word of the line
 * * * * * * * */

public class AlphabeticalSorter {
    public void alphabetize(LineStorage lineStorage) {
        //sorting is done using a simple insertion sort
        //lines are sorted based on the text of the first word

        //insertion sort algorithm
        int i = 1, j;
        while(i < lineStorage.getSize()) {
            j = i;
            //while j > 0, and line j-1 is > line j
            //important to check if j > 0 before making the comparision
            //otherwise it wont alphabetize correctly
            while (j > 0 && compareLines(lineStorage, j-1, j) > 0) {
                //swap lines j and j-1, then decrement j
                lineStorage.swapLines(j, j-1);
                j--;
            } i++; //move to next item to compare
        }
    }

    //compare two lines as lowercase text
    private int compareLines(LineStorage lineStorage, int first, int second) {
        String firstString = lineStorage.getLineText(first).toLowerCase();
        String secondString = lineStorage.getLineText(second).toLowerCase();
        return firstString.compareTo(secondString);
    }
}
