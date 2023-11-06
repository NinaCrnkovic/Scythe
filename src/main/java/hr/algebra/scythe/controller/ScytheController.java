package hr.algebra.scythe.controller;
import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Tile;
import hr.algebra.scythe.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private static final int MAX_TURNS = 10;
    private boolean gameEnded = false;

    private Set<Soldier> soldiersMoved = new HashSet<>();

    Set<Soldier> soldiersAttacked = new HashSet<>();

    private Player redPlayer;
    private Player bluePlayer;
    private Player.Color currentPlayerTurn;
    private int numberOfMoves;

    public void initialize() {
        playerService.initializePlayers();
        redPlayer = playerService.getPlayerRed();
        bluePlayer = playerService.getPlayerBlue();
        currentPlayer = redPlayer;
        currentPlayerTurn = currentPlayer.getColor();

        boardService = new BoardService(gameBoard, this::handleTileClick, redPlayer, bluePlayer);
        boardService.setupBoard();

        battleService = new BattleService(playerService, windowUtil, resourceService);

        borderPane.setFocusTraversable(true);
        borderPane.requestFocus();

        movesMade = 0;
        numberOfMoves = 0; // Initialize number of moves

        ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);
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
            System.out.println("klik.");

    } else {
            if (GameLogic.isAdjacent(selectedSoldier, x, y)) {
                if (!playerService.isTileOccupiedBySameColor(currentPlayer, x, y)) {
                    if (playerService.isTileOccupiedByOpponent(currentPlayer, x, y)) {
                        GameLogic.handleBattle(selectedSoldier, currentPlayer, x, y, playerService, battleService, soldiersMoved, allPlayersGrid);
                        ResourceDisplay.updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()), allPlayersGrid);
                        System.out.println("klik.");
                    } else {
                        playerService.moveSelectedSoldierTo(selectedSoldier, x, y, resourceService, allPlayersGrid);

                        soldiersMoved.add(selectedSoldier);
                        System.out.println("klik.");
                    }
                    selectedSoldier = null;
                    if (soldiersMoved.size() == 3) {
                        switchPlayer();
                        soldiersMoved.clear();
                        System.out.println("klik.");
                    }
                }
            }
        }
        boardService.updateBoard();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == redPlayer) ? bluePlayer : redPlayer;
        currentPlayerTurn = currentPlayer.getColor();
        totalTurns++;
        numberOfMoves = 0; // Reset the move counter for the new player's turn

        if (totalTurns >= MAX_TURNS) {
            gameEnded = true;
            GameLogic.endGame(playerService, allPlayersGrid);
            return;
        }

        soldiersMoved.clear();
        ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);
    }

    @FXML
    private void handleNewGame() {
       gameEnded= false;
        totalTurns = 0;
        numberOfMoves = 0;
        PlayerService.getPlayerRed().resetAllSoldierResources();
        PlayerService.getPlayerBlue().resetAllSoldierResources();
        initialize();

    }
    @FXML
    private void handleExit() {
        Platform.exit();

    }
    @FXML
    public void generateHtmlDocumentation() {
        DocumentationUtils.generateHtmlDocumentationFile();
    }

    @FXML
    private void saveGame() {
        try {
            FileUtils.saveGame(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves);
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    @FXML
    private void loadGame() {
        try {
            GameState loadedGameState = FileUtils.loadGame();
            currentPlayerTurn = loadedGameState.getCurrentPlayerTurn();
            redPlayer = loadedGameState.getRedPlayer();
            bluePlayer = loadedGameState.getBluePlayer();
            numberOfMoves = loadedGameState.getNumberOfMoves();
            currentPlayer = (currentPlayerTurn == Player.Color.RED) ? redPlayer : bluePlayer;
            playerService.updatePlayers(redPlayer, bluePlayer);
            boardService.updatePlayers(redPlayer, bluePlayer);
            boardService.updateBoard();// This sets up the grid and tiles
            ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }





}







