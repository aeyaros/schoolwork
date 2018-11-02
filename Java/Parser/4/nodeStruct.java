import java.util.ArrayList;

//node for trees
public class nodeStruct {
    private parseToken token;   //node data
    private ArrayList<nodeStruct> children;
    private int height;

    public nodeStruct() {
        this.token = null;
        this.children = new ArrayList<>();
        this.height = 0;
    }

    //specify node data
    public nodeStruct(parseToken tok) {
        this.token = tok;
        this.children = new ArrayList<>();
        this.height = 0;
    }

    public void addChild(parseToken tok) { this.children.add(new nodeStruct(tok)); }
    public void setToken(parseToken tok) { this.token = tok; }

    //checks through the tree for the leftmost leaf
    //returns a boolean so we can check for deadends
    //we only end up returning true if we actually set a token
    public Boolean setLeftMostLeaf(ArrayList<parseToken> tokList) {
        //if this is a leaf which is not null and not terminal
        if(this.children.isEmpty() && this.token != null && !(this.token.getIsTerminal())) {
            //then set this node's children to tokens from the production
            for(parseToken p: tokList) {
                this.addChild(p); //add production tokens one by one
            }
            return true; //successful
        } else if(this.children.size() > 0) {
            //if there are children check the children
            for(nodeStruct node: this.children) {
                //try this on each of the children
                //if we find the leftmost nonterminal leaf
                //then we found it and stop the loop
                if(node.setLeftMostLeaf(tokList)) return true;
            } //if we get here, we checked all the children and can return false
            return false;
        } else { //this node has no children and is a terminal or null leaf
            return false; //this is a dead end
        }
    }

    public void printPreorder() {
        //first print current node
        if(this.token != null) {
            System.out.print(this.token.getTokenString());
            System.out.print(" ");
        } else System.out.print("null ");
        //then print the subtrees in preorder
        if(!(this.children.isEmpty())) {
            for(nodeStruct node: this.children) {
                node.printPreorder();
            }
        }
    }

    public void printPostorder() {
        //if not leaf, print children in post order
        if(!(this.children.isEmpty())) {
            for(nodeStruct node: this.children) {
                node.printPostorder();
            }
        } //print current node
        if(this.token != null) {
            System.out.print(this.token.getTokenString());
            System.out.print(" ");
        } else System.out.print("null ");
    }

    public int getHeight() { return height; }

    //get height of the tree
    public void computeHeight() {
        this.height = 0;
        if(!this.children.isEmpty()){
            for(nodeStruct c:this.children) {
                c.computeHeight();
                if(c.getHeight() >= this.height) {
                    this.height = (1 + c.getHeight());
                }
            }
        }
    }
}