import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;

    @BeforeEach
    void setUp() {
        game = GameManager.newGame("one", "two");
    }

    @AfterEach
    void tearDown() {
        GameManager.endGame("one");
        GameManager.endGame("two");
    }

    @Test
    void checkGameStateTie() {
        game.move(5, 2, 4, 3);
        game.move(4, 3, 3, 4);
        game.move(6, 1, 5, 2);
        game.move(5, 2, 6, 3);
        game.move(2, 5, 4, 3);
        game.move(7, 0, 6, 1);
        game.move(6, 1, 5, 2);
        game.move(4, 3, 6, 1);
        game.move(6, 1, 7, 0);
        for (int i = 0; i < 40; ++i) {
            if (i % 2 == 0) {
                game.move(7, 0, 6, 1);
            }
            else {
                game.move(6, 1, 7, 0);
            }
        }
        assertEquals(GameState.TIE, game.checkGameState().getState());
    }

    @Test
    void checkGameStateComplete() {
        for (Piece p : game.getBoard().getPiecesInPlay().get(0)) {
            game.getBoard().capture(p);
        }
        assertEquals(GameState.COMPLETE, game.checkGameState().getState());
        assertNotEquals(null, game.checkGameState().getWinner());
        assertNotEquals(null, game.checkGameState().getLoser());
    }

    @Test
    void checkGameStateInProgress() {
        assertEquals(GameState.IN_PROGRESS, game.checkGameState().getState());
    }

    @Test
    void checkGameStateTerminated() {
        GameManager.endGame("one");
        assertEquals(GameState.TERMINATED, game.checkGameState().getState());
    }
}