package com.alarmclockradio;

public class Radio extends Sound {

    private boolean radioon; //true means radio is turned on

    /* constructor */

    //radio starts out turned on when created
    public Radio(String station_title, int volume) {
        super(station_title, volume);
        radioon = true;
    }

    /* accessors */

    //check if on
    public boolean isOn() {
        return radioon;
    }

    //returns radio output if on
    public String getOutput() {
        if (this.isOn()) {
            String output = "Radio Station ";
            output += this.output;
            return output;
        } else return "The radio is off";
    }

    //"plays" the radio
    public void play() {
        if (this.isOn()) {
            String output = "Now playing radio station ";
            output += this.output;
            System.out.println(output);
        } else System.out.println("Radio turned off");
    }

    /* modifiers */

    //turn on radio
    public void turnOn() {
        radioon = true;

    }

    //turn off radio
    public void turnOff() {
        radioon = false;
    }

    //set radio station
    public void setOutput(String station) {
        this.output = station;
    }
}
