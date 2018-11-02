import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static Map<String, Game> activeGames = new ConcurrentHashMap<>();
    private static Map<String, String> awaitingRematch = new ConcurrentHashMap<>();
    private static Map<String, String> awaitingInvite = new ConcurrentHashMap<>();
    private static Set<String> awaitingRandom = Collections.synchronizedSet(new HashSet<>());
    private static Map<String, ArrayList<Game>> history = new ConcurrentHashMap<>();

    /*
    create a new game with two players
     */
    public static Game newGame(String p1, String p2) {
        if (activeGames.containsKey(p1) || activeGames.containsKey(p2)) {
            return null;
        }
        Game g = new Game(new Player(p1), new Player(p2));
        activeGames.put(p1, g);
        activeGames.put(p2, g);
        return g;
    }

    /*
    remove a game from the map of active games
     */
    public static void endGame(String p) {
        Game g = activeGames.remove(p);
        if (g != null) {
            ArrayList<Game> playerHistory = history.getOrDefault(p, null);
            if (playerHistory == null) {
                playerHistory = new ArrayList<>();
                history.put(p, playerHistory);
            }
            playerHistory.add(g);
        }
    }

    public static Game getGame(String p) {
        return activeGames.getOrDefault(p, null);
    }

    /*
    process a rematch request
     */
    public static RematchResult rematch(String requester, String opponent) {
        if (activeGames.containsKey(opponent) && !activeGames.containsKey(requester)) {
            return new RematchResult(RematchState.REJECTED, null);
        }
        if (activeGames.containsKey(opponent)) {
            return new RematchResult(RematchState.ACCEPTED, activeGames.get(opponent));
        }
        if (activeGames.containsKey(requester)) {
            return new RematchResult(RematchState.REJECTED, null);
        }
        if (awaitingRematch.getOrDefault(opponent, "").equals(requester)) {
            awaitingRematch.remove(opponent);
            awaitingRematch.remove(requester);
            return new RematchResult(RematchState.ACCEPTED, newGame(requester, opponent));
        }
        awaitingRematch.putIfAbsent(requester, opponent);
        return new RematchResult(RematchState.PENDING, null);
    }

    /* process a request for a random game */
    public static RandomResult random(String requester) {
        if (activeGames.containsKey(requester)) {
            return new RandomResult(RandomState.ACCEPTED, activeGames.get(requester));
        }
        for (String awaiting : awaitingRandom) {
            if (!awaiting.equals(requester)) {
                awaitingRandom.remove(awaiting);
                awaitingRandom.remove(requester);
                return new RandomResult(RandomState.ACCEPTED, newGame(awaiting, requester));
            }
        }
        awaitingRandom.add(requester);
        return new RandomResult(RandomState.PENDING, null);
    }

    /*
    process an invite request
     */
    public static InviteResult invite(String requester, String opponent) {
        if (activeGames.containsKey(opponent) && !activeGames.containsKey(requester)) {
            return new InviteResult(InviteState.REJECTED, null);
        }
        if (activeGames.containsKey(opponent)) {
            return new InviteResult(InviteState.ACCEPTED, activeGames.get(opponent));
        }
        if (activeGames.containsKey(requester)) {
            return new InviteResult(InviteState.REJECTED, null);
        }
        if (awaitingInvite.getOrDefault(opponent, "").equals(requester)) {
            awaitingInvite.remove(opponent);
            awaitingInvite.remove(requester);
            return new InviteResult(InviteState.ACCEPTED, newGame(requester, opponent));
        }
        awaitingInvite.putIfAbsent(requester, opponent);
        return new InviteResult(InviteState.PENDING, null);
    }

    public static ArrayList<Game> getHistory(String p){
        return history.getOrDefault(p, null);
    }
}
