public class RematchResult {
    private RematchState state;
    private Game newGame;

    public RematchResult(RematchState state, Game newGame) {
        this.state = state;
        this.newGame = newGame;
    }

    public RematchState getState() {
        return state;
    }

    public Game getNewGame() {
        return newGame;
    }
}
