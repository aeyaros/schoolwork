package com.hw1pt3;

public interface menuCommand {
    int runCommand(input in, output out); //runs code as defined in a lambda block
    /*
    return value is int
    return value specifies whether to break out of the current menu when this command is done
    if 0, dont
    else, do
    e.g. goBack menuCommand: return 1, and it will break out of the menu
    */

    /*
    // how to make a menuCommand and set up a menu //

    menu myFavoriteMenu("Main menu", "choose an option");

    menu mySubMenu("Sub menu", "choose another option");

    menuCommand myFavoriteCommand = () -> {
        //put code in here
        //code like:

        System.out.println("My favorite text");

        //or run another menu object, which becomes a submenu

        mySubMenu.run(); //run submenu

        return 0; //return another int to break out of the menu once the command is done
    }

    menu.addOption("x", "choose this option", myFavoriteCommand);

    */

}
