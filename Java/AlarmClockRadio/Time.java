package com.alarmclockradio;

public class Time {

    /* private parts... haha */
    private int hour;
    private int minute;
    private int second;
    private boolean isAM;

    /* constructor */

    //create a time object, set each private variable
    protected Time(int hr, int min, int sec, boolean isitAM) {
        setHour(hr);
        setMinute(min);
        setSecond(sec);
        isAM = isitAM;
    }

    /* accessors */

    //get hour
    protected int getHour() {
        return this.hour;
    }

    //get minute
    protected int getMinute() {
        return this.minute;
    }

    //get second
    protected int getSecond() {
        return this.second;
    }

    //get AM or PM
    protected boolean getisAM() {
        return this.isAM;
    }

    public String getTime()
    {
        String output = "";

        //display a 12 if hour is set to 0
        if (this.getHour() == 0) {
            output += "12";
        }
        else output += this.getHour();


        output += ":";// + this.getMinute() + " ";

        //minutes: add leading 0 if less than 10
        if(this.getMinute() < 10) {
            String min = "0";
            min += this.getMinute();
            output += min;
        }
        else output += this.getMinute();

        /*
        output += ":";
        //seconds: add leading 0 if less than 10
        if(this.getSecond() < 10) {
            String sec = "0";
            sec += this.getSecond();
            output += sec;
        }
        else output += this.getSecond();
        */

        //display if AM or PM
        if (this.getisAM()) {
            output += " AM";
        }
        else output += " PM";

        return output;
    }

    public void displayTime() {
        System.out.println(this.getTime());
    }

    /* modifiers */

    //ensures hour is between 0 and 11
    protected void setHour(int hr) {
        if(hr > 11) hr = 11;
        else if (hr < 0) hr = 0;
        this.hour = hr;
    }

    //ensures minute is between 0 and 59
    protected void setMinute(int min) {
        if(min > 59) min = 59;
        else if (min < 0) min = 0;
        this.minute = min;
    }

    //ensures second is between 0 and 59
    protected void setSecond(int sec) {
        if(sec > 59) sec = 59;
        else if (sec < 0) sec = 0;
        this.second = sec;
    }

    //set if AM or not
    protected void setifAM(boolean isitAM) {
        this.isAM = isitAM;
    }

    //toggle between AM and PM
    protected void toggleAMPM() {
        if (this.isAM) {        //if AM
            this.isAM = false;  //set to PM
        }                       //if PM
        else this.isAM = true;  //set to AM
    }

    //set time
    public void setTime(int h, int m, int s, boolean am) {
        this.setHour(h);
        this.setMinute(m);
        this.setSecond(s);
        this.setifAM(am);
    }

}
