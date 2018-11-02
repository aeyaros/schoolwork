import java.util.ArrayList;

//basically a binary tree
//although it uses an arraylist to hold children
public class nodeStruct {
    private char label; //node data
    private ArrayList<nodeStruct> children;
    private int height;

    //default
    public nodeStruct() {
        this.label = 'N';
        this.children = new ArrayList<>();
        this.height = 0;
    }

    //specify node data
    public nodeStruct(char l) {
        this.label = l;
        this.children = new ArrayList<>();
        this.height = 0;
    }

    public char getLabel() { return this.label; }
    public void setLabel(char l) { this.label = l; }
    public int getHeight() { return height; }

    //set a child at an index
    //leftmost child = 0
    //right sibling  = 1
    public void setI(int i, nodeStruct n) {
        if(this.children.isEmpty()){
            if(i == 0) { //if empty, add two new nodes
                this.children.add(n); //leftmost child
                this.children.add(new nodeStruct('N')); //null right
            } else {
                this.children.add(new nodeStruct('N')); //null left
                this.children.add(n); //right sibling
            }
        } else if(this.children.size() == 1) { //if only left exists
            if(i == 0) this.children.set(i, n); //set left
            this.children.add(new nodeStruct('N')); //new null right
        } else { //else, just set 0 or 1 to n
            this.children.set(i, n);
        }
    }
    //print out tokens in preorder
    public void printPreorder() {
        //first print current node
        if(this.label != 'N') {
            System.out.print(this.label);
            System.out.print(" ");
        } //then print the subtrees in preorder
        if(!(this.children.isEmpty())) {
            for(nodeStruct node: this.children) {
                node.printPreorder();
            }
        }
    }

    //print out tokens in postorder
    public void printPostorder() {
        //if not leaf, print children in post order
        if(!(this.children.isEmpty())) {
            for(nodeStruct node: this.children) {
                node.printPostorder();
            }
        } //print current node
        if(this.label != 'N') {
            System.out.print(this.label);
            System.out.print(" ");
        }
    }

    //get height of the tree, as seen in my data structures textbook from 260
    public void computeHeight() {
        this.height = 0; //if not empty...
        if(!this.children.isEmpty()){
            //...check each child
            for(nodeStruct c:this.children) {
                c.computeHeight();
                if(c.getHeight() >= this.height) { //recursively get heights from below c
                    this.height = (1 + c.getHeight());
                } // +1, because we account for the child to the current node
            }
        }
    }
}