/*
* Andrew Yaros
* CS 360 PA 1
* Part 3: Recursive descend parser from FCS
*/

import java.util.ArrayList;

class Main {
    //static variable to store the parsetree
    public static nodeStruct parseTree = new nodeStruct(); //result of parse

    //expression will be stored in an array, terminalArray
    //the variable "nextTerminal" is a counter/index variable to access the array
    public static int nextTerminal; //current position
    public static char[] terminalArray; //array of characters
    public static int arraySize; //size of the array

    //symbols; I put END here to be safe but didn't really do much with it
    public static nodeStruct FAILED = new nodeStruct('N');
    public static final char END = '\0';

    //make node function
    public static nodeStruct makeNode0(char x) {
        nodeStruct root = new nodeStruct();
        root.setLabel(x);
        root.setI(0, new nodeStruct('N')); //leftmost child
        root.setI(1, new nodeStruct('N')); //right sibling
        return root;
    }

    //aux function
    public static nodeStruct makeNode1(char x, nodeStruct t) {
        nodeStruct root = makeNode0(x);
        root.setI(0,t); //leftmost
        return root;
    }

    //aux function
    public static nodeStruct makeNode4(char x, nodeStruct t1, nodeStruct t2, nodeStruct t3, nodeStruct t4){
        nodeStruct root = makeNode1(x, t1);
        t1.setI(1,t2); //set right sibling
        t2.setI(1,t3); //set right sibling
        t3.setI(1,t4); //set right sibling
        return root;
    }

    //get the next character in the array
    public static char getNextTerminal() {
        if(Main.nextTerminal < Main.arraySize) {
            return Main.terminalArray[Main.nextTerminal];
        } else return END;
    }

    //increment the "pointer" to the array
    public static void incrementTerminal() {
        if(Main.nextTerminal < (Main.arraySize) ) {
            Main.nextTerminal++;
       }
    }

    //production 1: null
    //production 2
    //find character (, then
    //find a string of balanced (), then
    //find the character ), then finally
    //find another string of balanced ()
    public static nodeStruct B() {
        nodeStruct firstB;
        nodeStruct secondB;
        if(getNextTerminal() == '(') { //production 2
            incrementTerminal();
            firstB = B();
            if((firstB.getLabel() != FAILED.getLabel()) && (getNextTerminal() == ')')) {
                incrementTerminal();
                secondB = B();
                if(secondB.getLabel() == FAILED.getLabel()) return FAILED;
                else return makeNode4('B',makeNode0('('), firstB, makeNode0(')'), secondB);
            } else return FAILED; //first call to B failed
        } else return makeNode1('B', makeNode0('e')); //production 1
    }

    //main, for running tests and calling B()
    public static void main(String args[]) {
        System.out.println("Andrew Yaros");
        System.out.println("CS 360 PA 1");
        System.out.println("PART 3: Recursive descend parser from FCS");
        System.out.println("Running tests on strings");
        System.out.println();

        FAILED.setLabel('F'); //failures are denoted by "F"

        //array of sample strings to test
        ArrayList<String> tests = new ArrayList<>();
        tests.add("()()");
        tests.add("()");
        tests.add("(())");
        tests.add("(");
        tests.add("(())(())()");
        tests.add("(C()(())()");
        for(String s: tests) {
            //print s
            System.out.println(s);

            //set index of pointer
            nextTerminal = 0;

            //convert string to char array
            terminalArray = s.toCharArray();
            arraySize = terminalArray.length;

            //make parse tree
            parseTree = B();

            //calculate height
            System.out.print("Height:     ");
            parseTree.computeHeight();
            System.out.println(parseTree.getHeight());

            //print stuff
            System.out.print("Pre-order:  ");
            parseTree.printPreorder();
            System.out.println();

            System.out.print("Post-order: ");
            parseTree.printPostorder();
            System.out.println();
            System.out.println();
        }
    }
}
/*
* Problem III (30 points). In C++, Python or Java:
 * Implement the recursive-descend parser of FCS
 * section 11.6 constructing parse trees of the
 * grammar of balanced parentheses (FCS figure 11.25;
 * students are allowed to use directly the code
 * of FCS section 11.6). After constructing a parse
 *tree your algorithm should compute its height
 * and list all labels in pre-order and post-order.
 * Demonstrate with examples that your code operates
 * properly.
 *
    *
 */