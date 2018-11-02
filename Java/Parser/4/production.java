import java.util.ArrayList;

public class production {
    private ArrayList<parseToken> rightSideList;
    private parseToken leftSideToken;

    public production(parseToken left, parseToken... right) {
        this.leftSideToken = left;
        this.rightSideList = new ArrayList<>();
        //noinspection ManualArrayToCollectionCopy
        for(parseToken pt: right) {
            this.rightSideList.add(pt);
        }
    }
    //public parseToken getLeftSideToken() { return leftSideToken; }
    public ArrayList<parseToken> getRightSideList() { return rightSideList; }
}
