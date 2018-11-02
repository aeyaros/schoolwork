package com.hw1pt3;

import java.util.ArrayList;

public class menu {
    private String title; //title
    private String prompt; //instructions/prompt

    private ArrayList<menuOption> options; //options to choose

    public menu(String menuTitle, String menuPrompt) {
        this.title = menuTitle;
        this.prompt = menuPrompt;
        this.options = new ArrayList<>();
    }

    //print out the contents of the menu
    public void print(output out) {

        int l = this.title.length();
        for(int i = 0; i < l; i++) { out.printOut("_"); } out.newLine();
        //print title, prompt
        out.printOut(this.title); out.newLine();

        l = this.prompt.length();
        for(int i = 0; i < l; i++) { out.printOut("_"); } out.newLine();

        //print description:
        out.printOut(this.prompt); out.newLine(2);

        //list menu items
        for(menuOption opt:this.options) {
            out.printOut("    Press ", opt.getOptionKey(), ") ", opt.getOptionText());
            out.newLine();
        }
        out.newLine();
    }

    //add an option to the menu
    public void addOption(String key, String inf, menuCommand cmd) {
        menuOption opt = new menuOption(key, inf, cmd);
        this.options.add(opt);
    }

    //actually run the menu
    public void run(input in, output out) {
        if(this.options.size() == 0) { //if menu has no options assigned
            out.printOut("Could not run menu \"", this.title, "\" since it does not have any options assigned to it.");
            out.newLine();
        }
        else { //normal situation: menu is run

            int breakFromMenu = 0; //commands will return an int; if non-zero then escape this menu


            while (breakFromMenu == 0) { //menu runs within this loop
                int invalid = 1; //set to 0 if command successful, used to determine if we should print a message that the requested menu option was not found
                this.print(out); //print out the menu
                String input = "";
                input = in.getLineFromUser(out);

                //check each command until we see one with a key that matches user input
                for (menuOption currentOption : this.options) {
                    if (input.equals(currentOption.getOptionKey())) { //if found
                        breakFromMenu = currentOption.doCommand(in, out); //run that menu option
                        invalid = 0; //this command was run, and so it obviously wasn't invalid
                        break;
                    }
                }
                //if invalid isn't 0, then the command was unsuccessful
                if (invalid != 0) {
                    out.printOut("Error: please type in a valid command.");
                    out.newLine();
                    out.printOut("Each option has its command printed before the \")\"");
                    out.newLine(3);
                }

            }//end of menu execution
        }
    }


}
