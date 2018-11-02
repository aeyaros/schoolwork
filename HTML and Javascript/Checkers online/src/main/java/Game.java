import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game {
    private Board board;
    private ArrayList<Player> players;
    private int hasControl;

    public Game(Player one, Player two) {
        players = prioritize(one, two);
        board = new Board(8, 8);
        hasControl = 1;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> prioritize(Player one, Player two) {
        ArrayList<Player> p = new ArrayList<>();
        boolean player_one_first = (new Random()).nextBoolean();
        if (player_one_first) {
            p.add(one);
            p.add(two);
        }
        else {
            p.add(two);
            p.add(one);
        }
        return p;
    }

    public MoveResult move(int oldX, int oldY, int newX, int newY) {
        MoveResult m = board.move(oldX, oldY, newX, newY);
        if (m.getState() == MoveState.DONE) {
            hasControl = (hasControl + 1) % players.size();
        }
        return m;
    }

    public GameResult checkGameState() {
        for (int i = 0; i < players.size(); ++i) {
            boolean hasLegalMove = false;
            for (ArrayList<Boolean> row : board.getLegalMoves(i)) {
                for (Boolean canMove : row) {
                    if (canMove) {
                        hasLegalMove = true;
                    }
                }
            }
            if (!hasLegalMove) {
                return new GameResult(players.get((i + 1) % 2), players.get(i), GameState.COMPLETE);
            }
            else if (GameManager.getGame(players.get(i).getUuid()) == null) {
                return new GameResult(null, null, GameState.TERMINATED);
            }
        }
        if (board.inStalemate()) {
            return new GameResult(null, null, GameState.TIE);
        }
        return new GameResult(null, null, GameState.IN_PROGRESS);
    }

    public int getHasControl() {
        return hasControl;
    }
}
