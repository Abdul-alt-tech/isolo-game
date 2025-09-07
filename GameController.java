package com.isolo;

import com.isolo.util.FileHandler;
import com.isolo.util.SoundManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML private GridPane gameBoard;
    @FXML private Label player1Label;
    @FXML private Label player2Label;
    @FXML private Label currentPlayerLabel;
    @FXML private Label player1Score;
    @FXML private Label player2Score;
    @FXML private VBox mainMenu;
    @FXML private VBox gameScreen;
    @FXML private VBox playerSetup;
    @FXML private VBox loadGameScreen;
    @FXML private VBox instructionsScreen;
    @FXML private TextField player1Name;
    @FXML private TextField player2Name;
    @FXML private ListView<String> savedGamesList;
    @FXML private Label noSavedGamesLabel;

    private GameLogic gameLogic;
    private Player player1;
    private Player player2;
    private Circle[][] stoneCircles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBoard();
        loadSavedGames();
    }

    private void initializeBoard() {
        gameBoard.getChildren().clear();
        stoneCircles = new Circle[4][8];
        
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 8; col++) {
                HBox pit = new HBox();
                pit.getStyleClass().add("pit");
                pit.setPrefSize(60, 60);
                
                HBox stonesContainer = new HBox();
                stonesContainer.setSpacing(2);
                
                for (int i = 0; i < 4; i++) {
                    Circle stone = new Circle(8);
                    stone.setFill((row < 2) ? Color.DARKBLUE : Color.DARKRED);
                    stonesContainer.getChildren().add(stone);
                }
                
                pit.getChildren().add(stonesContainer);
                
                final int r = row;
                final int c = col;
                pit.setOnMouseClicked(event -> handleMove(r, c));
                
                gameBoard.add(pit, col, row);
                stoneCircles[row][col] = (Circle) stonesContainer.getChildren().get(0);
            }
        }
    }

    private void updateBoard() {
        int[][] board = gameLogic.getBoard();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 8; col++) {
                HBox pit = (HBox) gameBoard.getChildren().get(row * 8 + col);
                HBox stonesContainer = (HBox) pit.getChildren().get(0);
                stonesContainer.getChildren().clear();
                for (int i = 0; i < board[row][col]; i++) {
                    Circle stone = new Circle(8);
                    stone.setFill((row < 2) ? Color.DARKBLUE : Color.DARKRED);
                    stonesContainer.getChildren().add(stone);
                }
                stoneCircles[row][col] = (board[row][col] > 0) ?
                        (Circle) stonesContainer.getChildren().get(0) : null;
            }
        }
        player1Score.setText("Score: " + gameLogic.getScores()[0]);
        player2Score.setText("Score: " + gameLogic.getScores()[1]);
        String currentPlayerText = "Current Player: " +
                ((gameLogic.getCurrentPlayer() == 1) ? player1.getName() : player2.getName());
        currentPlayerLabel.setText(currentPlayerText);
        if (gameLogic.getCurrentPlayer() == 1) {
            player1Label.setStyle("-fx-background-color: #aaffaa; -fx-padding: 5px;");
            player2Label.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
        } else {
            player1Label.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
            player2Label.setStyle("-fx-background-color: #aaffaa; -fx-padding: 5px;");
        }
        if (gameLogic.isGameOver()) {
            showGameOverDialog();
        }
        // Play capture sound if capture occurred
        if (gameLogic.wasCaptureMade()) {
            SoundManager.playCaptureSound();
        }
    }

    private void handleMove(int row, int col) {
        // Only allow moves in player's territory and with stones
        if (gameLogic.makeMove(row, col)) {
            SoundManager.playMoveSound();
            updateBoard();
        } else {
            SoundManager.playErrorSound();
        }
    }

    @FXML
    private void startNewGame() {
        if (player1Name.getText().isEmpty() || player2Name.getText().isEmpty()) {
            showAlert("Error", "Please enter names for both players.");
            return;
        }
        
        player1 = new Player(player1Name.getText());
        player2 = new Player(player2Name.getText());
        
        gameLogic = new GameLogic();
        
        player1Label.setText(player1.getName());
        player2Label.setText(player2.getName());
        
        updateBoard();
        
        mainMenu.setVisible(false);
        playerSetup.setVisible(false);
        gameScreen.setVisible(true);
    }

    @FXML
    private void showPlayerSetup() {
        mainMenu.setVisible(false);
        playerSetup.setVisible(true);
    }

    @FXML
    private void showLoadGame() {
        loadSavedGames();
        mainMenu.setVisible(false);
        loadGameScreen.setVisible(true);
    }

    @FXML
    private void showInstructionsDialog() {
        // Create a dialog for instructions
        Alert instructionsAlert = new Alert(Alert.AlertType.INFORMATION);
        instructionsAlert.setTitle("How to Play Isolo");
        instructionsAlert.setHeaderText("Game Instructions");
        
        // Create a text area for scrollable content
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(600, 400);
        textArea.setFont(Font.font("Arial", 14));
        
        // Build the instructions text
        StringBuilder instructions = new StringBuilder();
        instructions.append("GAME OVERVIEW\n");
        instructions.append("Isolo is a traditional Zambian board game played on a 4x8 grid. ");
        instructions.append("Two players take turns moving stones with the goal of capturing the opponent's stones.\n\n");
        
        instructions.append("SETUP\n");
        instructions.append("• The board has 4 rows and 8 columns\n");
        instructions.append("• Each pit starts with 4 stones\n");
        instructions.append("• Player 1 controls the top two rows\n");
        instructions.append("• Player 2 controls the bottom two rows\n\n");
        
        instructions.append("GAMEPLAY\n");
        instructions.append("1. On your turn, click on any pit on your side that contains stones\n");
        instructions.append("2. All stones from that pit will be distributed one by one\n");
        instructions.append("3. Player 1 moves counter-clockwise (leftwards)\n");
        instructions.append("4. Player 2 moves clockwise (rightwards)\n");
        instructions.append("5. If your last stone lands in an empty pit on your side, you capture all stones in the opposite pit\n\n");
        
        instructions.append("CAPTURING\n");
        instructions.append("• When your last stone lands in an empty pit on your side\n");
        instructions.append("• You capture all stones from the opposite pit on the opponent's side\n");
        instructions.append("• The captured stones are added to your score\n");
        instructions.append("• Both the landing pit and the captured pit are emptied\n\n");
        
        instructions.append("WINNING THE GAME\n");
        instructions.append("• The game ends when one player has no stones left on their side\n");
        instructions.append("• The player with more stones in their score wins\n");
        instructions.append("• If both players have the same number of stones, it's a draw\n\n");
        
        instructions.append("TIPS\n");
        instructions.append("• Plan moves that set up captures\n");
        instructions.append("• Try to leave your opponent with limited options\n");
        instructions.append("• Balance between capturing stones and maintaining positions\n");
        instructions.append("• Watch for opportunities to create multiple captures");
        
        textArea.setText(instructions.toString());
        
        instructionsAlert.getDialogPane().setContent(textArea);
        instructionsAlert.getDialogPane().setPrefSize(650, 500);
        
        // Show the alert
        instructionsAlert.showAndWait();
    }

    @FXML
    private void loadSelectedGame() {
        String selected = savedGamesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            GameState loadedState = FileHandler.loadGame(selected);
            if (loadedState != null) {
                gameLogic = new GameLogic();
                gameLogic.setBoard(loadedState.getBoard());
                gameLogic.setCurrentPlayer(loadedState.getCurrentPlayer());
                gameLogic.setScores(loadedState.getScores());
                gameLogic.setGameOver(loadedState.isGameOver());
                gameLogic.setWinner(loadedState.getWinner());
                
                player1 = new Player(loadedState.getPlayer1Name());
                player2 = new Player(loadedState.getPlayer2Name());
                
                player1Label.setText(player1.getName());
                player2Label.setText(player2.getName());
                
                updateBoard();
                
                mainMenu.setVisible(false);
                gameScreen.setVisible(true);
            }
        }
    }

    @FXML
    private void saveGame() {
        if (gameLogic != null) {
            GameState gameState = new GameState(gameLogic, player1.getName(), player2.getName());
            String filename = FileHandler.generateSaveName();
            FileHandler.saveGame(gameState, filename);
            showAlert("Game Saved", "Game has been saved successfully as " + filename);
        }
    }

    @FXML
    private void exitGame() {
        Platform.exit();
    }

    @FXML
    private void backToMainMenu() {
        playerSetup.setVisible(false);
        gameScreen.setVisible(false);
        loadGameScreen.setVisible(false);
        instructionsScreen.setVisible(false);
        mainMenu.setVisible(true);
    }

    private void loadSavedGames() {
        List<String> savedGames = FileHandler.getSavedGames();
        savedGamesList.getItems().setAll(savedGames);
        if (noSavedGamesLabel != null) {
            noSavedGamesLabel.setVisible(savedGames.isEmpty());
        }
    }

    private void showGameOverDialog() {
        int winner = gameLogic.getWinner();
        String message;
        
        if (winner == 0) {
            message = "The game ended in a draw!";
            if (player1 != null) player1.addDraw();
            if (player2 != null) player2.addDraw();
        } else if (winner == 1) {
            message = player1.getName() + " wins!";
            if (player1 != null) player1.addWin();
            if (player2 != null) player2.addLoss();
        } else {
            message = player2.getName() + " wins!";
            if (player1 != null) player1.addLoss();
            if (player2 != null) player2.addWin();
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Finished");
        alert.setContentText(message);
        
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}