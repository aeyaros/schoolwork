import java.util.ArrayList;
import java.util.Objects;

public class Board {
    private static double MAX_MOVE_DISTANCE = Math.sqrt(8);
    private static double MIN_MOVE_DISTANCE = Math.sqrt(2);
    private ArrayList<ArrayList<Piece>> layout;
    private ArrayList<ArrayList<Piece>> piecesInPlay;
    private ArrayList<ArrayList<Piece>> capturedPieces;
    private ArrayList<String> moveList;
    private int moveCount, lastCapture;

    public Board(int xSize, int ySize) {
        piecesInPlay = new ArrayList<>(2);
        capturedPieces = new ArrayList<>(2);
        for (int i = 0; i < 2; i++) {
            piecesInPlay.add(new ArrayList<>(12));
            capturedPieces.add(new ArrayList<>(12));
        }

        layout = new ArrayList<>(ySize);
        int red_piece_id = 0;
        int black_piece_id = 1;
        for (int i = 0; i < ySize; i++) {
            layout.add(new ArrayList<>(xSize));
            for (int j = 0; j < xSize; ++j) {
                if (i < 3 && (j + i) % 2 == 1) {
                    Piece p = new Piece(red_piece_id, j, i);
                    layout.get(i).add(p);
                    piecesInPlay.get(side(p)).add(p);
                    capturedPieces.get(side(p)).add(null);
                    red_piece_id += 2;
                }
                else if (i > 4 && (j + i) % 2 == 1) {
                    Piece p = new Piece(black_piece_id, j, i);
                    layout.get(i).add(p);
                    piecesInPlay.get(side(p)).add(p);
                    capturedPieces.get(side(p)).add(null);
                    black_piece_id += 2;
                }
                else {
                    layout.get(i).add(null);
                }
            }
        }

        moveCount = 0;
        lastCapture = 0;
        moveList = new ArrayList<>();
    }

    public int side(Piece p) {
        return p.getId() % 2;
    }

    public int slot(Piece p) {
        return p.getId() / 2;
    }

    public MoveResult move(int oldx, int oldy, int newx, int newy) {
        if ((newy > layout.size() || newy < 0) || (newx > layout.get(0).size() || newx < 0)) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Tried to move off board");
        }
        Piece p = layout.get(oldy).get(oldx);
        if (p == null) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "No piece at specified coordinates");
        }
        int side = side(p);
        if (!(p instanceof King) && ((side == 0 && newy < oldy) || (side == 1 && newy > oldy))) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Tried to move non-king backwards");
        }
        Piece atNewSpace = layout.get(newy).get(newx);
        if (atNewSpace != null) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Tried to move to occupied space");
        }
        double move_dist = Math.hypot(newx - oldx, newy - oldy);
        if (move_dist > MAX_MOVE_DISTANCE) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Invalid move distance");
        }
        Piece capture = null;
        String description = String.format("Move (%d, %d) to (%d, %d)", oldx, oldy, newx, newy);
        if (move_dist > MIN_MOVE_DISTANCE) {
            int dX = newx > oldx ? 1 : -1;
            int dY = newy > oldy ? 1 : -1;
            capture = layout.get(newy - dY).get(newx - dX);
            if (capture == null || side(capture) == side(p)) {
                return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Invalid capture");
            }
            description = String.format("%s; Capture (%d, %d)", description, capture.getX(), capture.getY());
            capture(capture);
        }
        else if (piecesInPlay.get(side(p)).stream().anyMatch(this::canCapture)) {
            return new MoveResult(MoveState.INVALID, oldx, oldy, null, "Must capture");
        }
        p.setX(newx);
        p.setY(newy);
        boolean crowned = false;
        if (!(p instanceof King) && ((side(p) == 1 && newy == 0) || (side(p) == 0 && newy == layout.size() - 1))) {
            p = crown(p);
            piecesInPlay.get(side(p)).set(slot(p), p);
            description = String.format("%s; Crown (%d, %d)", description, p.getX(), p.getY());
            crowned = true;
        }
        layout.get(newy).set(newx, p);
        layout.get(oldy).set(oldx, null);
        moveList.add(description);
        MoveState state;
        if (capture != null && canCapture(p.getX(), p.getY()) && !crowned) {
            state = MoveState.CONTINUE;
        } else {
            state = MoveState.DONE;
        }
        ++moveCount;
        return new MoveResult(state, newx, newy, capture, description);
    }

    public void capture(Piece p) {
        piecesInPlay.get(side(p)).set(slot(p), null);
        capturedPieces.get(side(p)).add(slot(p), p);
        layout.get(p.getY()).set(p.getX(), null);
        lastCapture = moveCount + 1;
    }

    public boolean canCapture(int x, int y) {
        int ySize = layout.size();
        int xSize = layout.get(0).size();
        if (x > xSize || x < 0 || y > ySize || y < 0) {
            return false;
        }
        Piece p = layout.get(y).get(x);
        if (p == null) {
            return false;
        }
        int upperX = x + 1;
        int lowerX = upperX - 2;
        int upperY = y + 1;
        int lowerY = upperY - 2;
        for (int j = upperX; j >= lowerX; j -= 2) {
            for (int i = upperY; i >= lowerY; i -= 2) {
                if (!(p instanceof King) && ((side(p) == 0 && i < y)) || (side(p) == 1 && i > y)) {
                    continue;
                }
                if (j < xSize && j >= 0 && i < ySize && i >= 0) {
                    Piece atSpace = layout.get(i).get(j);
                    if (atSpace != null && side(atSpace) != side(p)) {
                        int x1 = j == upperX ? j + 1 : j - 1;
                        int y1 = i == upperY ? i + 1 : i - 1;
                        if (x1 < xSize && x1 >= 0 && y1 < ySize && y1 >= 0) {
                            if (layout.get(y1).get(x1) == null) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canCapture(Piece p) {
        if (p == null) {
            return false;
        }
        return canCapture(p.getX(), p.getY());
    }

    public King crown(Piece p) {
        ArrayList<Piece> captured = capturedPieces.get(side(p));
        if (captured.stream().anyMatch(Objects::nonNull)) {
            Piece hat = captured.stream().filter(Objects::nonNull).findFirst().get();
            captured.set(captured.indexOf(hat), null);
            return new King(p, hat);
        }
        return new King(p);
    }

    public ArrayList<ArrayList<Boolean>> getLegalMovesPiece(int x, int y) {
        ArrayList<ArrayList<Boolean>> legalMoves = new ArrayList<>();
        for (ArrayList<Piece> row : layout) {
            ArrayList<Boolean> movesRow = new ArrayList<>();
            for (Piece p : row) {
                movesRow.add(false);
            }
            legalMoves.add(movesRow);
        }
        int ySize = layout.size();
        int xSize = layout.get(0).size();
        if (x > xSize || x < 0 || y > ySize || y < 0) {
            return legalMoves;
        }
        Piece p = layout.get(y).get(x);
        if (p == null) {
            return legalMoves;
        }
        int upperX = x + 1;
        int lowerX = upperX - 2;
        int upperY = y + 1;
        int lowerY = upperY - 2;
        for (int j = upperX; j >= lowerX; j -= 2) {
            for (int i = upperY; i >= lowerY; i -= 2) {
                if (!(p instanceof King) && ((side(p) == 0 && i < y) || (side(p) == 1 && i > y))) {
                    continue;
                }
                if (j < xSize && j >= 0 && i < ySize && i >= 0) {
                    Piece atSpace = layout.get(i).get(j);
                    if (atSpace != null && side(atSpace) != side(p)) {
                        int x1 = j == upperX ? j + 1 : j - 1;
                        int y1 = i == upperY ? i + 1 : i - 1;
                        if (x1 < xSize && x1 >= 0 && y1 < ySize && y1 >= 0) {
                            if (layout.get(y1).get(x1) == null) {
                                legalMoves.get(y1).set(x1, true);
                            }
                        }
                    } else if (atSpace == null) {
                        legalMoves.get(i).set(j, true);
                    }
                }
            }
        }
        return legalMoves;
    }

    public ArrayList<ArrayList<Boolean>> getLegalMoves(int side) {
        ArrayList<ArrayList<Boolean>> legalMoves = new ArrayList<>();
        for (ArrayList<Piece> row : layout) {
            ArrayList<Boolean> movesRow = new ArrayList<>();
            for (Piece p : row) {
                movesRow.add(false);
            }
            legalMoves.add(movesRow);
        }
        for (ArrayList<Piece> row : layout) {
            for (Piece p : row) {
                if (p == null || side(p) != side) {
                    continue;
                }
                int upperX = p.getX() + 1;
                int lowerX = upperX - 2;
                int upperY = p.getY() + 1;
                int lowerY = upperY - 2;
                int ySize = layout.size();
                int xSize = row.size();
                for (int x = upperX; x >= lowerX; x -= 2) {
                    for (int y = upperY; y >= lowerY; y -= 2) {
                        if (!(p instanceof King) && ((side == 0 && y < p.getY()) || (side == 1 && y > p.getY()))) {
                            continue;
                        }
                        if (x < xSize && x >= 0 && y < ySize && y >= 0) {
                            Piece atSpace = layout.get(y).get(x);
                            if (atSpace != null && side(atSpace) != side) {
                                int x1 = x == upperX ? x + 1 : x - 1;
                                int y1 = y == upperY ? y + 1 : y - 1;
                                if (x1 < xSize && x1 >= 0 && y1 < ySize && y1 >= 0) {
                                    legalMoves.get(y1).set(x1, layout.get(y1).get(y1) == null);
                                }
                            }
                            else if (atSpace == null){
                                legalMoves.get(y).set(x, true);
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public boolean inStalemate() {
        return moveCount - lastCapture >= 40;
    }

    public ArrayList<String> getMoveList() {
        return moveList;
    }

    public ArrayList<ArrayList<Piece>> getPiecesInPlay() {
        return piecesInPlay;
    }

    public ArrayList<ArrayList<Piece>> getCapturedPieces() {
        return capturedPieces;
    }
}
