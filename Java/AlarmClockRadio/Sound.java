package com.alarmclockradio;


public class Sound {
    /* private parts... haha */
    protected int volume;       //number from 1 to 10
    protected String output;    //noise the sound makes

    /* constructor */

    //create a sound object
    public Sound(String out, int vol) {
        output = out;
        volume = vol;
    }

    /* accessors */

    //check the volume
    public int getVolume() {
        return this.volume;
    }

    //retrieve the output
    public String getOutput() {
        return this.output;
    }

    /* modifiers */

    //ensures volume is between 1 and 10
    public void setVolume(int vol) {
        if(vol > 10) vol = 10;
        else if(vol < 0) vol = 0;
        this.volume = vol;
    }

    public void setOutput(String noise) {
        this.output = noise;
    }


}
