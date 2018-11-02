import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @AfterEach
    void tearDown() {
        GameManager.endGame("one");
        GameManager.endGame("two");
    }

    @Test
    void newGame() {
        Game g = GameManager.newGame("one", "two");
        Set<String> players = new HashSet<>();
        players.add("one");
        players.add("two");
        for (Player p : g.getPlayers()) {
            players.remove(p.getUuid());
        }
        assertTrue(players.isEmpty());
    }

    @Test
    void endGame() {
        Game g = GameManager.newGame("one", "two");
        GameManager.endGame("one");
        GameManager.endGame("two");
        assertNull(GameManager.getGame("one"));
        assertNull(GameManager.getGame("two"));
    }

    @Test
    void getGame() {
        Game g = GameManager.newGame("one", "two");
        assertSame(g, GameManager.getGame("one"));
        assertSame(g, GameManager.getGame("two"));
    }

    @Test
    void rematch() {
        assertEquals(RematchState.PENDING, GameManager.rematch("one", "two").getState());
        assertEquals(RematchState.PENDING, GameManager.rematch("one", "two").getState());
        assertEquals(RematchState.ACCEPTED, GameManager.rematch("two", "one").getState());
        assertEquals(RematchState.REJECTED, GameManager.rematch("three", "two").getState());
        assertEquals(RematchState.ACCEPTED, GameManager.rematch("one", "two").getState());
        assertSame(GameManager.getGame("two"), GameManager.rematch("one", "two").getNewGame());
        assertEquals(RematchState.REJECTED, GameManager.rematch("one", "three").getState());
    }

    @Test
    void invite() {
        assertEquals(InviteState.PENDING, GameManager.invite("one", "two").getState());
        assertEquals(InviteState.PENDING, GameManager.invite("one", "two").getState());
        assertEquals(InviteState.ACCEPTED, GameManager.invite("two", "one").getState());
        assertEquals(InviteState.REJECTED, GameManager.invite("three", "two").getState());
        assertEquals(InviteState.ACCEPTED, GameManager.invite("one", "two").getState());
        assertSame(GameManager.getGame("two"), GameManager.invite("one", "two").getNewGame());
        assertEquals(InviteState.REJECTED, GameManager.invite("one", "three").getState());
    }

    @Test
    void random() {
        assertEquals(RandomState.PENDING, GameManager.random("one").getState());
        assertEquals(RandomState.ACCEPTED, GameManager.random("two").getState());
        assertSame(GameManager.getGame("one"), GameManager.random("one").getNewGame());
    }
}