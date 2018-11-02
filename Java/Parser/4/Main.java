/*
 * Andrew Yaros
 * CS 360 PA 1
 * Part 4: Table-driven parser
 */

import java.util.ArrayList;
import java.util.Stack;

public class Main {
    //terminal parse symbol tokens
    public static final parseToken w  = new parseToken("w", true);   // while
    public static final parseToken c  = new parseToken("c", true);   // condition
    public static final parseToken lp = new parseToken("{", true);   // {
    public static final parseToken rp = new parseToken("}", true);   // }
    public static final parseToken s  = new parseToken("s", true);   // simple statement
    public static final parseToken sc = new parseToken(";", true);   // ;
    public static final parseToken en = new parseToken("ENDM", true);// end of line
    //non-terminal production symbol tokens
    public static final parseToken PrS = new parseToken("<S>", false);
    public static final parseToken PrT = new parseToken("<T>", false);

    //figure 11.32
    //parse table------------------------------index 0  1  2   3   4  5   6
    public static final parseToken[] symbolChars = { w, c, lp, rp, s, sc, en };
    public static final int[][] productionInts  = {{ 1, 0, 2 , 0 , 3, 0 , 0 },  //PrS index 0
                                                   { 4, 0, 4 , 5 , 4, 0 , 0 }}; //PrT index 1
    public static final parseToken ProducList[] = {PrS, PrT};
    //a value of 0 means no production

    //table lookup function, returns the number of the production (NOT THE INDEX)
    public static int productionNumberLookup(parseToken nonTermRow, parseToken symbolColumn) {

        int prodIndex = -1;//index of production, row
        int symbIndex = -1;//index of symbol, column

        //find i and j; loop through lists of valid tokens to check for matches
        for(int i = 0; i < ProducList.length; i++) {
            if(nonTermRow.tokenCompare(ProducList[i])) {
                prodIndex = i;
                for(int j = 0; j < symbolChars.length; j++) {
                    if(symbolColumn.tokenCompare(symbolChars[j])) {
                        symbIndex = j;
                        break;
                    }
                } break;
            }
        }
        if(prodIndex == -1 || symbIndex == -1) return -1; //error!
        else { //if successful, then get int from table - this is the production number
            int tableValue = productionInts[prodIndex][symbIndex];
            if(tableValue == 0 || tableValue == -1) return -1; //error!
            else return tableValue; //the number of the production to use
            //NOT THE INDEX OF THE PRODUCTION; the NUMBER OF THE PRODUCTION
        }
    }

    //figure 11.33
    //productions--------------------------------------    ->
    public static final production Pr1 = new production(PrS, w  , c  , PrS);
    public static final production Pr2 = new production(PrS, lp , PrT     );
    public static final production Pr3 = new production(PrS, s  , sc      );
    public static final production Pr4 = new production(PrT, PrS, PrT     );
    public static final production Pr5 = new production(PrT, rp           );
    //list of productions
    public static final production PrList[] = {Pr1, Pr2, Pr3, Pr4, Pr5};


    //reverse a stack, so end of input is at the bottom
    public static Stack<parseToken> reverseTokens(Stack<parseToken> Stk) {
        Stack<parseToken> reverseStack = new Stack<>();
        //remove objects at top of original stack
        //place at bottom of new stack
        while(!Stk.empty()) reverseStack.push(Stk.pop());
        return reverseStack;
    }

    //take an input string and turn it into a stack of tokens
    public static Stack<parseToken> stringParser(String strarg){
        char[] CharArr = strarg.toCharArray();
        Stack<parseToken> tokStk = new Stack<>();
        for(int i = 0; i < CharArr.length; i++) {
            char curChar = CharArr[i]; //current character
            switch (curChar) {
                case 'w': tokStk.push(w);
                    break;
                case 'c': tokStk.push(c);
                    break;
                case '{': tokStk.push(lp);
                    break;
                case '}': tokStk.push(rp);
                    break;
                case 's': tokStk.push(s);
                    break;
                case ';': tokStk.push(sc);
                    break;
                case 'E': if(i+3 < CharArr.length &&
                              CharArr[i+1] == 'N' &&
                              CharArr[i+2] == 'D' &&
                              CharArr[i+3] == 'M') {
                    tokStk.push(en); //push ending token to stack
                    //reverse stack, so the first part of expression is at top of stack
                    return reverseTokens(tokStk); //there cant be anything after the end, so return
                    }
                default: break;
            }
        }
        //if we get here, there was no end token, and we must have one
        tokStk.push(en); //add an ending token
        //then, reverse and return
        return reverseTokens(tokStk);
    }

    //store parsed input here, so we can call driver with it
    public Stack<parseToken> inputStack;

    //use this function on an input of tokens
    //gets a parse tree from the expression, if possible
    //also prints a pretty output of the diagram of the stack
    public static nodeStruct driver(Stack<parseToken> remainingInput){
        //stack for remaining input is the argument
        //create parse stack
        Stack<parseToken> parseStack = new Stack<>();

        //set parse stack to <S>
        parseStack.push(PrS);

        //lookahead variable; pop first symbol in lookahead
        parseToken lookAhead = remainingInput.pop();

        //initialize tree
        nodeStruct theParseTree = new nodeStruct();

        //formatting
        String divider = "------------------|------------------------  ";
        System.out.println(divider);

        int err = 0;

        // while lookahead is not ENDM
        while(!lookAhead.tokenCompare(en)) {
            //print output status
            System.out.print("Parse stack:      |  ");
            printStackTokens(parseStack);
            System.out.print("Lookahead:        |  ");
            System.out.print(lookAhead.getTokenString());
            System.out.println();
            System.out.print("Remaining input:  |  ");
            printStackTokens(remainingInput);
            System.out.println(divider);

            //check if parseStack is empty; we can't do anything, so the grammar must be wrong
            if(parseStack.empty()) {
                err = -1;
                System.out.println("Error: Parse stack empty; incorrect grammar.");
                return null;
            }
            //** if token at top of parse stack is nonterminal
            else if(!parseStack.peek().getIsTerminal()) {
                //**** it is the current root node
                theParseTree.setToken(parseStack.peek());

                //**** look up in parse table
                //****** top production in parse stack is the row of the table
                //****** lookahead is the column
                int prodNum = productionNumberLookup(parseStack.peek(), lookAhead);
                if(prodNum != -1) {
                    //******** find the corresponding production
                    production currentProduction = PrList[prodNum - 1]; //subtract 1 for index

                    //******** then replace current nonterminal in parse stack with symbols inside production
                    //get arraylist from production
                    ArrayList<parseToken> newSymbols = currentProduction.getRightSideList();

                    //pop current nonterminal symbol
                    parseStack.pop();
                    //then replace it with symbols from the production

                    //go through arraylist of new symbols in reverse order
                    //first symbol in the list needs to be pushed last
                    //so it is at the top of the parse stack
                    for(int i = newSymbols.size() - 1; i >= 0; i--){
                        parseStack.push(newSymbols.get(i));
                    }

                    //******** add those symbols as nodes to the current root
                    //if we choose to replace s by the body of a production
                    //we get the leaf of the parse tree corresponding to that s
                        //the leftmost leaf that is nonterminal
                    //we give it n children from the production
                    theParseTree.setLeftMostLeaf(newSymbols);

                } else { //error if either 0 in table, or out of bounds issue
                    //error! no production found for combo of lookahead and top of stack
                    err = -2;
                    System.out.println("Error: Production not found; incorrect grammar in expression.");
                    return null;
                }
            } else { //**** else, is terminal at top of stack
                //****** if so, does terminal in parse stack = lookahead
                if(parseStack.peek().tokenCompare(lookAhead)) {
                    //******** if so, pop the terminal from parse stack
                    parseStack.pop();

                    //********** get next lookahead from input
                    lookAhead = remainingInput.pop();

                } else { //******** if not, error
                    //terminal on top of stack doesnt match lookahead
                    err = -3;
                    System.out.println("Error: Terminal on top of stack doesn't match lookahead; incorrect grammar.");
                    return null;
                }
            }
        }
        if(err == 0){
            //output FINAL status
            System.out.print("Parse stack:      |  ");
            printStackTokens(parseStack);
            System.out.print("Lookahead:        |  ");
            System.out.print(lookAhead.getTokenString());
            System.out.println();
            System.out.print("Remaining input:  |  ");
            printStackTokens(remainingInput);
            System.out.println(divider);
            System.out.println();

            //return the tree
            return theParseTree;
        } else return null; //if error
    }

    public static void printStackTokens(Stack<parseToken> input) {
        if(input.empty()){
            System.out.println("e");
        } else {
            //stack is printed from bottom to top, apparently
            //thus, reverse the printout so it is in left to right order
            for(int i = input.size()-1; i >= 0; i--) {
                System.out.print(input.get(i).getTokenString());
                //System.out.print(' '); //spaces between tokens
            } System.out.println(); //print newline when done
        }
    }

    public static void main(String args[]) {
        System.out.println("Andrew Yaros");
        System.out.println("CS 360 PA 1");
        System.out.println("PART 4: Table-driven Parser");
        System.out.println("Running tests on strings");
        System.out.println();

        String horizontalLine = "-------------------------------------------";

        //list of test strings
        ArrayList<String> tests = new ArrayList<>();
        tests.add("{w c s ; s ; }ENDM");
        tests.add(" wc s;");
        tests.add("{w c s ; s ; wc {wcs;}  }");

        //testing errors
        tests.add("{ s cc w s;"); //missing bracket
        tests.add(" w c s; { w c s s s ; }"); //apparently, you cant have statement outside of brackets, empty parse tree
        tests.add("{w c { c s ;  wcs}cs; }ENDM"); //incorrect production
        //note: parser ignores any irrelevant characters, and automatically adds ENDM if it doesnt exist

        //loop throgh test strings, run tests
        for(String testString: tests){
            System.out.println(horizontalLine);
            System.out.println();
            System.out.print("Testing: ");
            System.out.println(testString);
            System.out.println();
            Stack<parseToken> parsedInput = stringParser(testString);
            //parse the input and get back a tree
            nodeStruct newParseTree = driver(parsedInput);

            if(newParseTree != null) {
                //get height of tree
                newParseTree.computeHeight();
                System.out.print("Height of tree:    ");
                System.out.print(newParseTree.getHeight());
                System.out.println();

                //print out tokens
                //preorder
                System.out.print("Pre-order tokens:  ");
                newParseTree.printPreorder();
                System.out.println();
                //postorder
                System.out.print("Post-order tokens: ");
                newParseTree.printPostorder();
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }
}

//all the println()s are what I get for not abstracting I/O
//...      :/

/*
<L> → <L><S> | ε

(1) <S> → wc<S>
(2) <S> → {<T>
(3) <S> → s;
(4) <T> → <S><T>
(5) <T> → }

Figure 11.33

Problem IV (40 points). In C++, Python or Java:
Implement the table driven parsing algorithm of
FCS section 11.7 constructing parse trees of the
grammar of simple programming statements (FCS
figure 11.33). Your algorithm should make use of
a stack as explained in FCS figure 11.34. After
constructing a parse tree your algorithm should
compute its height and list all labels in
pre-order and post-order. Demonstrate with
examples that your code operates properly.
*/