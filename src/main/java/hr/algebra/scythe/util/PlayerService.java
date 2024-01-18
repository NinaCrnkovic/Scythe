package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Tile;
import javafx.scene.layout.GridPane;

import java.util.List;


public class PlayerService {

    private static Player playerRed;
    private static Player playerBlue;

    private BoardService boardService;

    public PlayerService(BoardService boardService) {
        this.boardService = boardService;
        this.playerRed = new Player(Player.Color.RED);
        this.playerBlue = new Player(Player.Color.BLUE);
    }

    public PlayerService() {
        this(new BoardService());
    }

    public void initializePlayers() {
        setSoldierPositions(playerRed, Constants.RED_SOLDIER_START_POSITIONS);
        setSoldierPositions(playerBlue, Constants.BLUE_SOLDIER_START_POSITIONS);
    }

    private void setSoldierPositions(Player player, int[][] positions) {
        for (int i = 0; i < positions.length; i++) {
            player.setSoldierPosition(i, positions[i][0], positions[i][1]);
        }
    }

    public Soldier getSelectedSoldier(int x, int y, Soldier lastSoldierMoved) {
        for (int i = 0; i < 3; i++) {
            if (playerRed.getSoldier(i).isAt(x, y)) {
                return playerRed.getSoldier(i);
            }
            if (playerBlue.getSoldier(i).isAt(x, y)) {
                return playerBlue.getSoldier(i);
            }
        }
        return null;
    }

    public void updatePlayers(Player updatedPlayerRed, Player updatedPlayerBlue) {
        this.playerRed = updatedPlayerRed;
        this.playerBlue = updatedPlayerBlue;
        setPlayersOnBoard();
    }




    public void setPlayersOnBoard() {

        boardService.updateBoard();
    }


    public boolean isTileOccupiedBySameColor(Player currentPlayer, int x, int y) {
        Player playerToCheck = (currentPlayer.getColor() == Player.Color.RED) ? playerRed : playerBlue;
        for (int i = 0; i < 3; i++) {
            if (playerToCheck.getSoldier(i).isAt(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTileOccupiedByOpponent(Player currentPlayer, int x, int y) {
        Player opponent = (currentPlayer.getColor() == Player.Color.RED) ? playerBlue : playerRed;
        for (int i = 0; i < 3; i++) {
            if (opponent.getSoldier(i).isAt(x, y)) {
                return true;
            }
        }
        return false;
    }
    public static Player getPlayerRed() {
        return playerRed;
    }

    public static Player getPlayerBlue() {
        return playerBlue;
    }

    public static void returnAttackerToOriginalPosition(Soldier attacker, int originalX, int originalY) {
        attacker.setX(originalX);
        attacker.setY(originalY);
    }

    public static void returnSoldierToOriginalPosition(Soldier soldier, int originalX, int originalY) {
        soldier.setX(originalX);
        soldier.setY(originalY);
    }

    public static void moveSelectedSoldierTo(Soldier selectedSoldier, int x, int y, ResourceService resourceService, GridPane allPlayersGrid) {
        if (GameLogic.isValidMove(x, y, Constants.BOARD_SIZE)) {
            selectedSoldier.setX(x);
            selectedSoldier.setY(y);
            Tile tileOnPosition = BoardService.getTile(x, y);
            resourceService.gatherResourcesFromTile(selectedSoldier, tileOnPosition);
            ResourceDisplay.updateAllPlayerInfo(List.of(getPlayerRed(), getPlayerBlue()), allPlayersGrid);
        }
    }

}

