package com.isolo;

import java.time.LocalDateTime;

public class GameState {
    private int[][] board;
    private int currentPlayer;
    private int[] scores;
    private boolean gameOver;
    private int winner;
    private LocalDateTime saveTime;
    private String player1Name;
    private String player2Name;

    public GameState() {
        // Default constructor for serialization
    }

    public GameState(GameLogic gameLogic, String player1Name, String player2Name) {
    this.board = gameLogic.getBoard();
    this.currentPlayer = gameLogic.getCurrentPlayer();
    this.scores = gameLogic.getScores();
    this.gameOver = gameLogic.isGameOver(); // This should work now
    this.winner = gameLogic.getWinner();
    this.saveTime = LocalDateTime.now();
    this.player1Name = player1Name;
    this.player2Name = player2Name;
}

    // Getters and setters
    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) { this.board = board; }
    
    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }
    
    public int[] getScores() { return scores; }
    public void setScores(int[] scores) { this.scores = scores; }
    
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    
    public int getWinner() { return winner; }
    public void setWinner(int winner) { this.winner = winner; }
    
    public LocalDateTime getSaveTime() { return saveTime; }
    public void setSaveTime(LocalDateTime saveTime) { this.saveTime = saveTime; }
    
    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }
    
    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }
}
