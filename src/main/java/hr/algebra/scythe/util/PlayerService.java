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
            if (playerRed.getSoldier(i).isAt(x, y) && (lastSoldierMoved == null || !lastSoldierMoved.equals(playerRed.getSoldier(i)))) {
                return playerRed.getSoldier(i);
            }
            if (playerBlue.getSoldier(i).isAt(x, y) && (lastSoldierMoved == null || !lastSoldierMoved.equals(playerBlue.getSoldier(i)))) {
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


    public void gatherResourcesFromTile(Soldier soldier, Tile tile) {
        int woodGained = 0;
        int metalGained = 0;
        int foodGained = 0;

        switch (tile.getType()) {
            case FOREST:
                woodGained = gatherRandomResources(1, 3);
                break;
            case MOUNTAIN:
                metalGained = gatherRandomResources(1, 3);
                break;
            case VILLAGE:
                foodGained = gatherRandomResources(1, 3);
                break;
            default:
                break;
        }

        soldier.addWood(woodGained);
        soldier.addMetal(metalGained);
        soldier.addFood(foodGained);

        StringBuilder message = new StringBuilder("You got ");
        if (woodGained > 0) {
            message.append(woodGained).append(" wood");
        }
        if (metalGained > 0) {
            if (woodGained > 0) {
                message.append(", ");
            }
            message.append(metalGained).append(" metal");
        }
        if (foodGained > 0) {
            if (woodGained > 0 || metalGained > 0) {
                message.append(", ");
            }
            message.append(foodGained).append(" food");
        }

        message.append(".");

        // Prikaži pop-up poruku
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resource Gain");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());

        // Prikazi poruku i čekaj na potvrdu korisnika
        alert.showAndWait();
    }

    private int gatherRandomResources(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }


}

