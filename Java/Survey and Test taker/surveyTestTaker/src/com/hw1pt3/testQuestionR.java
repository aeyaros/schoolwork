package com.hw1pt3;

import java.io.Serializable;

public class testQuestionR extends questionRanking implements Serializable, testQuestion {

    testQuestionR(input in, output out) {
        super(in, out, "Enter a series of answers for the user to try and rank in proper order.\n The correct order of ranking is determined by the order you type in the answers when creating the question. \n i.e. the first you enter will be ranked 1, the second 2, etc.");
    }

    @Override
    public double grade() {
        //to grade, just ensure that userAnswers[i][0] == userAnswers[i][1]

        //for each row in userAnswers
        for (int i = 0; i < this.userAnswers.length; i++) {
            if (!(this.userAnswers[i][0] == this.userAnswers[i][1])) { //if columns dont match
                return 0.0; //then user chose incorrectly
            }
        }
        //otherwise, all answers the user picked are identical to the answers they should be
        return 1.0;
    }
}
