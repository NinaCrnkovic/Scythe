package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Tile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Objects;

public class BoardService {


    private final ImageView[][] boardImages = new ImageView[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    @FXML
    private GridPane gameBoard;
    private final TileClickHandler tileClickHandler;

    private final Player playerRed;
    private final Player playerBlue;

    public BoardService(GridPane gameBoard, TileClickHandler tileClickHandler, Player playerRed, Player playerBlue) {
        this.gameBoard = gameBoard;
        this.tileClickHandler = tileClickHandler;
        this.playerRed = playerRed;
        this.playerBlue = playerBlue;
    }


    private Tile getTileByIndex(int i, int j) {
        return getTile(i, j);
    }

    public static Tile getTile(int i, int j) {
        if ((i + j) % 3 == 0) {
            return new Tile(Tile.Type.VILLAGE);
        } else if ((i + j) % 3 == 1) {
            return new Tile(Tile.Type.FOREST);
        } else {
            return new Tile(Tile.Type.MOUNTAIN);
        }
    }


    public void setBattleImage(int x, int y) {
        Image battleImage = new Image(Objects.requireNonNull(getClass().getResource(Constants.BATTLE_IMAGE_PATH)).toExternalForm());
        ImageView imageView = new ImageView(battleImage);
        imageView.setFitWidth(Constants.TILE_WIDTH);
        imageView.setFitHeight(Constants.TILE_HEIGHT);
        gameBoard.add(imageView, x, y);
        boardImages[x][y] = imageView;
    }

    public void removeBattleImage(int x, int y) {
        // Uzima sliku vojnika i postavlja je natrag
        for (int k = 0; k < 3; k++) {
            Soldier redSoldier = playerRed.getSoldier(k);
            if (redSoldier.isAt(x, y)) {
                boardImages[x][y].setImage(redSoldier.getImage());
            } else {
                Soldier blueSoldier = playerBlue.getSoldier(k);
                if (blueSoldier.isAt(x, y)) {
                    boardImages[x][y].setImage(blueSoldier.getImage());
                }
            }
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }



    public void setupBoard() {
        gameBoard.getRowConstraints().clear();
        gameBoard.getColumnConstraints().clear();

        // Postavljanje fiksne visine i širine za svaki red i kolonu unutar GridPane
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(Constants.TILE_HEIGHT+Constants.MARGIN);
            gameBoard.getRowConstraints().add(rowConstraints);

            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(Constants.TILE_WIDTH+Constants.MARGIN);
            gameBoard.getColumnConstraints().add(colConstraints);
        }
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                Tile tile = getTileByIndex(i, j);
                ImageView tileImageView = new ImageView(tile.getImage());
                tileImageView.setFitWidth(Constants.TILE_WIDTH);
                tileImageView.setFitHeight(Constants.TILE_HEIGHT);

                final int finalI = i;
                final int finalJ = j;
                tileImageView.setOnMouseClicked(event -> tileClickHandler.handle(finalI, finalJ));


                gameBoard.add(tileImageView, i, j);
                boardImages[i][j] = tileImageView;
            }
        }
        updateBoard();
    }

    public void updateBoard() {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                Tile tile = getTileByIndex(i, j);
                boardImages[i][j].setImage(tile.getImage());

                for (int k = 0; k < 3; k++) {
                    Soldier redSoldier = playerRed.getSoldier(k);
                    if (redSoldier.isAt(i, j)) {
                        boardImages[i][j].setImage(redSoldier.getImage());
                    } else {
                        Soldier blueSoldier = playerBlue.getSoldier(k);
                        if (blueSoldier.isAt(i, j)) {
                            boardImages[i][j].setImage(blueSoldier.getImage());
                        }
                    }
                }
            }
        }
    }
}

