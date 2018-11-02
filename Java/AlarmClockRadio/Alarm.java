package com.alarmclockradio;

public class Alarm extends Clock {

    private boolean alarmon; //true means alarm is on, false means off
    private Sound alarmsound;
    private int snoozemin = 9; //how many minutes to snooze, 9 by default
    private static final int SECONDS_IN_A_MINUTE = 60;

    public Alarm(int h, int m, boolean isitAM, Sound snd){
        super(h, m, 0, isitAM); //alarm always set to 0 seconds
        alarmsound = snd;
        alarmon = true;  //alarm starts as turned on
    }

    //can already set alarm time via Time class

    /* accessors */
    public boolean isOn() {
        return alarmon;
    }

    public String getStatus() {
        String output = "The alarm is set to ";
        output += getTime();
        output += " and is set to play";
        if (alarmsound instanceof Radio) {
            output += "radio station " + alarmsound.getOutput() + ".";
        }
        else
            output += "the noise \"" + alarmsound.getOutput() + ".\"";

        return output;
    }

    /* methods */

    //turn on alarm
    public void turnOn() {
        this.alarmon = true;
    }

    //turn off alarm
    public void turnOff() {
        this.alarmon = false;
    }

    //set alarm volume
    public void setVolume(int vol) {
        this.alarmsound.setVolume(vol);
    }

    //moves alarm clock forwards by 60s * 9min
    public void snooze() {
        int moretime = SECONDS_IN_A_MINUTE * snoozemin;
        for(int i = 0; i < moretime; i++) {
            this.tick();
        }
    }

    //alarm will be turned on by the ACR function
    //ACR can encapsulate an alarm, a clock and/or a radio.
    //checkalarm compares the alarm and the clock to see if they are set to the same time

    //if alarm is on, plays alarm, then turns off alarm
    public void makenoise() {
        if(this.alarmon) {  //if alarm on
            if(alarmsound instanceof Radio) {   //if alarm is a radio, turn radio on
                ((Radio) alarmsound).turnOn();
            }
            System.out.println(this.alarmsound.getOutput()); //print noise
            System.out.println("test1");
            this.turnOff(); //alarm is now finished, and is turned off
        }
    }
}
