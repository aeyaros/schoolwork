public class InviteResult {
    private InviteState state;
    private Game newGame;

    public InviteResult(InviteState state, Game newGame) {
        this.state = state;
        this.newGame = newGame;
    }

    public InviteState getState() {
        return state;
    }

    public Game getNewGame() {
        return newGame;
    }
}
