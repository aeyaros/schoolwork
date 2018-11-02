package com.hw1pt3;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class outputConsole extends output {

    outputConsole() {
    }

    @Override
    public void printOut(String... output)
    {
        for(String s: output) {
            System.out.print(s);
        }
    }

    @Override
    public void printIndex(int index) {
        System.out.print(index + 1);
    }

    public void newLine() {
        System.out.println();
    }

    @Override
    public void newLine(int n) {
        if(n < 1) {
            System.out.println();
        }
        else {
            for(int i = 0; i < n; i ++) {
                System.out.println();
            }
        }
    }

    //print ASCII letter
    @Override
    public void printCapitalLetter(int i) {
        System.out.print(output.getCapitalLetter(i));
    }

    @Override
    public <E> void saver(E thingToSave, String path, output out) {
        if (thingToSave instanceof Survey || thingToSave instanceof Test) {
            try {
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                if (thingToSave instanceof Survey) {
                    oos.writeObject(thingToSave);
                } else {
                    oos.writeObject(thingToSave);
                }
                out.printOut("Saved file to ", path);
                out.newLine(2);
                oos.close();
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                out.printOut("Error; could not save file.");
                out.newLine();
            }
        }
    }
}
