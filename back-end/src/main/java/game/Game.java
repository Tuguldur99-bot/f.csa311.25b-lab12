package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

enum Player {
    PLAYER0(0), PLAYER1(1);

    final int value;

    Player(int value) {
        this.value = value;
    }
}

public class Game {
    private final Board board;
    private final Player player;
    private final List<Game> history;
    public final String winner; 
   
    public Game() {
        this(new Board(), Player.PLAYER0, List.of(), null);
    }
    
    public Game(Board board, Player nextPlayer) {
        this(board, nextPlayer, List.of(), null);
    }
    
    public Game(Board board, Player nextPlayer, List<Game> history) {
        this(board, nextPlayer, history, null);
    }
    
    public Game(Board board, Player nextPlayer, List<Game> history, String winner) {
        this.board = board;
        this.player = nextPlayer;
        this.history = history;
        this.winner = winner;
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Game play(int x, int y) {
        if (this.board.getCell(x, y) != null || this.winner != null) {
            return this;
        }
    
        List<Game> newHistory = new ArrayList<>(this.history);
        newHistory.add(this);
    
        Player nextPlayer = this.player == Player.PLAYER0 ? Player.PLAYER1 : Player.PLAYER0;
        Board newBoard = this.board.updateCell(x, y, this.player);
        
        Player winPlayer = getWinner(newBoard); // шинэ төлөв дээр шалгана
        String newWinner = winPlayer != null ? winPlayer.toString() : null;
    
        return new Game(newBoard, nextPlayer, newHistory, newWinner);
    }
    

    public Game undoMove() {
        if (this.history.isEmpty()) {
            return this; 
        }
        List<Game> newHistory = new ArrayList<>(this.history);
        Game previousGame = newHistory.remove(newHistory.size() - 1);
        return new Game(previousGame.board, previousGame.player, newHistory);
    }
 
    private Player getWinner(Board board) {
        for (int row = 0; row < 3; row++)
            if (board.getCell(row, 0) != null && board.getCell(row, 0) == board.getCell(row, 1)
                    && board.getCell(row, 1) == board.getCell(row, 2))
                return board.getCell(row, 0);
        for (int col = 0; col < 3; col++)
            if (board.getCell(0, col) != null && board.getCell(0, col) == board.getCell(1, col)
                    && board.getCell(0, col) == board.getCell(2, col))
                return board.getCell(0, col);
        if (board.getCell(1, 1) != null && board.getCell(0, 0) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 2))
            return board.getCell(1, 1);
        if (board.getCell(1, 1) != null && board.getCell(0, 2) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 0))
            return board.getCell(1, 1);
        return null;
    }
    

    public Collection<String> getHistory() {
        StringBuilder sb = new StringBuilder();
        for (Game game : this.history) {
            sb.append(game.toString());
        }
        return List.of(sb.toString());
    }
}