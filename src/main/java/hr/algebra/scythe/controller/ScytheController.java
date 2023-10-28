package hr.algebra.scythe.controller;
import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Tile;
import hr.algebra.scythe.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScytheController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gameBoard;

    @FXML
    private GridPane allPlayersGrid;

    private Soldier selectedSoldier;
    private Soldier lastSoldierMoved = null;

    private final PlayerService playerService = new PlayerService();
    private final WindowUtil windowUtil = new WindowUtil();
    private final ResourceService resourceService = new ResourceService();
    private BoardService boardService;
    private BattleService battleService;
    private Player currentPlayer;
    private int movesMade;

    private int totalTurns = 0;
    private static final int MAX_TURNS = 1;
    private boolean gameEnded = false;

    private Set<Soldier> soldiersMoved = new HashSet<>();
    Set<Soldier> soldiersAttacked = new HashSet<>();

    public void initialize() {
        playerService.initializePlayers();
        boardService = new BoardService(gameBoard, this::handleTileClick,
                playerService.getPlayerRed(), playerService.getPlayerBlue());
        boardService.setupBoard();
        battleService = new BattleService(playerService, windowUtil, resourceService);

        borderPane.setFocusTraversable(true);
        borderPane.requestFocus();

        currentPlayer = playerService.getPlayerRed();
        movesMade = 0;
        updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));
    }
    private void handleBattle(int x, int y) {
        Soldier defender = playerService.getSelectedSoldier(x, y, null);
        int attackerOldX = selectedSoldier.getX();
        int attackerOldY = selectedSoldier.getY();

        boolean attackMade = battleService.initiateAttack(selectedSoldier, currentPlayer, x, y, attackerOldX, attackerOldY);
        if (attackMade) {
            soldiersMoved.add(selectedSoldier); // Dodajemo vojnika koji je napao u skup
        }
        playerService.returnSoldierToOriginalPosition(selectedSoldier, attackerOldX, attackerOldY);
        selectedSoldier = null;

        if (defender != null) {
            playerService.returnSoldierToOriginalPosition(defender, x, y);
        }
    }

    private void handleTileClick(int x, int y) {
        if (gameEnded) {
            return;
        }
        if (selectedSoldier == null) {
            selectedSoldier = playerService.getSelectedSoldier(x, y, lastSoldierMoved);
            if (selectedSoldier != null && (selectedSoldier.getColor() != currentPlayer.getColor() || soldiersMoved.contains(selectedSoldier))) {
                selectedSoldier = null;
            }

    } else {
            if (GameLogic.isAdjacent(selectedSoldier, x, y)) {
                if (!playerService.isTileOccupiedBySameColor(currentPlayer, x, y)) {
                    if (playerService.isTileOccupiedByOpponent(currentPlayer, x, y)) {
                        handleBattle(x, y);
                        //soldiersMoved.add(selectedSoldier); // Dodajemo napadača u set nakon napada
                    } else {
                        moveSelectedSoldierTo(x, y);
                        soldiersMoved.add(selectedSoldier); // Dodajemo vojnika u set nakon poteza
                    }
                    selectedSoldier = null;
                    if (soldiersMoved.size() == 3) {
                        switchPlayer();
                        soldiersMoved.clear(); // Očistite set da bi se mogli ponovno početi brojati potezi za sljedećeg igrača
                    }
                }
            }
        }
        boardService.updateBoard();
    }

    private void moveSelectedSoldierTo(int x, int y) {
        if (GameLogic.isValidMove(x, y, Constants.BOARD_SIZE)) {
            selectedSoldier.setX(x);
            selectedSoldier.setY(y);
            Tile tileOnPosition = BoardService.getTile(x, y);
            resourceService.gatherResourcesFromTile(selectedSoldier, tileOnPosition);
            updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));
        }
    }

    private void switchPlayer() {
        totalTurns++;
        if(totalTurns == MAX_TURNS) {
            endGame();
            return;
        }
        currentPlayer = (currentPlayer == playerService.getPlayerRed()) ? playerService.getPlayerBlue() : playerService.getPlayerRed();
        soldiersMoved.clear();
        updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));
    }

    private void endGame() {
        int redResources = playerService.getPlayerRed().totalResources();
        int blueResources = playerService.getPlayerBlue().totalResources();

        Player winner;
        if (redResources > blueResources) {
            winner = playerService.getPlayerRed();
        } else if (blueResources > redResources) {
            winner = playerService.getPlayerBlue();
        } else {
            winner = null;
        }

        displayEndGameMessage(winner, redResources, blueResources);
        gameEnded = true;
    }

    private void displayEndGameMessage(Player winner, int redResources, int blueResources) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");

        StringBuilder message = new StringBuilder();

        if(winner == null) {
            message.append("It's a tie!\n");
        } else {
            message.append(winner.getColor()).append(" Player wins!\n");
        }

        message.append("Red Player had ").append(redResources).append(" resources.\n");
        message.append("Blue Player had ").append(blueResources).append(" resources.");

        alert.setContentText(message.toString());
        alert.showAndWait();

    }

    private void updateAllPlayerInfo(List<Player> players) {
        allPlayersGrid.getChildren().clear();

        int rowIndex = 0;
        for (Player player : players) {
            Label playerLabel = new Label(player.getColor().toString() + " Player");
            playerLabel.setPadding(new Insets(10, 10, 10, 10));
            playerLabel.getStyleClass().add(player.getColor().toString().toLowerCase() + "-player");

            allPlayersGrid.add(playerLabel, 0, rowIndex++);

            for (int i = 0; i < 3; i++) {
                Soldier soldier = player.getSoldier(i);

                Label soldierLabel = new Label("Soldier " + (i + 1));
                soldierLabel.setPadding(new Insets(10, 10, 10, 10));
                soldierLabel.setStyle("-fx-font-weight: bold;");
                allPlayersGrid.add(soldierLabel, 0, rowIndex);

                VBox resourcesBox = ResourceDisplay.createResourceBox(soldier);
                allPlayersGrid.add(resourcesBox, 1, rowIndex++);
            }
        }
    }
    @FXML
    private void handleNewGame() {
       gameEnded= false;
        totalTurns = 0;
        initialize();

    }
    @FXML
    private void handleExit() {
        Platform.exit();

    }

}






