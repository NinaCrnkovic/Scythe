package hr.algebra.scythe.controller;

import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Tile;
import hr.algebra.scythe.util.BoardService;
import hr.algebra.scythe.util.PlayerService;
import hr.algebra.scythe.util.GameLogic;
import hr.algebra.scythe.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ScytheController {
    @FXML
    public BorderPane borderPane;

    @FXML
    private GridPane gameBoard;

    @FXML
    private GridPane allPlayersGrid;

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
        updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));

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

            // After setting the new position, gather resources
            Tile tileOnPosition = BoardService.getTile(x, y);
            playerService.gatherResourcesFromTile(selectedSoldier, tileOnPosition);

            // Update the player info UI after each move
            updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));
        }
    }



    private void switchPlayer() {
        if (currentPlayer == playerService.getPlayerRed()) {
            currentPlayer = playerService.getPlayerBlue();
        } else {
            currentPlayer = playerService.getPlayerRed();
        }
        soldiersMoved.clear();  // Clear the set when switching players

        // Update the player info UI after switching the player
        updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()));
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

    private void updateAllPlayerInfo(List<Player> players) {
        allPlayersGrid.getChildren().clear();  // Ukloni sve trenutne informacije

        int rowIndex = 0;

        for (Player player : players) {
            Label playerLabel = new Label(player.getColor().toString() + " Player");

            if (player.getColor() == Player.Color.RED) {
                playerLabel = new Label("Red Player");
                playerLabel.getStyleClass().add("red-player");
            } else {
                playerLabel = new Label("Blue Player");
                playerLabel.getStyleClass().add("blue-player");
            }

            allPlayersGrid.add(playerLabel, 0, rowIndex++);

            for (int i = 0; i < 3; i++) {
                Soldier soldier = player.getSoldier(i);

                Label soldierLabel = new Label("Soldier " + (i + 1));  // +1 jer želimo vojnike označiti kao Soldier 1, Soldier 2, itd.
                soldierLabel.setStyle("-fx-font-weight: bold;");
                allPlayersGrid.add(soldierLabel, 0, rowIndex);

                VBox resourcesBox = createResourceBox(soldier);
                allPlayersGrid.add(resourcesBox, 1, rowIndex++);


            }
        }
    }

    private VBox createResourceBox(Soldier soldier) {
        VBox resourceBox = new VBox(5);

        HBox woodBox = createResourceIconBox(Constants.WOOD_ICON_PATH, soldier.getWood(), 32);
        HBox metalBox = createResourceIconBox(Constants.METAL_ICON_PATH, soldier.getMetal(), 32);
        HBox foodBox = createResourceIconBox(Constants.FOOD_ICON_PATH, soldier.getFood(), 32);


        resourceBox.getChildren().addAll(woodBox, metalBox, foodBox);

        return resourceBox;
    }

    private HBox createResourceIconBox(String iconPath, int value, double iconSize) {
        HBox iconBox = new HBox(5);

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);

        Label valueLabel = new Label(Integer.toString(value));

        iconBox.getChildren().addAll(icon, valueLabel);

        return iconBox;
    }







}





