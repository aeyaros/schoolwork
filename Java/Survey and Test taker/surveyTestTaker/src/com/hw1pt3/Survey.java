package com.hw1pt3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Survey implements Serializable {
    //title of the survey
    protected String title;
    protected ArrayList<Question> questions;
    protected int timesTaken;

    Survey() {
        this.questions = new ArrayList<>();
    }

    Survey(input in, output out) {
        this.timesTaken = 0;
        this.questions = new ArrayList<>();
        //set title
        out.printOut("Please enter a title:"); out.newLine();
        this.title = in.getLineFromUser(out);

        //add questions until user decides they are done
        int b = 0;
        while(b == 0) {
            this.addQuestionMenu(in, out); out.newLine(2);
            out.printOut("Would you like to add another question? Type y or n"); out.newLine();


            int c = 0;
            while (c == 0) {
                String input = in.getLineFromUser(out);
                if (input.equals("n")) {
                    b = 1; //no more questions: break outer loop
                    c = 1; //valid user response: break inner loop
                } else if (input.equals("y")) {
                    //dont break outer loop: user wants another question
                    c = 1; //valid user response: break inner loop
                } else { //invalid response
                    out.printOut("Please type y or n:"); out.newLine();
                }
            }
        }
    }

    //get rid of currently loaded answers
    public void clearAllUserAnswers() { //clear all the user answers that were saved
        for(Question q: this.questions) {
            q.clearUserAnswers();
        }
    }

    //get rid of all past responses
    public void clearAllPastResponses() {
        for(Question q:this.questions) {
            q.clearPastResponses();
        }
    }

    //save the responses
    public void saveAnsweredQuestions(ArrayList<Question> questionsToSave) {
        String theTime = Main.getTimeDate();
        for(Question q: this.questions) {
            q.addResponses(); //add responses from each question to the question's saved responses
        }
    }

    //load the responses
   // public ArrayList<Question> loadAnsweredQuestions(int index) {
   //     return this.completedCopies.get(index).getSavedQuestions();
   // }

   // public void clearSavedCopies() {
   //     this.completedCopies.clear();
   // }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    protected void printAddQuestionMenu(output out) {
        out.printOut("Please select a type of question to add:"); out.newLine();
        out.printOut("1) Multiple choice"); out.newLine();
        out.printOut("2) Multiple-multiple choice"); out.newLine();
        out.printOut("3) True or false"); out.newLine();
        out.printOut("4) Short answer"); out.newLine();
        out.printOut("5) Essay"); out.newLine();
        out.printOut("6) Matching"); out.newLine();
        out.printOut("7) Ranking"); out.newLine();
    }

    protected void addQuestionMenu(input in, output out) {
        int b = 0;
        while (b == 0) {
            printAddQuestionMenu(out); out.newLine();
            String userChoice = in.getLineFromUser(out);
            switch (userChoice) {
                case "1":  //MC
                    out.printOut("Adding multiple choice survey question:");
                    out.newLine();
                    this.questions.add(new questionMultiple(in, out));
                    b = 1;
                    break;
                case "2":  //MMC
                    out.printOut("Adding multiple-multiple choice survey question:");
                    out.newLine();
                    this.questions.add(new questionMultipleMultipleChoice(in, out));
                    b = 1;
                    break;
                case "3":  //TF
                    out.printOut("Adding true/false survey question:");
                    out.newLine();
                    this.questions.add(new questionTF(in, out));
                    b = 1;
                    break;
                case "4":  //SA
                    out.printOut("Adding short answer survey question:");
                    out.newLine();
                    this.questions.add(new questionShortAnswer(in, out));
                    b = 1;
                    break;
                case "5":  //E
                    out.printOut("Adding essay survey question:");
                    out.newLine();
                    this.questions.add(new questionEssay(in, out));
                    b = 1;
                    break;
                case "6":  //M
                    out.printOut("Adding matching survey question:");
                    out.newLine();
                    this.questions.add(new questionMatching(in, out));
                    b = 1;
                    break;
                case "7":  //R
                    out.printOut("Adding ranking survey question:");
                    out.newLine();
                    this.questions.add(new questionRanking(in, out));
                    b = 1;
                    break;
                default:
                    out.printOut("You must enter a number between 1 and 7.");
                    out.newLine();
                    break;
            }
        }
    }

    public void start(input in, output out) {
        //clear any user answers out of this instance
        this.clearAllUserAnswers();

        //print dashes above and below title
        for(int i = 0; i < this.title.length(); i++) out.printOut("-");
        out.newLine();
        out.printOut(this.title); //print title
        out.newLine();
        for(int i = 0; i < this.title.length(); i++) out.printOut("-");
        out.newLine(2);

        //ask each question
        int questionNumber = 1;
        for(Question q: this.questions) {
            out.printOut("Question #", Integer.toString(questionNumber),":");
            out.newLine(2);

            q.ask(in,out); //ask the current question
            out.newLine(2);

            questionNumber++;
        }

        //survey complete; save completed copy, increment times taken
        this.timesTaken++;
        saveAnsweredQuestions(this.questions);

        out.newLine(2); //all done
   }

    //print out the question prompts
    public void printPrompts(input in, output out) {
        //go through questions, print prompts
        for(int i = 0; i < this.questions.size(); i++) {
            out.printOut("Question #");
            out.printIndex(i);
            out.printOut(": ", this.questions.get(i).getPrompt());
            out.newLine(2);
        }
    }

    public int edit(input in, output out) {
        int a = 0;
        while(a == 0) {
            out.printOut("WARNING: Editing a survey or test will erase all saved copies of it."); out.newLine();
            out.printOut("To keep the data, you can still save a survey or test to the disk before editing."); out.newLine();
            //This does not count as "test code being in the survey code." :/
            out.printOut("Would you like to proceed? Type y or n? If you proceed, data will be erased.");out.newLine();
            String proceed = in.getLineFromUser(out);
            if(proceed.equals("n"))  return 0; //exit this function, did not edit
            else if(proceed.equals("y")) a = 1; //break this loop
            else {
                out.printOut("Invalid input. Please type y for yes or n for no.");
                out.newLine(3);
            }
        }
        //if here, user decided to continue
        //clear out the data!
        this.clearAllUserAnswers();
        this.clearAllPastResponses();
        this.timesTaken = 0;

        int b = 0;
        while (b == 0) {
            //print questions
            out.printOut("Editing ", this.getTitle());
            out.newLine();

            this.printPrompts(in, out);

            out.printOut("Please type the number of the question you want to edit. You can also press t to edit the title or x to cancel.");
            out.newLine();

            int max = this.questions.size();

            /* user will type a number; index will be parsed from that number
            indices are 1 less than the number the user types, this is done
            using parseIndexFromNumber function, which returns -1 on failure */

            int c = 0;
            while (c == 0) {
                String userInput = "";
                userInput = in.getLineFromUser(out);

                if (userInput.equals("t")) { //edit title
                    out.printOut("Please enter a new title:");
                    out.newLine();
                    this.title = in.getLineFromUser(out);
                } else if (userInput.equals("x")) {
                    c = 2; //break this loop, user wants to cancel/exit
                } else {//otherwise, see if the user typed a number
                    int userNum = in.parsePositiveNumber(userInput);
                    if (userNum > 0 && userNum <= max) {
                        this.questions.get(userNum - 1).edit(in, out); //edit the question at that index
                        c = 1; //break out of this
                    } else {
                        out.printOut("Invalid input; try again.");
                    }

                }
            }
            if (c == 2) return 1; //user wants to cancel anyway

            out.newLine(2);
            out.printOut("Would you like to edit another question? Type y or n");
            out.newLine();
            c = 0;
            while (c == 0) {
                String input = in.getLineFromUser(out);
                if (input.equals("y")) {
                    c = 1;
                } else if (input.equals("n")) {
                    c = 1;
                    b = 1;
                } else {
                    int d = 0;
                    while(d==0) {
                        out.printOut("Please type y or n."); out.newLine();
                        String yorn = in.getLineFromUser(out);
                        if(yorn.equals("y")) {
                            d = 1;
                            c = 1;
                        } else if(yorn.equals("n")) {
                            d = 1;
                            c = 1;
                            b = 1;
                        } else {
                            out.printOut("Invalid input.");
                        }
                    }

                    out.newLine();
                }
            }//end c

        }//end b
        return 1; //edited
    }

    public void display(output out) {
        out.printOut(this.title); out.newLine(); out.newLine(); //print title
        for(Question q: this.questions) { //print each question one by one
            out.printOut("Question # ");
            out.printIndex(this.questions.indexOf(q));
            out.printOut(":");
            out.newLine();
            q.print(out);
            out.newLine();
        }
        out.newLine(2);
    }

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
            else if(q instanceof questionShortAnswer) {
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

        out.newLine(3);
    }

    public static void printAnswerIntArray(output out, int[][] userInputs) {
        //for matching, left column: user inputs, right column, correct inputs
        //for ranking, left column is used, right is not used
        //here we dont care about correct inputs, only about left columns
        for(int i = 0; i < userInputs.length; i++) {
            //print letter
            out.printCapitalLetter(i); out.printOut(": ");
            //print answer from index
            out.printOut(Integer.toString(userInputs[i][1]));
            out.newLine();
        }
    }
}
