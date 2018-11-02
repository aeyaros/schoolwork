package com.hw1pt3;

import java.io.Serializable;

public class testQuestionTF extends questionTF implements Serializable, testQuestion {
    testQuestionTF(input in, output out) {
        super(in, out);
        setThisAsTrueOrFalse(in, out);
    }

    @Override
    public void print(output out) {
        super.print(out);
        if(this.answerList.get(1).getIsCorrect()) { //if true is set to true...
            out.printOut("The correct answer is \"true.\"");// then is true
        } else {//otherwise, false is set to true
            out.printOut("The correct answer is \"false.\""); //then is false
        }
        out.newLine();
    }

    @Override
    public void edit(input in, output out) {
        super.edit(in, out); //edit question prompt
        setThisAsTrueOrFalse(in, out); //set answer to true or false
    }

    protected void setThisAsTrueOrFalse(input in, output out) {
        out.printOut("Is the answer true?"); out.newLine();
        int b = 0;
        while(b == 0) {
            String s = in.getLineFromUser(out);
            if(s.equals("y")){                              //answer is true
                this.answerList.get(0).setIsCorrect(false); //choosing false is bad
                this.answerList.get(1).setIsCorrect(true);  //choosing true is good
                out.printOut("Set answer to true.");out.newLine();
                b = 1;
            } else if(s.equals("n")){                       //answer is false
                this.answerList.get(0).setIsCorrect(true);  //choosing false is good
                this.answerList.get(1).setIsCorrect(false); //choosing true is bad
                out.printOut("Set answer to false."); out.newLine();
                b = 1;
            } else {
                out.printOut("Please type y or n."); out.newLine();
            }
        }
    }

    @Override
    public double grade() {
        //get the answer that the user picked, which is inside userAnswers
        answerChoice selectedByUser = this.userAnswers.get(0);

        //was that a correct answer?
        if(selectedByUser.getIsCorrect()) { //if this is the correct answer, then score = 1
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
