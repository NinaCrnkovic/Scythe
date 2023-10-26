package hr.algebra.scythe.controller;

import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.util.BoardService;
import hr.algebra.scythe.util.PlayerService;
import hr.algebra.scythe.util.GameLogic;
import hr.algebra.scythe.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class ScytheController {

    @FXML
    public BorderPane borderPane;

    @FXML
    private GridPane gameBoard;

    private Soldier selectedSoldier;
    private Soldier lastSoldierMoved = null;

    private final PlayerService playerService = new PlayerService();
    private BoardService boardService;
    private Player currentPlayer;
    private int movesMade;

    private Set<Soldier> soldiersMoved = new HashSet<>();



    public void initialize() {
        playerService.initializePlayers();

        boardService = new BoardService(gameBoard, this::handleTileClick,
                playerService.getPlayerRed(), playerService.getPlayerBlue());
        boardService.setupBoard();
        borderPane.setFocusTraversable(true);
        borderPane.requestFocus();
        currentPlayer = playerService.getPlayerRed();  // Crveni igrač započinje igru
        movesMade = 0;

    }

    private void handleTileClick(int x, int y) {
        if (selectedSoldier == null) {
            selectedSoldier = playerService.getSelectedSoldier(x, y, lastSoldierMoved);
            if (selectedSoldier != null && selectedSoldier.getColor() != currentPlayer.getColor()) {
                // The player is trying to move an opponent's soldier.
                selectedSoldier = null;
                return;
            }
        } else {
            if (GameLogic.isAdjacent(selectedSoldier, x, y)) {
                if (!playerService.isTileOccupiedBySameColor(currentPlayer, x, y)) {
                    if (playerService.isTileOccupiedByOpponent(currentPlayer, x, y)) {
                        System.out.println("Battle Initiated!");  // Placeholder for battle logic
                        boardService.setBattleImage(x, y);
                        openDiceRollWindow();
                    }
                    moveSelectedSoldierTo(x, y);
                    lastSoldierMoved = selectedSoldier;
                    soldiersMoved.add(selectedSoldier);

                    if (soldiersMoved.size() == 3) {
                        switchPlayer();
                    }
                }
                selectedSoldier = null;
            }
        }
        boardService.updateBoard();
    }



    private void moveSelectedSoldierTo(int x, int y) {
        if (GameLogic.isValidMove(x, y, Constants.BOARD_SIZE)) {
            selectedSoldier.setX(x);
            selectedSoldier.setY(y);
        }
    }

    private void switchPlayer() {
        if (currentPlayer == playerService.getPlayerRed()) {
            currentPlayer = playerService.getPlayerBlue();
        } else {
            currentPlayer = playerService.getPlayerRed();
        }
        soldiersMoved.clear();  // Clear the set when switching players
    }

    public void openDiceRollWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/algebra/scythe/view/diceRoll.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Roll Dice");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}





