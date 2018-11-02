public class GameResult {
    private Player winner, loser;
    private GameState state;

    public GameResult(Player winner, Player loser, GameState state) {
        this.winner = winner;
        this.loser = loser;
        this.state = state;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return loser;
    }

    public GameState getState() {
        return state;
    }
}
