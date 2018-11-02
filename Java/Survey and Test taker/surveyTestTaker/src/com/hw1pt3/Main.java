/* ------------
 * Andrew Yaros
 * CS350 HW1 Part 3
 * Survey/Test Program
 * -------------------
 */

package com.hw1pt3;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    /* Loaded surveys */
    private static ArrayList<Survey> loadedSurveys = new ArrayList<>();
    private static ArrayList<Test> loadedTests = new ArrayList<>();

    private static final String SURVEYDIR = "Surveys";
    private static final String TESTDIR = "Tests";

    private static final String SURVEYEXT = "srv";
    private static final String TESTEXT = "tst";

    private static String absolutePath;

    public static void main(String[] args) {
        //hello :)

        System.out.println();
        System.out.println();
        System.out.println("    Initializing I/O...");

        /* Setting up inputs and outputs */
        inputConsole inConsole = new inputConsole();
        outputConsole outConsole = new outputConsole();

        outConsole.printOut("    Setting up directories..."); outConsole.newLine();
        if(setUpDirs(outConsole) != 0) {
            outConsole.printOut("    WARNING: could not set up directories. Saving and loading likely will not work.");
        }

        /* Where is the program located? */
        absolutePath = new File("").getAbsolutePath();

        System.out.println("    Building menus..."); outConsole.newLine();
        /* CREATE MENUS */

        /* COMMOM MENU COMMANDS */
        //to return to previous menu:
        menuCommand goBack = (in, out) -> {
            return 1;
        };

        /* SURVEY MENU */
        menu mainSurveyMenu = new menu("Working with surveys", "Please type an option, then press enter:");
        menuCommand createSurvey = (in, out) -> {
            loadedSurveys.add(new Survey(in, out)); //add new survey
            return 0;
        };

        menuCommand displaySurvey = (in, out) -> {
            out.printOut("Please select a survey to display:"); out.newLine();
            int ret = listSurveysLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedSurveys.size(), in, out);
                loadedSurveys.get(input).display(out);
            }
            return 0;
        };

        menuCommand saveSurvey = (in, out) -> {
            //ask user which survey they want to save
            out.printOut("Please select a survey to save to a file:"); out.newLine();
            if(listSurveysLoaded(out) == 0) {
                int input = in.getUserIndexInput(1, loadedSurveys.size(), in, out);
                Survey survey = loadedSurveys.get(input); //chosen survey

                String sTitle = survey.getTitle(); //title of the chosen survey
                //construct filepath
                String path = SURVEYDIR + File.separator + sTitle + '.' + SURVEYEXT; //path of the file to save

                //save the survey to a path
                out.saver(survey, path, out);
            }
            return 0;
        };

        menuCommand loadSurvey = (in, out) -> {
            //get directory
            File surveyDir = new File(absolutePath + File.separator + SURVEYDIR);
            File[] surveys = surveyDir.listFiles(); //get files

            out.printOut("Please choose a file from the survey directory:");
            out.newLine();

            //print and count files in directory
            int n = fileCounterPrinter(surveys, SURVEYEXT, out);

            if(n == 0) {
                out.printOut("There are no survey files in the survey directory.");
                out.newLine(2);
                return 0;
            }
            else {
                //get user input
                int userInput = input.getUserIndexInput(1, n, in, out);
                String fName = surveys[userInput].getName();

                //construct file path
                String path = absolutePath + File.separator + SURVEYDIR + File.separator + fName; //path of the file to load
                Survey loadedSurvey = in.loader(path, out);
                if(loadedSurvey != null) {
                    loadedSurveys.add(loadedSurvey);
                }
            }
            return 0;
        };

        menuCommand modifySurvey = (in, out) -> {
            out.printOut("Please select a survey to edit:"); out.newLine();
            int ret = listSurveysLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedSurveys.size(), in, out);
                loadedSurveys.get(input).edit(in, out);
            }
            return 0;
        };

        menuCommand takeSurvey = (in, out) -> {
            out.printOut("Please select a survey to take:"); out.newLine();
            int ret = listSurveysLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedSurveys.size(), in, out);
                loadedSurveys.get(input).start(in, out);
            }
            return 0;
        };

        menuCommand tabulateSurvey = (in, out) -> {
            out.printOut("Please select a survey to tabulate:"); out.newLine();
            int ret = listSurveysLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedSurveys.size(), in, out);
                loadedSurveys.get(input).tabulate(in, out);
            }
            return 0;
        };

        mainSurveyMenu.addOption("1","Create a Survey", createSurvey);
        mainSurveyMenu.addOption("2","Display a Survey", displaySurvey);
        mainSurveyMenu.addOption("3","Load a Survey", loadSurvey);
        mainSurveyMenu.addOption("4","Save a Survey", saveSurvey);
        mainSurveyMenu.addOption("5","Modify an Existing Survey", modifySurvey);
        mainSurveyMenu.addOption("6","Take a Survey", takeSurvey);
        mainSurveyMenu.addOption("7","Tabulate a Survey", tabulateSurvey);
        mainSurveyMenu.addOption("8","Go back", goBack);

        /* TEST MENU */
        menu mainTestMenu = new menu("Working with tests", "Please type an option, then press enter:");

        menuCommand createTest = (in, out) -> {
            loadedTests.add(new Test(in, out));
            return 0;
        };

        menuCommand displayTest = (in, out) -> {
            out.printOut("Please select a test to display:"); out.newLine();
            int ret = listTestsLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                loadedTests.get(input).display(out);
            }
            return 0;
        };

        menuCommand saveTest = (in, out) -> {
            //ask user which test they want to save
            out.printOut("Please select a test to save to a file:"); out.newLine();
            if(listTestsLoaded(out) == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                Test test = loadedTests.get(input); //chosen test

                String tTitle = test.getTitle(); //title of the chosen test
                //construct filepath
                String path = TESTDIR + File.separator + tTitle + '.' + TESTEXT; //path of the file to save

                //save the test to a path
                out.saver(test, path, out);
            }
            return 0;
        };

        menuCommand loadTest = (in, out) -> {
            //get directory
            File testDir = new File(absolutePath + File.separator + TESTDIR);
            File[] tests = testDir.listFiles(); //get files

            out.printOut("Please choose a file from the test directory:");
            out.newLine();

            //print and count files in directory
            int n = fileCounterPrinter(tests, TESTEXT, out);

            if(n == 0) {
                out.printOut("There are no test files in the survey directory.");
                out.newLine(2);
                return 0;
            }
            else {
                //get user input
                int userInput = input.getUserIndexInput(1, n, in, out);
                String fName = tests[userInput].getName();

                //construct file path
                String path = absolutePath + File.separator + TESTDIR + File.separator + fName; //path of the file to load
                Test loadedTest = in.loader(path, out);
                if(loadedTest != null) {
                    loadedTests.add(loadedTest);
                }
            }
            return 0;
        };

        menuCommand modifyTest = (in, out) -> {
            out.printOut("Please select a test to edit:"); out.newLine();
            int ret = listTestsLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                loadedTests.get(input).edit(in,out);
            }
            return 0;
        };

        menuCommand takeTest = (in, out) -> {
            out.printOut("Please select a test to take:"); out.newLine();
            int ret = listTestsLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                loadedTests.get(input).start(in,out);
            }
            return 0;
        };

        menuCommand tabulateTest = (in, out) -> {
            out.printOut("Please select a test to tabulate:"); out.newLine();
            int ret = listTestsLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                loadedTests.get(input).tabulate(in,out);
            }
            return 0;
        };

        menuCommand gradeTest = (in, out) -> {
            out.printOut("Please select a test to grade:"); out.newLine();
            int ret = listTestsLoaded(out);
            if(ret == 0) {
                int input = in.getUserIndexInput(1, loadedTests.size(), in, out);
                loadedTests.get(input).gradeTest(in,out);
            }
            return 0;
        };

        mainTestMenu.addOption("1","Create a Test", createTest);
        mainTestMenu.addOption("2","Display a Test", displayTest);
        mainTestMenu.addOption("3","Load a Test", loadTest);
        mainTestMenu.addOption("4","Save a Test", saveTest);
        mainTestMenu.addOption("5","Modify an Existing Test", modifyTest);
        mainTestMenu.addOption("6","Take a Test", takeTest);
        mainTestMenu.addOption("7","Tabulate a Test",tabulateTest);
        mainTestMenu.addOption("8","Grade a Test", gradeTest);
        mainTestMenu.addOption("9","Go back", goBack);

        /* MAIN MENU */
        menu mainMenu = new menu("Main Menu", "Do you want to work with a survey or a test?");

        //run the survey menu:
        menuCommand loadSurveyMain = (in, out) -> {
            mainSurveyMenu.run(in, out);
            return 0;
        };

        //run the test menu:
        menuCommand loadTestMain = (in, out) -> {
            mainTestMenu.run(in, out);
            return 0;
        };

        mainMenu.addOption("1", "Surveys", loadSurveyMain);
        mainMenu.addOption("2", "Tests", loadTestMain);
        mainMenu.addOption("3", "Quit", goBack);

        outConsole.printOut("    Setup complete! Running main menu."); outConsole.newLine();
        outConsole.newLine(2);
        outConsole.printOut("------------------------------------------------"); outConsole.newLine();
        outConsole.printOut("Andrew Yaros - Drexel University - CS 350 - HW 2"); outConsole.newLine();
        outConsole.printOut("Creating, displaying, saving, and loading tests."); outConsole.newLine();
        outConsole.printOut("------------------------------------------------"); outConsole.newLine();

        /* /// ACTUALLY RUN THE PROGRAM HERE: /// */
        //load the main menu with an input and an output. When the exit option is selected, the main menu's loop will be broken, and the program will exit.
        mainMenu.run(inConsole, outConsole);
    }

    public static int listSurveysLoaded(output out) {
        if(loadedSurveys.size() == 0) {
            out.printOut("There are no surveys loaded in memory.");
            out.newLine(3);
            return 1;
        } else {
            int i = 1;
            for(Survey s: loadedSurveys) {
                out.printOut(Integer.toString(i), ") ", s.getTitle());
                out.newLine();
                i++;
            }
            out.newLine();
            return 0;
        }
    }

    public static int listTestsLoaded(output out) {
        if(loadedTests.size() == 0) {
            out.printOut("There are no tests loaded in memory.");
            out.newLine(3);
            return 1;
        } else {
            int i = 1;
            for(Test t: loadedTests) {
                out.printOut(Integer.toString(i),") ", t.getTitle());
                out.newLine();
                i++;
            }
            out.newLine();
            return 0;
        }
    }

    //creates relative directories for surveys and tests if they don't already exist
    public static int setUpDirs(output out) {
        File sur = new File(SURVEYDIR);
        File tst = new File(TESTDIR);
        int x = 0;

        if(!sur.isDirectory()) { //if no survey directory
            if(sur.mkdir()) { //create it
                out.printOut("    Created a local directory for surveys: ", sur.getAbsolutePath());
                out.newLine();
                x++;
            } else {
                out.printOut("    WARNING: Failed to create local survey directory within ", absolutePath); out.newLine();
                return 1;
            }
        }
        if(!tst.isDirectory()) { //if no test directory
            if(tst.mkdir()) {//create it
                out.printOut("    Created a local directory for tests: ", tst.getAbsolutePath());
                out.newLine();
                x++;
            } else {
                out.printOut("    WARNING: Failed to create local test directory within ", absolutePath); out.newLine();
                return 1;
            }
        }

        if(x == 0){
            out.printOut("    Survey and test directories were found successfully."); out.newLine();
        }

        return 0;
    }

    //counts and prints names of files in an array, given a certain extension
    public static int fileCounterPrinter(File[] files, String extension, output out) {
        int n = 0; //number of files
        try {
            for (File f : files) { //if file is a file...
                String name = f.getName();
                //make sure file is a file, and make sure text after last . matches the file extension we want
                if (f.isFile() && name.substring(name.lastIndexOf('.') + 1).equals(extension)) {
                    out.printOut(Integer.toString(n + 1), ") ", f.getName());
                    out.newLine();
                    n++;
                }
            }
        } catch(Exception e)
        {
            out.printOut("Error. Files not found! Don't delete the survey and test directories while the program's running!");
        }
        return n;
    }

    //get time taken
    public static String getTimeDate() {
        Date today = new Date();
        SimpleDateFormat now = new SimpleDateFormat("y.M.d 'at' h.m.s");
        return String.format(now.format(today));
    }
}
