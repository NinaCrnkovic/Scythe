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

    private static final ImageView[][] boardImages = new ImageView[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    @FXML
    private static GridPane gameBoard;
    private static TileClickHandler tileClickHandler = null;

    private Player playerRed;
    private Player playerBlue;

    public BoardService(GridPane gameBoard, TileClickHandler tileClickHandler, Player playerRed, Player playerBlue) {
        this.gameBoard = gameBoard;
        this.tileClickHandler = tileClickHandler;
        this.playerRed = playerRed;
        this.playerBlue = playerBlue;
    }

    public BoardService() {
        this.tileClickHandler = null;
        this.playerRed = null;
        this.playerBlue = null;
    }


    // Dodajte nove metode u BoardService za ažuriranje igrača
    public void updatePlayers(Player redPlayer, Player bluePlayer) {
        this.playerRed = redPlayer;
        this.playerBlue = bluePlayer;
    }

    public void placeSoldiersOnBoard() {
        // Očistite ploču od prethodno postavljenih vojnika
        clearSoldiersFromBoard();

        // Postavite vojnike crvenog igrača
        placePlayerSoldiers(playerRed);

        // Postavite vojnike plavog igrača
        placePlayerSoldiers(playerBlue);
    }

    private void placePlayerSoldiers(Player player) {
        if (player != null) {
            for (Soldier soldier : player.getSoldiers()) {
                if (soldier != null) {
                    int x = soldier.getX();
                    int y = soldier.getY();



                    // Log the coordinates where the soldier is expected to be placed
                    System.out.println("Placing " + player.getColor().toString() + " soldier at: (" + x + ", " + y + ")");
                    if (isValidPosition(x, y)) {
                        ImageView soldierImageView = new ImageView(soldier.getImage());

                        soldierImageView.setOnMouseClicked(event -> tileClickHandler.handle(x, y));


                        soldierImageView.setFitWidth(Constants.TILE_WIDTH);
                        soldierImageView.setFitHeight(Constants.TILE_HEIGHT);
                        gameBoard.add(soldierImageView, x, y); // Pretpostavljam da je gameBoard GridPane ili sličan kontejner
                        boardImages[x][y] = soldierImageView;
                    } else {
                        // Log if the position is invalid
                        System.out.println("Invalid position for soldier at: (" + x + ", " + y + ")");
                    }
                }
            }
        }
    }

    // Metoda za provjeru valjanosti pozicije na ploči
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < Constants.BOARD_SIZE && y >= 0 && y < Constants.BOARD_SIZE;
    }



    public static void clearSoldiersFromBoard() {
        // Prođite kroz sve slike na ploči i uklonite slike vojnika
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (boardImages[i][j] != null) {
                    // Uklonite ImageView s GridPane-a (ili odgovarajućeg kontejnera)
                    gameBoard.getChildren().remove(boardImages[i][j]);
                    // Resetirajte sliku u matrici na null ili na početnu sliku pločice
                    boardImages[i][j] = null;
                }
            }
        }
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
                if (this.boardImages[i][j] != null) {
                    boardImages[i][j].setImage(tile.getImage());
                }


                // Resetiranje slikama vojnika
                setImageForSoldier(i, j, playerRed);
                setImageForSoldier(i, j, playerBlue);
            }
        }
    }

    private void setImageForSoldier(int i, int j, Player player) {
        if (player != null) {
            for (Soldier soldier : player.getSoldiers()) {
                if (soldier != null && soldier.isAt(i, j)) {
                    ImageView tileImageView = boardImages[i][j];
                    // Provjerite je li ImageView null prije nego što pokušate postaviti sliku
                    if (tileImageView != null) {
                        tileImageView.setImage(soldier.getImage());
                    } else {
                        // Možda biste htjeli dodati neku logiku ovdje, npr. stvaranje novog ImageView ako je to potrebno
                        System.out.println("Pokušaj postavljanja slike na null ImageView na koordinatama (" + i + "," + j + ")");
                    }
                    break; // Prekid petlje ako je vojnik pronađen
                }
            }
        }
    }






}

