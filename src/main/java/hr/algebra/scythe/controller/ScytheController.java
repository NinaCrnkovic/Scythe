package hr.algebra.scythe.controller;
import hr.algebra.scythe.ScytheApplication;
import hr.algebra.scythe.chat.RemoteChatService;
import hr.algebra.scythe.model.*;
import hr.algebra.scythe.thread.GetLastGameMoveThread;
import hr.algebra.scythe.thread.SaveNewGameMoveThread;
import hr.algebra.scythe.util.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.ActionEvent;

public class ScytheController {
    @FXML
    private Label battle, redPlayerResult, bluePlayerResult, gameResult, theLastGameMoveLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gameBoard, allPlayersGrid, chatGrid;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField chatMessageTextField;

    private Soldier selectedSoldier;
    private Soldier lastSoldierMoved = null;
    private static final PlayerService playerService = new PlayerService();
    private final WindowUtil windowUtil = new WindowUtil();
    private final ResourceService resourceService = new ResourceService();
    private static BoardService boardService;
    private BattleService battleService;
    private static Player currentPlayer;
    private int movesMade;
    private int totalTurns = 0;
    private static final int MAX_TURNS = 3;
    private boolean gameEnded = false;
    private Set<Soldier> soldiersMoved = new HashSet<>();
    List<GameState> allGameStates = new ArrayList<>();
    private static Player redPlayer, bluePlayer;
    private static Player.Color currentPlayerTurn;
    private static int numberOfMoves;
    public static RemoteChatService remoteChatService;
    private DiceRoll diceRoll = new DiceRoll();

    public void initialize() {
        chatMessageTextField.setOnAction(e -> {
            if (!chatMessageTextField.getText().isBlank()) {
                ChatUtils.sendChatMessage(chatMessageTextField.getText(), chatTextArea);
                chatMessageTextField.clear();
            }
        });
        playerService.initializePlayers();
        redPlayer = PlayerService.getPlayerRed();
        bluePlayer = PlayerService.getPlayerBlue();
        currentPlayer = redPlayer;
        currentPlayerTurn = currentPlayer.getColor();
        boardService = new BoardService(gameBoard, this::handleTileClick, redPlayer, bluePlayer);
        boardService.setupBoard();
        battleService = new BattleService(playerService, windowUtil, resourceService);
        borderPane.setFocusTraversable(true);
        borderPane.requestFocus();
        movesMade = 0;
        numberOfMoves = 0;
        unvisibleLables();
        ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);
        if (ScytheApplication.loggedInRoleName == RoleName.SERVER) {
            RmiUtils.startRmiRemoteChatServer();
        } else if (ScytheApplication.loggedInRoleName == RoleName.CLIENT) {
            RmiUtils.startRmiRemoteChatClient();
        }
        if (ScytheApplication.loggedInRoleName != RoleName.SINGLE_PLAYER) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> ChatUtils.refreshChatMessages(chatTextArea)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();

            chatMessageTextField.setOnKeyPressed(event -> {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            onClickSendChatMessage();
                        }
                    }
            );
        }
        GetLastGameMoveThread getLastGameMoveThread
                = new GetLastGameMoveThread(theLastGameMoveLabel);
        Thread starterThread = new Thread(getLastGameMoveThread);
        starterThread.start();
    }
    private void handleTileClick(int x, int y) {
        unvisibleLables();
        if (gameEnded) {
            return;
        }
        if ((ScytheApplication.loggedInRoleName == RoleName.CLIENT && currentPlayerTurn != Player.Color.BLUE) ||
                (ScytheApplication.loggedInRoleName == RoleName.SERVER && currentPlayerTurn != Player.Color.RED)) {
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
                        GameLogic.handleBattle(selectedSoldier, currentPlayer, x, y, playerService, battleService, soldiersMoved, allPlayersGrid);
                        ResourceDisplay.updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()), allPlayersGrid);
                        diceRoll = BattleService.getDiceRoll();
                        updateBattleLabels();
                    } else {
                        playerService.moveSelectedSoldierTo(selectedSoldier, x, y, resourceService, allPlayersGrid);
                        soldiersMoved.add(selectedSoldier);
                    }
                    selectedSoldier = null;
                    GameMove gameMove = new GameMove(currentPlayer.getColor(), x, y, LocalDateTime.now());
                    XmlUtils.saveGameMove(gameMove);
                    SaveNewGameMoveThread saveNewGameMoveThread = new SaveNewGameMoveThread(gameMove);
                    Thread staterThread = new Thread(saveNewGameMoveThread);
                    staterThread.start();
                    if (soldiersMoved.size() == 3) {
                        switchPlayer();
                        soldiersMoved.clear();
                    }
                }
            }
            GameState gameState = GameStateUtils.createGameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, gameEnded, diceRoll);
            allGameStates.add(gameState);
            if (gameEnded) {
                gameState.setGameOver(true);
                handleGameEnd();
                if (ScytheApplication.loggedInRoleName == RoleName.CLIENT) {
                    NetworkingUtils.sendGameStateToServer(gameState);
                } else if (ScytheApplication.loggedInRoleName == RoleName.SERVER) {
                    NetworkingUtils.sendGameStateToClient(gameState);
                }
            } else {
                if (ScytheApplication.loggedInRoleName == RoleName.CLIENT) {
                    NetworkingUtils.sendGameStateToServer(gameState);
                } else if (ScytheApplication.loggedInRoleName == RoleName.SERVER) {
                    NetworkingUtils.sendGameStateToClient(gameState);
                }
            }
        }
        boardService.updateBoard();
    }
    private void switchPlayer() {
        currentPlayer = (currentPlayer == redPlayer) ? bluePlayer : redPlayer;
        currentPlayerTurn = currentPlayer.getColor();
        totalTurns++;
        numberOfMoves = 0;
        if (totalTurns >= MAX_TURNS) {
            gameEnded = true;
            GameLogic.endGame(playerService);
            return;
        }
        soldiersMoved.clear();
        ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);
    }
    private void handleGameEnd() {
        MessageService.displayEndGameMessage(redPlayer.totalResources(), bluePlayer.totalResources());
    }
    @FXML
    public void handleNewGame() {
        resetGame();
        XmlUtils.createNewFile();
    }
    public void resetGame() {
        gameEnded = false;
        totalTurns = 0;
        numberOfMoves = 0;
        PlayerService.getPlayerRed().resetAllSoldierResources();
        PlayerService.getPlayerBlue().resetAllSoldierResources();
        initialize();
        GameState gameState = GameStateUtils.createGameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, gameEnded, diceRoll);
        if (RoleName.CLIENT.name().equals(ScytheApplication.loggedInRoleName.name())) {
            NetworkingUtils.sendGameStateToServer(gameState);
        } else if (RoleName.SERVER.name().equals(ScytheApplication.loggedInRoleName.name())) {
            NetworkingUtils.sendGameStateToClient(gameState);
        }
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
            FileUtils.saveGame(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, gameEnded, diceRoll);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void loadGame() {
        try {
            GameState loadedGameState = FileUtils.loadGame();
            updateGameBoard(loadedGameState);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void updateGameBoard(GameState gameState) {
        currentPlayerTurn = gameState.getCurrentPlayerTurn();
        redPlayer = gameState.getRedPlayer();
        bluePlayer = gameState.getBluePlayer();
        numberOfMoves = gameState.getNumberOfMoves();
        gameEnded = gameState.isGameOver();
        if (gameEnded) {

            MessageService.displayEndGameMessage(redPlayer.totalResources(), bluePlayer.totalResources());
        }
        currentPlayer = (currentPlayerTurn == Player.Color.RED) ? redPlayer : bluePlayer;
        playerService.updatePlayers(redPlayer, bluePlayer);
        boardService.updatePlayers(redPlayer, bluePlayer);
        boardService.updateBoard();
        diceRoll = gameState.getDiceRoll();
        updateBattleLabels();
        ResourceDisplay.updateAllPlayerInfo(List.of(redPlayer, bluePlayer), allPlayersGrid);
    }
    private void updateBattleLabels() {
        if (diceRoll != null) {
            if (diceRoll.getRedRoll() == 0 && diceRoll.getBlueRoll() == 0) {
                return;
            }
            battle.setText("Battle!! Dice Roll: ");
            redPlayerResult.setText("Red Player: " + diceRoll.getRedRoll());
            bluePlayerResult.setText("Blue Player: " + diceRoll.getBlueRoll());
            gameResult.setText(diceRoll.getWinner());
            visibleLables();
        }
    }
    private void visibleLables() {
        battle.setVisible(true);
        redPlayerResult.setVisible(true);
        bluePlayerResult.setVisible(true);
        gameResult.setVisible(true);
    }
    private void unvisibleLables() {
        battle.setVisible(false);
        redPlayerResult.setVisible(false);
        bluePlayerResult.setVisible(false);
        gameResult.setVisible(false);
    }
    public void onClickSendChatMessage() {
        String chatMessage = chatMessageTextField.getText();
        chatMessageTextField.clear();
        ChatUtils.sendChatMessage(chatMessage, chatTextArea);
    }
    @FXML
    public void replayGame() {
        List<GameMove> gameMoveList = XmlUtils.getAllGameMoves();
        AtomicInteger i = new AtomicInteger(0);
        final Timeline replayer = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                GameMove gameMove = gameMoveList.get(i.get());

                                System.out.println(gameMove.toString());
                                i.set(i.get() + 1);
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        replayer.setCycleCount(gameMoveList.size());
        replayer.play();
    }





}









