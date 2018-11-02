package com.alarmclockradio;

public class ACR
{
    //components of the ACR
    public Alarm theAlarm;
    public Clock theClock;
    public Radio theRadio;

    //booleans to determine if the clock radio has a component or not
    private boolean hasAlarm;
    private boolean hasClock;
    private boolean hasRadio;

    /* constructor */
    //creates an empty object; you add the components yourself :P
    public ACR() {
        boolean hasAlarm = false;
        boolean hasClock = false;
        boolean hasRadio = false;
    }

    //add a clock to the ACM if one doesn't exist
    public void addClock(int h, int m, int s, boolean isitAM) {
        if (!this.hasClock) {    //if no clock
            this.theClock = new Clock(h, m, s, isitAM); //create one
            this.hasClock = true;  //now there is one
        }
    }

    //add an alarm to the ACM
    //can only add alarm if there is an existing clock, but no alarm
    public void addAlarm(int h, int m, boolean isitAM, Sound snd) {
        if (this.hasClock && !this.hasAlarm) { //if clock but no alarm
            this.theAlarm = new Alarm(h, m, isitAM, snd); //create one
            this.hasAlarm = true; //now there is an alarm
        }
        else System.out.println("Could not add alarm. Please add a clock first.");
    }

    //add a radio to the ACM
    public void addRadio(String station, int volume) {
        if (!this.hasRadio) { //if no radio
            this.theRadio = new Radio(station, volume); //add radio
            this.hasRadio = true; //now there is one
        }
    }

    /* radio, alarm, and clock are controlled by accessing
     * the public members of this class (theClock, theAlarm, and theRadio)
     *
     * This is not a great idea but I don't have much time left
     * so I can't recreate all the members here with error checking :/
     */

    //I thought there was something wrong with the compare function and resorted to this out of desperation and it made no difference
    //god help me, god help us all
    public int adder(int h, int m, int s, boolean a)
    {
        int o = s + (100 * m) + (10000 * h);
        if (a) o += 1000000;
        return o;
    }

    //checks to see if alarm time = clock time, and sets off alarm
    public void checkAlarm() {
        if(hasClock && hasAlarm) {

            int c = adder(theClock.getHour(), theClock.getMinute(), theClock.getSecond(), theClock.getisAM());
            int a = adder(theAlarm.getHour(), theAlarm.getMinute(), theAlarm.getSecond(), theAlarm.getisAM());
            /*
            if ((theClock.getisAM() == theAlarm.getisAM()) &&
                (theClock.getHour() == theAlarm.getHour()) &&
                (theClock.getMinute() == theAlarm.getMinute()) &&
                (theClock.getSecond() == theAlarm.getSecond())) {
                System.out.println("test1");
                theAlarm.makenoise(); //time is right to make noise
            }
           }*/
            if (a == c) {
                System.out.println("test1");
                theAlarm.makenoise(); //time is right to make noise
            }
        }
        else System.out.println("Please add an alarm");

    }

}
