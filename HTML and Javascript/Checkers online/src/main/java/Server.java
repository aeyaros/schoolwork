import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        staticFiles.location("/public");
        staticFiles.expireTime(600);
        String playerIdKey = "playerId";
        int port = 443;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        port(port);

        get("/",(request,response)->{
            String uuid = request.cookie(playerIdKey);
         	if (uuid == null) {
         	    uuid = PlayerManager.getNewID();
         	    response.cookie(playerIdKey, uuid);
            }
            response.redirect("/home.html");
         	return "";
        });

        get("/statusUpdate", (request, response)->{
         	String uuid = request.cookie(playerIdKey);
         	Game game = GameManager.getGame(uuid);
         	if (game == null) {
         	    return "";
            }
            ArrayList<ArrayList<Integer>> currentBoard = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                ArrayList<Integer> row = new ArrayList<>();
                for (int j = 0; j < 8; ++j) {
                    row.add(0);
                }
                currentBoard.add(row);
            }
            Board gameBoard = game.getBoard();
            for (ArrayList<Piece> side : gameBoard.getPiecesInPlay()) {
                for (Piece p : side) {
                    if (p != null) {
                        int kingVal = p instanceof King ? 2 : 0;
                        int sideVal = gameBoard.side(p) + 1;
                        currentBoard.get(p.getY()).set(p.getX(), kingVal + sideVal);
                    }

                }
            }
            int playerSide = -1;
            ArrayList<Player> players = game.getPlayers();
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).getUuid().equals(uuid)) {
                    playerSide = i;
                }
            }
            int userPawnsCaptured = 0;
            int userKingsCaptured = 0;
            int oppoPawnsCaptured = 0;
            int oppoKingsCaptured = 0;
            for (ArrayList<Piece> side : gameBoard.getCapturedPieces()) {
                for (Piece p : side) {
                    if (p != null) {
                        if (gameBoard.side(p) == playerSide && p instanceof King) {
                            ++oppoKingsCaptured;
                        }
                        else if (gameBoard.side(p) == playerSide) {
                            ++oppoPawnsCaptured;
                        }
                        else if (p instanceof King) {
                            ++userKingsCaptured;
                        }
                        else {
                            ++userPawnsCaptured;
                        }
                    }
                }
            }
            int playersTurn = game.getHasControl() == playerSide ? 1 : 0;
         	int playerColor = playerSide;
         	GameResult gr = game.checkGameState();
         	int isOver = gr.getState() != GameState.IN_PROGRESS ? 1 : 0;
         	int winner = gr.getWinner() != null && gr.getWinner().getUuid().equals(uuid) ? 1 : 0;
         	String opponentUuid = players.get((playerSide + 1) % 2).getUuid();
         	response.type("application/json");
         	String body = (new Gson()).toJson(
                    new GameStatusObject(currentBoard, playersTurn, playerColor, isOver, winner,
                            userPawnsCaptured, userKingsCaptured, oppoPawnsCaptured,
                            oppoKingsCaptured, opponentUuid)
            );
         	response.body(body);
         	return body;
        });

        post("/validMoves", (request, response) -> {
            String uuid = request.cookie(playerIdKey);
            Game game = GameManager.getGame(uuid);
            if (game == null) {
                return "";
            }
            ValidMovesRequest vmr = (new Gson()).fromJson(request.body(), ValidMovesRequest.class);
            ArrayList<ArrayList<Boolean>> legalMoves = game.getBoard().getLegalMovesPiece(vmr.getX(), vmr.getY());
            ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();
            for (ArrayList<Boolean> row : legalMoves) {
                ArrayList<Integer> moveRow = new ArrayList<>();
                for (Boolean isLegal : row) {
                    if (isLegal) {
                        moveRow.add(1);
                    } else {
                        moveRow.add(0);
                    }
                }
                validMoves.add(moveRow);
            }
            return (new Gson()).toJson(new ValidMovesObject(validMoves));
        });

        post("/sendMove", (request, response) -> {
            String uuid = request.cookie(playerIdKey);
            String body = request.body();
            Move move = (new Gson()).fromJson(body, Move.class);
            Game game = GameManager.getGame(uuid);
            if (game == null) {
                return "";
            }
            int playerSide = -1;
            ArrayList<Player> players = game.getPlayers();
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).getUuid().equals(uuid)) {
                    playerSide = i;
                }
            }
            if (playerSide != game.getHasControl()) {
                return (new Gson()).toJson(new SendMoveObject(0));
            }
            MoveResult mr = game.move(move.getX1(), move.getY1(), move.getX2(), move.getY2());
            if (mr.getState() == MoveState.INVALID) {
                return (new Gson()).toJson(new SendMoveObject(0));
            }
            else {
                return (new Gson()).toJson(new SendMoveObject(1));
            }
        });

        post("/invite", (request, response) -> {
            String uuid = request.cookie(playerIdKey);
            String body = request.body();
            System.out.println(body);
            InviteRematchRequest req = (new Gson()).fromJson(body, InviteRematchRequest.class);
            InviteResult ir = GameManager.invite(uuid, req.getUuid());
            if (ir.getState() == InviteState.ACCEPTED) {
                return (new Gson()).toJson(new GameRequestResult(1));
            } else {
                return (new Gson()).toJson(new GameRequestResult(0));
            }
        });

        post("/rematch", (request, response) -> {
            String uuid = request.cookie(playerIdKey);
            String body = request.body();
            InviteRematchRequest req = (new Gson()).fromJson(body, InviteRematchRequest.class);
            RematchResult rr = GameManager.rematch(uuid, req.getUuid());
            if (rr.getState() == RematchState.ACCEPTED) {
                return (new Gson()).toJson(new GameRequestResult(1));
            } else {
                return (new Gson()).toJson(new GameRequestResult(0));
            }
        });

        get("/random", ((request, response) -> {
            String uuid = request.cookie(playerIdKey);
            RandomResult rr = GameManager.random(uuid);
            if (rr.getState() == RandomState.ACCEPTED) {
                return (new Gson()).toJson(new GameRequestResult(1));
            } else {
                return (new Gson()).toJson(new GameRequestResult(0));
            }
        }));

        get("/endGame", (request, response) -> {
            String uuid = request.cookie(playerIdKey);
            GameManager.endGame(uuid);
            return "";
        });
    }
}
