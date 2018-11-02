import java.util.ArrayList;

public class GameStatusObject {
    private ArrayList<ArrayList<Integer>> currentBoard;
    private int playersTurn;
    private int playerColor;
    private int isOver;
    private int winner;
    private int userPawnsCaptured;
    private int userKingsCaptured;
    private int oppoPawnsCaptured;
    private int oppoKingsCaptured;
    private String opponentUuid;

    public GameStatusObject(ArrayList<ArrayList<Integer>> currentBoard, int playersTurn, int playerColor, int isOver,
                            int winner, int userPawnsCaptured, int userKingsCaptured, int oppoPawnsCaptured,
                            int oppoKingsCaptured, String opponentUuid) {
        this.currentBoard = currentBoard;
        this.playersTurn = playersTurn;
        this.playerColor = playerColor;
        this.isOver = isOver;
        this.winner = winner;
        this.userPawnsCaptured = userPawnsCaptured;
        this.userKingsCaptured = userKingsCaptured;
        this.oppoPawnsCaptured = oppoPawnsCaptured;
        this.oppoKingsCaptured = oppoKingsCaptured;
        this.opponentUuid = opponentUuid;
    }
}
