package com.alarmclockradio;

public class Main {

    public static void main(String[] args) {
	// write your code here

        System.out.println("Andrew Yaros CS350 Lab 1 part A");

        Sound aSound = new Sound("Buzz Buzz Buzz", 5);
        Radio aRadio = new Radio("KYW 1060 News Radio", 10);

        ACR alarmclockradio = new ACR();
        alarmclockradio.addClock(11,58,56,false);
        alarmclockradio.addAlarm(12,01, true, aSound);

        for (int i = 0; i < 250; i++) {

            alarmclockradio.theClock.displayTime();
            alarmclockradio.checkAlarm();
            alarmclockradio.theClock.tick();
        }

    }
}
