//Andrew Yaros
//SE 311 HW 2
//CircularShifter class
//Circular shift each line
import java.util.ArrayList;

public class CircularShifter extends Filter{
    public CircularShifter(Pipe input_, Pipe output_) {
        super(input_, output_);
    }

    @Override
    protected void transform() {
        //get lines from input until exception
        while (true) {
            try {
                //get current line
                Object currentObject = input_.get();
                if(currentObject == null) break;

                ArrayList<String> currentline = (ArrayList<String>)currentObject;

                //first, output the original line
                output_.put(currentObject);

                //for this line, do circular shift and output the original line, plus shifted variations

                int wordsInLine = currentline.size();
                //number of iterations of line = number of words in line - 1
                for(int i = 0; i < wordsInLine-1; i++) {
                    //make new line
                    ArrayList<String> nextIteration = new ArrayList<>(currentline);

                    //remove first word from beginning
                    String firstWord = nextIteration.get(0);
                    nextIteration.remove(0);

                    //add first word to end of next iteration
                    nextIteration.add(firstWord);

                    //printout("Shifting word: " + firstWord+ "\n" );

                    //output the next iteration to the pipe
                    output_.put(nextIteration);

                    //update the current iteration
                    currentline = new ArrayList<>(nextIteration);
                }
            } catch (Exception e) {
                break; //once we are done getting input, break loop
            }
        }

        output_.put(null);
        //printout("Done shifting.\n");
        //stop the filter
        this.stop();
    }
}
