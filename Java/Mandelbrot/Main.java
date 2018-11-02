/* Andrew Yaros
 * CS 361 Assignment 1
 * Part 2
 * MULTITHREADED VERSION
 *//* Main class */

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Main {

    //declare array for thread results
    static volatile double[][][][] results;

    public static void main(final String args[]){
        /* Get arguments from command line
         * Return if no args, or odd number of args
         */
        int argsLength = args.length;
        if(argsLength != 14) { System.out.println(
                "Use exactly 14 args; 7 pairs of -ARGNAME, XXX.yyy");
            return;
        }

        //7 parameters * 2 = 14
        //not using NUMTHREADS as argument for sequential version

        //argument vars
        int NUMTHREADS = 0; //because this is sequential version
        int SIZE = 0; int THRESHOLD = 0; double XLO = 0;
        double XHI = 0; double YLO = 0;  double YHI = 0;

        //parse arguments
        try { //don't need to increment to the last value because:
             //last value should be number corresponding to
            //the second to last arg, for which we expect text
            for(int i = 0; i < argsLength-1; i++) {
                switch (args[i]) {
                    case "-NUMTHREADS": i++;//increment i; next arg is number
                        NUMTHREADS = Integer.parseInt(args[i]);
                        break;
                    case "-SIZE": i++;
                        SIZE = Integer.parseInt(args[i]);
                        break;
                    case "-THRESHOLD": i++;
                        THRESHOLD = Integer.parseInt(args[i]);
                        break;
                    case "-XLO": i++;
                        XLO = Double.parseDouble(args[i]);
                        break;
                    case "-XHI": i++;
                        XHI = Double.parseDouble(args[i]);
                        break;
                    case "-YLO": i++;
                        YLO = Double.parseDouble(args[i]);
                        break;
                    case "-YHI": i++;
                        YHI = Double.parseDouble(args[i]);
                        break;
                    default:
                        throw new IllegalArgumentException(args[i]
                                + " is unexpected.");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage() + " should be a number");
            return;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }

        if(NUMTHREADS < 1) {
            System.out.println("Cant have " + Integer.toString(NUMTHREADS) + " threads.");
            NUMTHREADS = 1;
            System.out.println("Running with only one thread.");
        }

        //calculate how many rows each thread will do
        int threadHeight = SIZE/NUMTHREADS;

        //value for thread calculation results is a Main class variable
        //variable for thread objects
        mandelbrot[] threadObjects = new mandelbrot[NUMTHREADS];

        //after we get results, they will be consolidated into this array
        double[][][] pixels = new double[SIZE][SIZE][4];

        //time the calculation
        long startT, endT, runT;
        //Note: I couldn't find "System.currentMillis"
        //I only found "System.currentTimeMillis"

        /* Just in case things don't divide evenly:
         * We will make it so the last thread is larger;
         *     that is, we divide size by the number of threads.
         *     Each height is the integer value of that division
         *     except for the last, which is that value plus the remainder.
         *///the last thread will be threadHeight + (SIZE%NUMTHREADS)
        int lastThreadSize = threadHeight+(SIZE%NUMTHREADS);

        //we are splitting up the height computations
        //we need the difference between yhi and ylo
        double yDiff = YHI - YLO;

        //by "penultimate" I mean regarding all but the last piece

        //divide into n-1 equal pieces and one larger piece
        int piece = SIZE/NUMTHREADS; //integer size of NUMTHREADS equal pieces (in pixels)
        int penultimate = piece * (NUMTHREADS-1); //NUMTHREADS-1 pieces (in pixels)
        //what fraction of the entire size is the penultimate size (between 0.0 - 1.0)
        double penultimatePercentage = ((double) penultimate)/((double) SIZE);
        //penultimate size divided into n-1 pieces (between 0.0 - 1.0)
        double fractionOfPenultimate = yDiff*(penultimatePercentage/((double) NUMTHREADS-1));
        //the yLo of the last piece
        double yPenultimateHeight = YLO + (penultimatePercentage*yDiff);


        //results array may allocate slightly more space, since last index may have more height
        Main.results = new double[NUMTHREADS][SIZE][lastThreadSize][4];

        //get starting time
        startT = System.currentTimeMillis();

        //calculate all but the last thread
        for(int i = 0; i < NUMTHREADS-1; i++) {
            //instead of a fraction of the whole size, it is a fraction
            //of the penultimate size
            double tempYlo = YLO + (fractionOfPenultimate*  i);
            double tempYhi = YLO + (fractionOfPenultimate* (i+1));

            //create the thread object
            threadObjects[i] = new mandelbrot("thread"+Integer.toString(i),
                    i,XLO,XHI,tempYlo,tempYhi,SIZE,threadHeight,THRESHOLD);
            //run the thread
            threadObjects[i].start();
        }

        //next do the last thread
        //go from temp height of the last thread from the above loop
        threadObjects[NUMTHREADS-1] = new mandelbrot("thread"+Integer.toString(NUMTHREADS-1),
                NUMTHREADS-1,XLO,XHI,yPenultimateHeight,YHI,SIZE,lastThreadSize,THRESHOLD);
        //run the last thread
        threadObjects[NUMTHREADS-1].start();

        //join threads
        try { //join all but the last thread
            for(int i = 0; i < NUMTHREADS-1; i++) {
                threadObjects[i].join();
            } //join last thread
            threadObjects[NUMTHREADS-1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Couldn't return a thread");
            return;
        }

        //get ending time
        endT = System.currentTimeMillis();

        //convert 4D results array into 3D pixels array
        /* array conversion example
            400x400  8 threads  th = 50

            copy from results[i][w][from 0 to 49 ][z] to pixels[w][0 to 49][z]
            copy from results[i][w][from 0 to 49 ][z] to pixels[w][50 to 99][z]
            copy from results[i][w][from 0 to 49 ][z] to pixels[w][100 to 149][z]
            copy from results[i][w][from 0 to 49 ][z] to pixels[w][150 to 199][z]
            copy from results[i][w][from 0 to 49 ][z] to pixels[w][i*threadheight to (i+1)*threadheight -1][z]
            ......      */

        //first, add all but the last thread
        for(int i = 0; i < NUMTHREADS-1; i++) {
            for(int w = 0; w < SIZE; w++) { //width
                for(int z = 0; z < 4; z++) { //color/mono
                    int resultHeight = 0; //from 0 to threadHeight
                    for(int h = i*threadHeight; h < (i+1)*threadHeight; h++) {
                        pixels[w][h][z] = results[i][w][resultHeight][z];
                        resultHeight++; //will go from 0 to threadHeight-1
                    }
                }
            }
        } //Now do for the last thread
        for(int w = 0; w < SIZE; w++) { //width
            for(int z = 0; z < 4; z++) { //color/mono
                int resultHeight = 0; //from 0 to threadHeight
                //calculate from the end of the penultimate thread to size
                //(size is, of course, at the end of the entire image)
                for(int h = (NUMTHREADS-1)*threadHeight; h < SIZE; h++) {
                    pixels[w][h][z] = results[NUMTHREADS-1][w][resultHeight][z];
                    resultHeight++; //will go from 0 to threadHeight-1
                }
            }
        }

        //calculate run time
        runT = endT - startT;
        System.out.println("Time: " + runT);

        //construct image
        BufferedImage square = new BufferedImage(SIZE, SIZE,
                BufferedImage.TYPE_INT_ARGB);

        for(int y = 0; y < SIZE; y++) {
            for(int x = 0; x < SIZE; x++) {
                /* Color monoC = new Color((float) pixels[y][x][0],
                        (float) pixels[y][x][0], (float) pixels[y][x][0]); */
                Color rgbC = new Color((float) pixels[y][x][1],
                        (float) pixels[y][x][2], (float) pixels[y][x][3]);
                square.setRGB(x, y, rgbC.getRGB());
            }
        }

        //save file
        String fileName = "mdl_nthr" + NUMTHREADS + "_s_" + SIZE + "_th_" + THRESHOLD
                + "_xL_" + XLO + "_xH_" + XHI
                + "_yL_" + YLO + "_yH_" + YHI + ".png";

        try { ImageIO.write(square, "png",
                    new File(fileName)); }
        catch (IOException e) { e.printStackTrace(); }

        System.out.println(" ");
    } //end main
} //end class

