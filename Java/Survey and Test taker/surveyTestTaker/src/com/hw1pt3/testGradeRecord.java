package com.hw1pt3;

import java.io.Serializable;

public class testGradeRecord implements Serializable {
    private String timeTaken;
    private String GradeString;

    testGradeRecord(String gradeinfo) {
        this.GradeString = gradeinfo;
        this.timeTaken = Main.getTimeDate();
    }

    public String getGradeString() {
        return GradeString;
    }

    public void setGradeString(String gradeString) {
        GradeString = gradeString;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }
}
