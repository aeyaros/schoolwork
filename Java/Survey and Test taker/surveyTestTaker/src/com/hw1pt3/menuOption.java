package com.hw1pt3;

public class menuOption {
    private String optionKey; //what user presses to choose option
    private String optionText; //what the option says
    private menuCommand command; //allows code to be executed

    public menuOption(String key, String txt, menuCommand cmd) {
        this.optionKey = key;
        this.optionText = txt;
        this.command = cmd;
    }

    public String getOptionKey() {return this.optionKey; } //get key

    public String getOptionText() { return this.optionText; } //get text

    public int doCommand(input in, output out) {
        out.newLine();
        return this.command.runCommand(in, out); } //execute code
}
