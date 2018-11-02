public class RandomResult {
    private RandomState state;
    private Game newGame;

    public RandomResult(RandomState state, Game newGame) {
        this.state = state;
        this.newGame = newGame;
    }

    public RandomState getState() {
        return state;
    }

    public Game getNewGame() {
        return newGame;
    }
}
