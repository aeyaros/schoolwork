package com.alarmclockradio;

public class Clock extends Time {

    /* constructor */

    public Clock(int h, int m, int s, boolean isitAM) {
        super(h, m, s, isitAM);
    }

    /* method */

    //tick: move the time forwards by one second
    public void tick() {
        if(this.getSecond() == 59) {         //if second is 59
            this.setSecond(0);            //reset back to 0,
            if(this.getMinute() == 59) {     //if minute is 59,
                this.setMinute(0);        //reset back to 0
                if(this.getHour() == 11) {   //if hour is 11,
                    this.setHour(0);      //reset back to 0
                    this.toggleAMPM();  //toggle AM or PM
                }       //otherwise, not 11 so increment hour
                else this.setHour(this.getHour() + 1);
            }           //otherwise, not 59 so increment minute
            else this.setMinute(this.getMinute() + 1);
        }               //otherwise, not 59 so increment second
        else this.setSecond(this.getSecond() + 1);
    }

}
