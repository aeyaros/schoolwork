package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Test extends Survey implements Serializable {
    private ArrayList<testGradeRecord> savedGrades;

    Test() {
        this.questions = new ArrayList<>();
    }

    Test(input in, output out) {
        super(in, out);
        this.savedGrades = new ArrayList<>();
    }

    @Override
    public void addQuestionMenu(input in, output out) {
        int b = 0;
        while (b == 0) {
            printAddQuestionMenu(out); out.newLine();
            String userChoice = in.getLineFromUser(out);
            if (userChoice.equals("1")) { //MC
                out.printOut("Adding multiple choice test question:"); out.newLine();
                this.questions.add(new testQuestionMC(in, out));
                b = 1;
            } else if (userChoice.equals("2")) { //MMC
                out.printOut("Adding multiple-multiple choice test question:"); out.newLine();
                this.questions.add(new testQuestionMMC(in, out));
                b = 1;
            } else if (userChoice.equals("3")) { //TF
                out.printOut("Adding true/false test question:"); out.newLine();
                this.questions.add(new testQuestionTF(in, out));
                b = 1;
            } else if (userChoice.equals("4")) { //SA
                out.printOut("Adding short answer test question:"); out.newLine();
                this.questions.add(new testQuestionSA(in, out));
                b = 1;
            } else if (userChoice.equals("5")) { //E
                out.printOut("Adding essay test question:"); out.newLine();
                this.questions.add(new testQuestionE(in, out));
                b = 1;
            } else if (userChoice.equals("6")) { //M
                out.printOut("Adding matching test question:"); out.newLine();
                this.questions.add(new testQuestionM(in, out));
                b = 1;
            } else if (userChoice.equals("7")) { //R
                out.printOut("Adding ranking test question:"); out.newLine();
                this.questions.add(new testQuestionR(in, out));
                b = 1;
            } else {
                out.printOut("You must enter a number between 1 and 7.");
                out.newLine();
            }
        }
    }

    //make a temporary copy of a test, take it, and resave it, so you can take it many times without everything overlapping
    //finished test will be re-saved by the menu...
    @Override
    public void start(input in, output out) {
        super.start(in,out);

        //autograde, save recorded grade
        String myGrade = this.getGrade(this.questions);
        this.savedGrades.add(new testGradeRecord(myGrade));
   }

   @Override
   public int edit(input in, output out) {
        int r = super.edit(in,out);
        if(r == 1) this.savedGrades.clear(); //clear grades from the previous version of the test, if it was edited
        return r;
   }

   public void gradeTestHelper(input in, output out, int index) {
        out.printOut(this.savedGrades.get(index).getGradeString());
        out.newLine(2);
   }

   //ok, so really everything is autograded, and im just saving the output of the autograde
    //I WAS GOING TO HAVE A DIFFERENT WAY where I stored all the questions in an array so I could actually grade the questions later, but this wasn't working properly and I couldn't get it working
    public void gradeTest(input in, output out) {
        int size = this.savedGrades.size();
        if(size == 0) { //if no copies of test
            out.printOut("You have not taken this test yet since it was last created or edited. Please take it at least once before grading it.");
            out.newLine(2);
            return;//cant grade nothing, so bye bye
        } else if(size == 1) { //if user took test once
            //grade the only copy
            gradeTestHelper(in,out,0);
        }else { //if there are multiple saved tests
            int b = 0;
            while(b == 0) {
                out.printOut("This test has been taken multiple times. Do you want to grade the most recent copy?");
                out.newLine(); out.printOut("Press y or n"); out.newLine();
                String response = in.getLineFromUser(out);
                if(response.equals("y")) {
                    gradeTestHelper(in,out,size-1); //size - 1 is last index, most recently added copy
                    b = 1; //break
                } else if(response.equals("n")) {
                    //print menu of tests, pick from menu
                    out.printOut("Please choose from one of these copies:");
                    out.newLine();
                    int i = 1;
                    for(testGradeRecord sr: this.savedGrades){
                        out.printOut(Integer.toString(i), ") Copy of ", this.getTitle(), " completed at ", sr.getTimeTaken());
                        out.newLine();
                        i++;
                    }
                    out.printOut("x) Cancel"); out.newLine(2);

                    int c = 0;
                    while(c == 0){

                        out.printOut("Type in the number of the copy, or press x to cancel.");
                        out.newLine();

                        String anumber = in.getLineFromUser(out);
                        if(anumber.equals("x")) {
                            return; //user wants to cancel
                        } else {
                            int choice = in.parsePositiveNumber(anumber);
                            if(choice == -1) { //if error parsing
                                out.printOut("Please type x, or a number"); out.newLine();
                            } else if (choice < 1 || choice > size) { //if out of range
                                out.printOut("Please type a number between 1 and ", Integer.toString(size),".");
                            } else {
                                gradeTestHelper(in,out,choice-1); //grade at that index
                                c = 1;
                                b = 1;
                            }
                        }
                    }//end c
                } else {
                    out.printOut("Invalid input. Please type y or n.");
                }
            }//end b
            out.newLine();
        }
    }

    public String getGrade(ArrayList<Question> testQuestions ) {
        double numberOfQuestions = 0;
        double gradePoints = 0;

        //question counts if it is a test question, but not an essay test question
        for(Question q: testQuestions){
            if(q instanceof testQuestion && !(q instanceof testQuestionE)) {
                gradePoints += ((testQuestion) q).grade();
                numberOfQuestions++;
            }
        }

        //output final grade to user

        //set up strings
        double percentage = 100 * (gradePoints/numberOfQuestions);
        String percentStr = Double.toString(percentage);
        String gradeN     = Double.toString(gradePoints);
        String gradeD     = Double.toString(numberOfQuestions);
        String gradeN10   = Double.toString(gradePoints * 10);
        String gradeD10   = Double.toString(numberOfQuestions * 10);
        String gradeFract = gradeN10 + "/" + gradeD10;

        //string to return
        String gradeInfo = "You answered " + gradeN + " of " + gradeD + " non-essay questions. \nYour grade is: " + gradeFract + " (" + percentStr + "%)";
        return gradeInfo;
    }

    //test shortanswer questions are derived directly from survey multiple multiple choice, not from survey shortanswer
    //i dont know exactly how i want to refactor it; this is word for word except that it checks for instance of test short answer rather than instance of survey short answer
    //but if I just add an OR || in there... q instanceof questionShortAnswer || q instanceof testQuestion SA
    //then technically that would be test code inside of survey code
    //so I could have just thrown that or in there; and it is bad to copy like this
    //but i FINALLY have this all working and I'm already late :/ and i have other things to do so im leaving it like this
    //god help me
    //god help us all
    @Override
    public void tabulate(input in, output out) {
        if(this.timesTaken == 0) {
            //survey or test has not been completed
            out.printOut("You must take a survey or test before tabulating it.");
            out.newLine(2);
            return;
        }

        int questionIndex = 0;
        for(Question q: this.questions) {
            //each question has past responses saved as strings
            //to tabulate, we want the number of times each string was chosen

            //get the list of strings with past responses
            ArrayList<String> currentQuestionStrings = q.getSavedResponses();

            out.printOut("Question #", Integer.toString(questionIndex+1)," replies");
            out.newLine(2);

            //if essay
            //print all the strings
            if(q instanceof questionEssay) {
                int numParagraphs = ((questionEssay) q).getNumberOfResponses();
                for (int i = 0; i < currentQuestionStrings.size(); i++) {
                    if (i % numParagraphs == 0) out.newLine(); //extra space for new set of paragraphs
                    out.printOut(currentQuestionStrings.get(i));//print current user paragraph
                    out.newLine();
                }
            }
            //if matching or ranking
            //put strings into a set
            //loop through the set, for string s
            //count number of times s appears in the list
            //print the number, then print s
            else if(q instanceof questionMatching) {
                HashSet<String> tempSet = new HashSet<>(currentQuestionStrings);
                for(String currentAns : tempSet) {
                    int stringCount = 0;
                    for (String ans : currentQuestionStrings) {
                        if (currentAns.equals(ans)) stringCount++;
                    }
                    out.printOut(Integer.toString(stringCount) + ":");
                    out.newLine();
                    out.printOut(currentAns);
                    out.newLine(2);
                }
            }
            //if true false, same as multiple choice but print True/False instead of a letter
            else if(q instanceof questionTF) {
                for(int i = 0; i < ((questionMultipleMultipleChoice) q).answerList.size(); i++){
                    //get current question text
                    String curText = ((questionMultipleMultipleChoice) q).getAnAnswer(i);
                    int ansCount = 0;
                    for(String s: currentQuestionStrings) {
                        if(curText.equals(s)) { //match
                            ansCount++;
                        }
                    }
                    out.printOut(curText, ": ", Integer.toString(ansCount)); //print "True: n " or "False: n"
                    out.newLine();
                }
            }
            else if(q instanceof testQuestionSA) { ///////////////////////////////////////// (-_-) :/ :( :P
                //get all responses into a set
                //loop through set
                //check how often each response occurs
                HashSet<String> tempSet = new HashSet<>(q.getSavedResponses());
                for(String s: tempSet){ //current string to look for
                    int count = 0;
                    for(String t: q.getSavedResponses()){ //look at current string in saved responses
                        if(s.equals(t)) count++; //if a match, ++
                    }
                    out.printOut(s,": ", Integer.toString(count)); out.newLine();
                }

            }
            //if multiple choice
            //for int i = 0; i < answerList.size(); i++)
            //find number of times text of current answer appears in list
            //print capital letter, print number
            else if(q instanceof questionMultipleMultipleChoice) {
                int size = ((questionMultipleMultipleChoice) q).answerList.size();
                for(int i = 0; i < size; i++){
                    //get current question text
                    String curText = ((questionMultipleMultipleChoice) q).getAnAnswer(i);
                    int ansCount = 0;
                    for(String s: currentQuestionStrings) {
                        if(curText.equals(s)) { //match
                            ansCount++;
                        }
                    }
                    out.printOut(output.getCapitalLetter(i),") ", Integer.toString(ansCount)); //" Print "A) n"
                    out.newLine();
                }

            }
            questionIndex++;
            out.newLine(2);
        }
    }
}
