import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        board = new Board(8, 8);
    }

    @org.junit.jupiter.api.Test
    void side() {
        assertEquals(1, board.side(new Piece(1, 1, 1)));
        assertEquals(0, board.side(new Piece(2, 7, 1)));
    }

    @org.junit.jupiter.api.Test
    void slot() {
        assertEquals(0, board.slot(new Piece(1, 1, 1)));
        assertEquals(1, board.slot(new Piece(2, 7, 1)));
    }

    @org.junit.jupiter.api.Test
    void moveInvalid() {
        assertEquals(MoveState.INVALID, board.move(1, 0, 1200, 1200).getState());
        assertEquals(MoveState.INVALID, board.move(0, 0, 1, 1).getState());
        assertEquals(MoveState.INVALID, board.move(2, 1, 1, 0).getState());
        assertEquals(MoveState.INVALID, board.move(1, 0, 2, 1).getState());
        board.move(4, 5, 3, 4);
        board.move(3, 4, 2, 3);
        board.move(3, 2, 1, 4);
        assertEquals(MoveState.INVALID, board.move(1, 2, 4, 5).getState());
        assertEquals(MoveState.INVALID, board.move(1, 2, 3, 4).getState());
        board.move(4, 1, 3, 2);
        board.move(3, 2, 2, 3);
        assertEquals(MoveState.INVALID, board.move(1, 2, 3, 4).getState());
        board.move(6, 5, 5, 4);
        board.move(5, 4, 4, 3);
        assertEquals(MoveState.INVALID, board.move(5, 2, 6, 3).getState());
    }

    @org.junit.jupiter.api.Test
    void moveCaptureDone() {
        board.move(4, 5, 3, 4);
        board.move(3, 4, 2, 3);
        assertEquals(MoveState.DONE, board.move(3, 2, 1, 4).getState());
    }

    @org.junit.jupiter.api.Test
    void moveCaptureContinue() {
        board.move(4, 5, 3, 4);
        board.move(3, 4, 2, 3);
        board.move(5, 6, 4, 5);
        assertEquals(MoveState.CONTINUE, board.move(1, 2, 3, 4).getState());
    }

    @org.junit.jupiter.api.Test
    void capture() {
        board.capture(board.getPiecesInPlay().get(0).get(0));
        assertEquals(0, board.getCapturedPieces().get(0).get(0).getId());
        assertNull(board.getPiecesInPlay().get(0).get(0));
    }

    @org.junit.jupiter.api.Test
    void canCapture() {
        board.move(4, 5, 3, 4);
        board.move(3, 4, 2, 3);
        assertTrue(board.canCapture(3, 2));
    }

    @org.junit.jupiter.api.Test
    void crown() {
        board.capture(board.getPiecesInPlay().get(0).get(0));
        assertTrue(board.crown(board.getPiecesInPlay().get(0).get(1)) instanceof King);
    }

    @org.junit.jupiter.api.Test
    void getLegalMoves() {
        ArrayList<ArrayList<Boolean>> legalMoves = board.getLegalMoves(0);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (i == 3 && j % 2 == 0) {
                    assertTrue(legalMoves.get(i).get(j));
                }
                else {
                    assertFalse(legalMoves.get(i).get(j));
                }
            }
        }
        legalMoves = board.getLegalMoves(1);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (i == 4 && j % 2 == 1) {
                    assertTrue(legalMoves.get(i).get(j));
                }
                else {
                    assertFalse(legalMoves.get(i).get(j));
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void inStalemate() {
        assertFalse(board.inStalemate());
        board.move(5, 2, 4, 3);
        board.move(4, 3, 3, 4);
        board.move(6, 1, 5, 2);
        board.move(5, 2, 6, 3);
        board.move(2, 5, 4, 3);
        board.move(7, 0, 6, 1);
        board.move(6, 1, 5, 2);
        board.move(4, 3, 6, 1);
        board.move(6, 1, 7, 0);
        for (int i = 0; i < 40; ++i) {
            if (i % 2 == 0) {
                board.move(7, 0, 6, 1);
            }
            else {
                board.move(6, 1, 7, 0);
            }
        }
        assertTrue(board.inStalemate());
    }
}