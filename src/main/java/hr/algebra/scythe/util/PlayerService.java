package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Tile;
import javafx.scene.control.Alert;

import java.util.Random;

public class PlayerService {

    private final Player playerRed = new Player(Player.Color.RED);
    private final Player playerBlue = new Player(Player.Color.BLUE);
    private static final int[][] RED_SOLDIER_START_POSITIONS = {
            {0, 0},
            {0, 1},
            {1, 0}
    };

    private static final int[][] BLUE_SOLDIER_START_POSITIONS = {
            {5, 5},
            {4, 5},
            {5, 4}
    };

    public void initializePlayers() {
        setSoldierPositions(playerRed, RED_SOLDIER_START_POSITIONS);
        setSoldierPositions(playerBlue, BLUE_SOLDIER_START_POSITIONS);
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
    public Player getPlayerRed() {
        return playerRed;
    }

    public Player getPlayerBlue() {
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


}

