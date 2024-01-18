package hr.algebra.scythe.util;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Soldier;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Set;

public class GameLogic {


    public static boolean isAdjacent(Soldier soldier, int x, int y) {
        int dx = x - soldier.getX();
        int dy = y - soldier.getY();
        return (Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0);
    }

    public static boolean isValidMove(int x, int y, int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    public static void handleBattle(Soldier selectedSoldier, Player currentPlayer, int x, int y,
                                    PlayerService playerService,
                                    BattleService battleService,
                                    Set<Soldier> soldiersMoved,
                                    GridPane allPlayersGrid) {

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

        ResourceDisplay.updateAllPlayerInfo(List.of(playerService.getPlayerRed(), playerService.getPlayerBlue()), allPlayersGrid);
    }

    public static Player endGame(PlayerService playerService) {
        int redResources = playerService.getPlayerRed().totalResources();
        int blueResources = playerService.getPlayerBlue().totalResources();

        if (redResources > blueResources) {
            return playerService.getPlayerRed();
        } else if (blueResources > redResources) {
            return playerService.getPlayerBlue();
        } else {
            return null; // It's a tie
        }
    }


}
