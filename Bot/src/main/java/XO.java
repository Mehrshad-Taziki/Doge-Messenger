import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class XO {
    private static HashMap<Integer, Integer> inWaitGames; // gameID , Creator
    private static HashMap<Integer, XOGame> games; //gameID
    private static HashMap<Integer, Integer> playersGame; // playerID, GameID
    private static ArrayList<Integer> receivers;

    public static String getFields() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String waitString = objectMapper.writeValueAsString(inWaitGames);
            String gamesString = objectMapper.writeValueAsString(games);
            String playersString = objectMapper.writeValueAsString(playersGame);
            String receiversString = objectMapper.writeValueAsString(receivers);
            String[] fields = new String[]{waitString, gamesString, playersString, receiversString};
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setFields(String data) {
        if (data == null) {
            games = new HashMap<>();
            inWaitGames = new HashMap<>();
            playersGame = new HashMap<>();
            receivers = new ArrayList<>();
            return;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] fields = objectMapper.readValue(data, String[].class);
            inWaitGames = objectMapper.readValue(fields[0], new TypeReference<HashMap<Integer, Integer>>() {
            });
            games = objectMapper.readValue(fields[1], new TypeReference<HashMap<Integer, XOGame>>() {
            });
            playersGame = objectMapper.readValue(fields[2], new TypeReference<HashMap<Integer, Integer>>() {
            });
            receivers = objectMapper.readValue(fields[3], new TypeReference<ArrayList<Integer>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            playersGame = new HashMap<>();
            games = new HashMap<>();
            inWaitGames = new HashMap<>();
            receivers = new ArrayList<>();
        }
    }

    public static boolean invokeAnswerMessage() {
        return true;
    }

    public static boolean invokeAnswerGroupMessage() {
        return false;
    }

    public static boolean invokeSendMessage() {
        return true;
    }

    public static boolean invokeSendGroupMessage() {
        return false;
    }

    public static boolean invokeSendComment() {
        return false;
    }

    public static boolean invokeSendTweet() {
        return false;
    }

    public static boolean acceptInvite() {
        return false;
    }

    public static String answerMessage(String text, int senderID) {
        if (text.equals("/help")) return "/create To Create A New Game" +
                "\n/join (Game ID) To Join Your Friends Game";
        if (text.equals("/create")) {
            if (inWaitGames.containsValue(senderID)) return "You Have Already Created A Game";
            if (playersGame.containsKey(senderID)) return "You Are Already Playing A Game";
            Random random = new Random();
            int newGameCode;
            do {
                newGameCode = random.nextInt();
            } while (inWaitGames.containsKey(newGameCode) || games.containsKey(newGameCode) || newGameCode == 0);
            inWaitGames.put(newGameCode, senderID);
            return "Send Your Friends This Code To Join With /join " + newGameCode;
        }
        if (text.startsWith("/join ")) {
            if (playersGame.containsKey(senderID)) return "You Are Already Playing A Game";
            if (inWaitGames.containsValue(senderID)) return "You Have Already Created A Game";
            try {
                int inviteCode = Integer.parseInt(text.substring(6));
                if (inWaitGames.containsKey(inviteCode)) {
                    int receiverID = inWaitGames.get(inviteCode);
                    inWaitGames.remove(inviteCode);
                    playersGame.put(senderID, inviteCode);
                    playersGame.put(receiverID, inviteCode);
                    games.put(inviteCode, new XOGame(receiverID, senderID));
                    receivers.add(receiverID);
                    receivers.add(senderID);
                    return "Successfully Joined The Game";
                }
                return "Invite Code Doesn't Exist";
            } catch (Throwable throwable) {
                return "InValid Code";
            }
        }
        if (text.startsWith("/hit ")) {
            if (!playersGame.containsKey(senderID)) return "You Are Not In A Game";
            if (games.get(playersGame.get(senderID)).getWinner() != 0) return "Game Is Finished";
            try {
                int i = Integer.parseInt(text.substring(5, 6)) - 1;
                int j = Integer.parseInt(text.substring(7, 8)) - 1;
                if (i >= 0 && i <= 2 && j >= 0 && j <= 2) {
                    if (games.get(playersGame.get(senderID)).TurnID() == senderID) {
                        boolean hit = games.get(playersGame.get(senderID)).hit(i, j);
                        if (!hit) return "Invalid Target";
                        receivers.add(games.get(playersGame.get(senderID)).getPlayerOneID());
                        receivers.add(games.get(playersGame.get(senderID)).getPlayerTwoID());
                        return "Target Hit";
                    }
                    return "It's Not Your Turn";
                }
            } catch (Throwable throwable) {
                return "Invalid Numbers";
            }
        }
        if (text.equals("/Leave")) {
            if (inWaitGames.containsValue(senderID)) {
                int gameID = getWaitGameID(senderID);
                inWaitGames.remove(gameID);
                return "Game Canceled";
            }
            if (playersGame.containsKey(senderID)) {
                int gameID = playersGame.get(senderID);
                XOGame game = games.get(gameID);
                game.lose(game.getPlayerType(senderID));
                receivers.add(games.get(playersGame.get(senderID)).getPlayerOneID());
                receivers.add(games.get(playersGame.get(senderID)).getPlayerTwoID());
                return "Left The Game";
            }
        }
        return "Invalid Command";
    }

    private static int getWaitGameID(int playerID) {
        AtomicInteger ID = new AtomicInteger(0);
        inWaitGames.forEach((gameID, player) -> {
            if (player == playerID) ID.set(gameID);
        });
        return ID.get();
    }

    public static String sendMessage(int receiverID) {
        if (playersGame.containsKey(receiverID)) {
            if (receivers.contains(receiverID)) {
                receivers.remove(Integer.valueOf(receiverID));
                int gameID = playersGame.get(receiverID);
                XOGame game = games.get(gameID);
                if (game.getWinner() != 0) {
                    playersGame.remove(receiverID);
                    if (!playersGame.containsValue(gameID)) games.remove(gameID);
                }
                return game.getGame(game.getPlayerType(receiverID));
            }
        }
        return "";
    }

    public static String answerGPMessage(String text, int senderID, int groupID) {
        return null;
    }

    public static String sendGPMessage(int groupID) {
        return null;
    }

    public static String sendTweet(String text) {
        return null;
    }

    public static String sendComment(String text, int senderID, int postID) {
        return null;
    }


    static class XOGame {
        private int[][] cells;
        private int winner; //0 none 1 , 2 , 3 draw
        private int turn;
        private int playerOneID; //x
        private int playerTwoID; //o

        public XOGame() {
        }

        public XOGame(int playerOneID, int playerTwoID) {
            this.playerOneID = playerOneID;
            this.playerTwoID = playerTwoID;
            turn = 1;
            winner = 0;
            cells = new int[3][3];
            cells[0][0] = 0;
            cells[0][1] = 0;
            cells[0][2] = 0;
            cells[1][0] = 0;
            cells[1][1] = 0;
            cells[1][2] = 0;
            cells[2][0] = 0;
            cells[2][1] = 0;
            cells[2][2] = 0;
        }

        private int getPlayerType(int id) {
            if (playerOneID == id) return 1;
            return 2;
        }

        private int TurnID() {
            if (turn == 1) return playerOneID;
            return playerTwoID;
        }

        private void nextTurn() {
            if (turn == 1) turn = 2;
            else turn = 1;
        }

        private String getGame(int player) {
            StringBuilder gameState;
            if (player == 1) {
                switch (winner) {
                    case 1 -> gameState = new StringBuilder("You Have Won The Game!");
                    case 2 -> gameState = new StringBuilder("You Have Lost The Game!");
                    case 3 -> gameState = new StringBuilder("Game Ended In a draw!");
                    default -> {
                        if (turn == 1) gameState = new StringBuilder("your turn");
                        else gameState = new StringBuilder("opponent's turn");
                    }
                }
            } else {
                switch (winner) {
                    case 2 -> gameState = new StringBuilder("You Have Won The Game!");
                    case 1 -> gameState = new StringBuilder("You Have Lost The Game!");
                    case 3 -> gameState = new StringBuilder("Game Ended In a draw!");
                    default -> {
                        if (turn == 2) gameState = new StringBuilder("your turn");
                        else gameState = new StringBuilder("opponent's turn");
                    }
                }
            }
            gameState.append("\n you are ").append(player).append("\n");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    gameState.append(cells[i][j]);
                }
                if (i < 2) gameState.append("\n");
            }
            return gameState.toString();
        }

        private boolean hit(int i, int j) {
            if (cells[i][j] == 0) {
                cells[i][j] = turn;
                int player = turn;
                if (cells[0][0] == cells[0][1] && cells[0][0] == cells[0][2] && cells[0][0] == player) winner = turn;
                else if (cells[1][0] == cells[1][1] && cells[1][0] == cells[1][2] && cells[1][0] == player)
                    winner = turn;
                else if (cells[2][0] == cells[2][1] && cells[2][0] == cells[2][2] && cells[2][0] == player)
                    winner = turn;
                else if (cells[0][0] == cells[1][0] && cells[0][0] == cells[2][0] && cells[0][0] == player)
                    winner = turn;
                else if (cells[0][1] == cells[1][1] && cells[0][1] == cells[2][1] && cells[0][1] == player)
                    winner = turn;
                else if (cells[0][2] == cells[1][2] && cells[0][2] == cells[2][2] && cells[0][2] == player)
                    winner = turn;
                else if (cells[0][0] == cells[1][1] && cells[0][0] == cells[2][2] && cells[0][0] == player)
                    winner = turn;
                else if (cells[0][2] == cells[1][1] && cells[0][2] == cells[2][0] && cells[0][2] == player)
                    winner = turn;
                else {
                    boolean draw = true;
                    Loop:
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (cells[k][l] == 0) {
                                draw = false;
                                break Loop;
                            }
                        }
                    }
                    if (draw) winner = 3;
                }
                nextTurn();
                return true;
            }
            return false;
        }

        public int[][] getCells() {
            return cells;
        }

        public void setCells(int[][] cells) {
            this.cells = cells;
        }

        public int getWinner() {
            return winner;
        }

        public void setWinner(int winner) {
            this.winner = winner;
        }

        public int getTurn() {
            return turn;
        }

        public void setTurn(int turn) {
            this.turn = turn;
        }

        public int getPlayerOneID() {
            return playerOneID;
        }

        public void setPlayerOneID(int playerOneID) {
            this.playerOneID = playerOneID;
        }

        public int getPlayerTwoID() {
            return playerTwoID;
        }

        public void setPlayerTwoID(int playerTwoID) {
            this.playerTwoID = playerTwoID;
        }

        public void lose(int loser) {
            if (loser == 1) winner = 2;
            if (loser == 2) winner = 1;
        }
    }
}
