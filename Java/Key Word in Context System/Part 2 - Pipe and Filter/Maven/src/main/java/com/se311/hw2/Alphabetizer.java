//Andrew Yaros
//SE 311 HW 2
//Alphabetizer class
//Sorts lines in alphabetical order
import java.util.ArrayList;
public class Alphabetizer extends Filter{

    public Alphabetizer(Pipe input_, Pipe output_) {
        super(input_, output_);
    }

    @Override
    protected void transform() {
        //get lines from input until exception
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        while(true) {
            try {
                //get current line
                Object currentObject = input_.get();
                if(currentObject == null) break;

                //cast, then add to list
                ArrayList<String> currentLine = (ArrayList<String>)currentObject;
                lines.add(currentLine);

                //printout("Reading line to sort: ");
                //for(String s: currentLine) printout( " ");
                //printout("\n");
            } catch (Exception e) {
                break;
            }
        }
        //ensure we have non-zero amount of lines
        if(lines.size() > 0) {
            //sort lines
            alphabetize(lines);
            //send to output pipe
            for(int i = 0; i < lines.size(); i++) output_.put(lines.get(i));
        } else {
            System.err.print("ERROR: Possibly because alphabetizer not given any lines.");
            System.exit(-555);
        }

        //stop the filter
        output_.put(null);
        //printout("Done sorting.\n");
        this.stop();
    }

    private void alphabetize(ArrayList<ArrayList<String>> lines) {
        //sorting is done using a simple insertion sort
        //lines are sorted based on the text of the first word
        //insertion sort algorithm
        int i = 1, j;
        while(i < lines.size()) {
            j = i;
            //while j > 0, and line j-1 is > line j
            //important to check if j > 0 before making the comparision
            //otherwise it wont alphabetize correctly
            while (j > 0 && compareLines(lines, j-1, j) > 0) {
                //swap lines j and j-1, then decrement j
                swap(lines, j, j-1);
                j--;
            } i++; //move to next item to compare
        }
    }

    private void swap(ArrayList<ArrayList<String>> lines, int i, int j) {
        //get ith and jth strings
        ArrayList<String> oldI = new ArrayList<>(lines.get(i));
        ArrayList<String> oldJ = new ArrayList<>(lines.get(j));
        //set new jth string to the old ith string, and vice versa
        lines.set(j, oldI);
        lines.set(i, oldJ);
    }

    //compare two lines as lowercase text
    private int compareLines(ArrayList<ArrayList<String>> lineStorage, int first, int second) {
        String firstString = getLineText(lineStorage.get(first)).toLowerCase();
        String secondString = getLineText(lineStorage.get(second)).toLowerCase();
        return firstString.compareTo(secondString);
    }

    //concantenates strings in an arraylist of strings
    private String getLineText(ArrayList<String> line){
        StringBuilder builder = new StringBuilder(); //new StringBuilder
        for(String s: line) builder.append(s); //append each word to builder
        return builder.toString();
    }
}
