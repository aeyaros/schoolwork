public class parseToken {
    private String tokenData;
    private Boolean isTerminal;

    public parseToken() {
        this.tokenData = null;
        this.isTerminal = null;
    }

    public parseToken(String data, Boolean term) {
        this.tokenData = data;
        this.isTerminal = term;
    }

    public String getTokenString() { return tokenData; }
    public Boolean getIsTerminal() { return isTerminal; }

    //compare two tokens
    public Boolean tokenCompare(parseToken otherToken) {
        //noinspection RedundantIfStatement
        if(this.getTokenString().equals(otherToken.getTokenString()) &&
           this.getIsTerminal().booleanValue() == otherToken.getIsTerminal().booleanValue()) {
            return true;
        } else return false;
    }
}