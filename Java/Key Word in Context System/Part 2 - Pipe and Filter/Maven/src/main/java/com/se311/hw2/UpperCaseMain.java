//Andrew Yaros
//SE 311 HW 2
//Main class
public class UpperCaseMain {

    public static void main(String args[]) {
        System.out.print("+--------------+\n");
        System.out.print("| Andrew Yaros |\n");
        System.out.print("| SE 311 HW #2 |\n");
        System.out.print("+--------------+\n\n");

        //use file input
        boolean useFileInput = false; //console input by defaut
        //use file output
        boolean useFileOutput = false; //console output by default
        //where to do the uppercase conversion/output
        int whereToStop = 3; //do all the stuff by default

        /* * * * * * * * * * * * * * * * * * * * *
        ==========================================
        * to keep it simple, we can configure I/O
          methods via command line arguments

        * list of arguments
          1. use file input? "true" or "false"
          2. input file path or, if 1 is false, type "none"
          3. use file output? "true" or "false"
          4. output file path or, if 3 is false, type "none"
          5. integer; where uppercase conversion occurs; set to:
             1 for after input
             2 for after shifting
             3 for after sorting
        ==========================================
        * * * * * * * * * * * * * * * * * * * * */

        //set variables from program arguments
        String inputPath = "";
        String outputPath = "";

        //input filename
        if(args.length == 5 && args[0].equals("true")) {
            useFileInput = true;
            inputPath = args[1]; //set input path
            System.out.print("Using file input.\n");
        } else { System.out.print("Using console input.\n"); }

        if(args.length == 5 && args[2].equals("true")) {
            useFileOutput = true;
            outputPath = args[3]; //set output path
            System.out.print("Using file output.\n\n");
        } else { System.out.print("Using console output.\n\n"); }

        if(args.length == 5) {
            try {
                whereToStop = Integer.parseInt(args[4]);
            } catch (Exception e) {
                //we are already using 3 by default if input is incorrect
                System.out.print(args[4] + " is an invalid integer for fifth argument (Should be 1, 2, or 3); defaulting to option 3.");
                whereToStop = 3;
            }
        }

        //create modules
        Pipe firstPipe = new PipeImpl(); //three pipes
        Pipe secondPipe = new PipeImpl();
        Pipe thirdPipe = new PipeImpl();
        Filter generator = new ConsoleGenerator(null, firstPipe); //the generator, which gets input
            if(useFileInput) generator = new FileGenerator(null, firstPipe, inputPath);
        Filter shifter; //the circular shifter
        Filter sorter; //the alphabetizer
        Filter converter; //the converter, which gives output

        if(whereToStop == 1) { //output uppercase lines after input
            converter = new UpperCaseConverter(firstPipe, null, useFileOutput, outputPath);
            //start the filters
            generator.start();
            converter.start();
        } else if (whereToStop == 2) { //output uppercase lines after circular shift
            shifter = new CircularShifter(firstPipe, secondPipe);
            converter = new UpperCaseConverter(secondPipe, null, useFileOutput, outputPath);
            //start the filters
            generator.start();
            shifter.start();
            converter.start();
        } else { //output uppercase after alphabetization (default)
            shifter = new CircularShifter(firstPipe, secondPipe);
            sorter = new Alphabetizer(secondPipe, thirdPipe);
            converter = new UpperCaseConverter(thirdPipe, null, useFileOutput, outputPath);
            //start the filters
            generator.start();
            shifter.start();
            sorter.start();
            converter.start();
        }
    }
}

