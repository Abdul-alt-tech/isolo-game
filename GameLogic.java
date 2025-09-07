package com.isolo;

public class GameLogic {
    private int[][] board;
    private int currentPlayer;
    private int[] scores;
    private boolean gameOver;
    private int winner;
    private boolean lastCaptureMade;

    public GameLogic() {
        initializeGame();
    }

    public void initializeGame() {
        board = new int[4][8];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 4;
            }
        }
        currentPlayer = 1; // Player 1 starts
        scores = new int[2];
        gameOver = false;
        winner = 0;
    }

    public boolean makeMove(int row, int col) {
        lastCaptureMade = false;
        if (gameOver || !isValidMove(row, col)) {
            return false;
        }

        int stones = board[row][col];
        board[row][col] = 0;
        int currentRow = row;
        int currentCol = col;

        while (stones > 0) {
            if (currentPlayer == 1) {
                currentCol--;
                if (currentCol < 0) {
                    currentCol = 7;
                    currentRow = (currentRow == 0) ? 1 : 0;
                }
            } else {
                currentCol++;
                if (currentCol > 7) {
                    currentCol = 0;
                    currentRow = (currentRow == 3) ? 2 : 3;
                }
            }

            // Drop stone only in player's territory
            if ((currentPlayer == 1 && currentRow < 2) ||
                (currentPlayer == 2 && currentRow >= 2)) {
                board[currentRow][currentCol]++;
                stones--;
            }
        }

        checkCapture(currentRow, currentCol);

        if (checkGameOverCondition()) {
            determineWinner();
            gameOver = true;
        } else {
            switchPlayer();
        }

        return true;
    }

    private boolean isValidMove(int row, int col) {
        if ((currentPlayer == 1 && row < 2) ||
            (currentPlayer == 2 && row >= 2)) {
            return board[row][col] > 0;
        }
        return false;
    }

    private void checkCapture(int row, int col) {
        if (((currentPlayer == 1 && row < 2) ||
             (currentPlayer == 2 && row >= 2)) &&
            board[row][col] == 1) {

            int oppositeRow = (row < 2) ? row + 2 : row - 2;
            int oppositeCol = col;

            if (board[oppositeRow][oppositeCol] > 0) {
                scores[currentPlayer - 1] += board[oppositeRow][oppositeCol] + 1;
                board[oppositeRow][oppositeCol] = 0;
                board[row][col] = 0;
                lastCaptureMade = true;
            }
        }
    }

    private boolean checkGameOverCondition() {
        boolean player1Empty = true;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] > 0) {
                    player1Empty = false;
                    break;
                }
            }
        }

        boolean player2Empty = true;
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] > 0) {
                    player2Empty = false;
                    break;
                }
            }
        }

        return player1Empty || player2Empty;
    }

    private void determineWinner() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                scores[0] += board[i][j];
                board[i][j] = 0;
            }
        }

        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                scores[1] += board[i][j];
                board[i][j] = 0;
            }
        }

        if (scores[0] > scores[1]) {
            winner = 1;
        } else if (scores[1] > scores[0]) {
            winner = 2;
        } else {
            winner = 0; // Draw
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    // Getters and setters
    public int[][] getBoard() { return board; }
    public int getCurrentPlayer() { return currentPlayer; }
    public int[] getScores() { return scores; }
    public boolean isGameOver() { return gameOver; }
    public int getWinner() { return winner; }
    public boolean wasCaptureMade() { return lastCaptureMade; }

    public void setBoard(int[][] board) { this.board = board; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }
    public void setScores(int[] scores) { this.scores = scores; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void setWinner(int winner) { this.winner = winner; }
}
