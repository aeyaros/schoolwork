/* * * * * * * *
 * Andrew Yaros *
 * SE 311 HW #1 *
 * * * * * * * *
 * Abstract output class
 * Used for exporting the results of the computation
 * i.e. used to export a list of lines
 * * * * * * * */
public abstract class Output {
    public abstract void doOutput(LineStorage output, String pathArg, String delimeter);

    public static void printout(String s){
        System.out.print(s);
    }
}
