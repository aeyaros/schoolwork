package com.hw1pt3;

import java.io.Serializable;

public class testQuestionM extends questionMatching implements Serializable, testQuestion {
    testQuestionM(input in, output out) {
        super(in, out);
    }

    @Override
    public double grade() {
        //to grade, just ensure that userAnswers[i][0] == userAnswers[i][1]

        //for each row in userAnswers
        for(int i = 0; i < this.userAnswers.length; i++) {
            if(!(this.userAnswers[i][0] == this.userAnswers[i][1])) { //if columns dont match
                return 0.0; //then user chose incorrectly
            }
        }

        //otherwise, all answers the user picked are identical to the answers they should be
        return 1.0;
    }
}
