package com.hw1pt3;

import java.io.Serializable;

public class testQuestionE extends questionEssay implements Serializable, testQuestion {
    testQuestionE(input in, output out) {
        super(in, out);
    }

    @Override
    public double grade() {
        return 0; //essays are not graded
    }
}
