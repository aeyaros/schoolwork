public class MoveResult {
    private MoveState state;
    private int newX, newY;
    private Piece capture;
    private String description;

    public MoveResult(MoveState state, int newX, int newY, Piece capture, String description) {
        this.state = state;
        this.newX = newX;
        this.newY = newY;
        this.capture = capture;
        this.description = description;
    }

    public MoveState getState() {
        return state;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public Piece getCapture() {
        return capture;
    }

    public String getDescription() {
        return description;
    }
}
