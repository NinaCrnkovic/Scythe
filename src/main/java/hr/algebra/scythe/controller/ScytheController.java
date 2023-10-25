package hr.algebra.scythe.controller;

import hr.algebra.scythe.model.Player;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.Objects;

public class ScytheController {

    @FXML
    public BorderPane borderPane;

    @FXML
    private GridPane gameBoard;

    private static final int BOARD_SIZE = 6;
    private Player playerRed;
    private Player playerBlue;
    private Player selectedPlayer;
    private Player lastPlayerMoved = null; // New field to keep track of the last moved player

    private final ImageView[][] boardImages = new ImageView[BOARD_SIZE][BOARD_SIZE];

    // Image paths constants
    private static final String IMAGE_PATH_TEMPLATE = "/hr/algebra/scythe/view/images/%s";
    private static final String BACKGROUND_IMAGE = String.format(IMAGE_PATH_TEMPLATE, "background.jpg");
    private static final String RED_PLAYER_IMAGE = String.format(IMAGE_PATH_TEMPLATE, "playerred.jpg");
    private static final String BLUE_PLAYER_IMAGE = String.format(IMAGE_PATH_TEMPLATE, "playerblue.jpg");

    public void initialize() {
        playerRed = new Player(Player.Color.RED, 0, 0);
        playerBlue = new Player(Player.Color.BLUE, BOARD_SIZE - 1, BOARD_SIZE - 1);

        setupBoard();
        borderPane.setFocusTraversable(true);
        borderPane.requestFocus();
    }

    private void setupBoard() {
        Image transparentImage = new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE)).toExternalForm());
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ImageView tileImageView = new ImageView(transparentImage);
                tileImageView.setFitWidth(150);
                tileImageView.setFitHeight(150);

                final int finalI = i;
                final int finalJ = j;
                tileImageView.setOnMouseClicked(event -> handleTileClick(finalI, finalJ));

                gameBoard.add(tileImageView, i, j);
                boardImages[i][j] = tileImageView;
            }
        }

        updateBoard();
    }

    private Image getPlayerImage(String color) {
        return new Image(Objects.requireNonNull(getClass().getResource(
                color.equals("red") ? RED_PLAYER_IMAGE : BLUE_PLAYER_IMAGE
        )).toExternalForm());
    }

    private void handleTileClick(int x, int y) {
        if (selectedPlayer == null) {
            if (x == playerRed.getX() && y == playerRed.getY() && lastPlayerMoved != playerRed) {
                selectedPlayer = playerRed;
            } else if (x == playerBlue.getX() && y == playerBlue.getY() && lastPlayerMoved != playerBlue) {
                selectedPlayer = playerBlue;
            }
        } else {
            if (isAdjacent(selectedPlayer, x, y)) {
                moveSelectedPlayerTo(x, y);
                lastPlayerMoved = selectedPlayer; // After moving the player, update the last moved player
            }
            selectedPlayer = null; // Reset the selection after the move attempt
        }
        updateBoard();
    }

    private boolean isAdjacent(Player player, int x, int y) {
        int dx = x - player.getX();
        int dy = y - player.getY();

        return (Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0);
    }

    private void moveSelectedPlayerTo(int x, int y) {
        if (isValidMove(x, y)) {
            selectedPlayer.setX(x);
            selectedPlayer.setY(y);
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    private void updateBoard() {
        Image background = new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE)).toExternalForm());

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardImages[i][j].setImage(background);  // Postavljanje svih na pozadinsku sliku

                if (i == playerRed.getX() && j == playerRed.getY()) {
                    boardImages[i][j].setImage(getPlayerImage("red"));
                } else if (i == playerBlue.getX() && j == playerBlue.getY()) {
                    boardImages[i][j].setImage(getPlayerImage("blue"));
                }
            }
        }
    }
}


