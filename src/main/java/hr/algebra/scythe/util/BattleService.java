package hr.algebra.scythe.util;

import hr.algebra.scythe.model.DiceRoll;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Soldier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Random;

public class BattleService {


    private PlayerService playerService;
    private WindowUtil windowUtil;
    private ResourceService resourceService;

    public static DiceRoll diceRoll;

    private Random random = new Random();




    public BattleService(PlayerService playerService, WindowUtil windowUtil, ResourceService resourceService) {
        this.playerService = playerService;
        this.windowUtil = windowUtil;
        this.resourceService = resourceService;
    }

    public boolean initiateAttack(Soldier attacker, Player currentPlayer, int toX, int toY, int originalX, int originalY) {
        //Player opponent = (currentPlayer.getColor() == Player.Color.RED) ? playerService.getPlayerBlue() : playerService.getPlayerRed();
        Soldier defender = playerService.getSelectedSoldier(toX, toY, null);

        if (defender == null) {
            return false;
        }

        diceRoll = new DiceRoll();
        rollDiceForPlayers();

        checkWinner();

        boolean isRedAttacker = (attacker.getColor() == Player.Color.RED);
        boolean attackerWins = (isRedAttacker && diceRoll.getRedRoll() > diceRoll.getBlueRoll()) || (!isRedAttacker && diceRoll.getBlueRoll() > diceRoll.getRedRoll());

        if (attackerWins) {
            resourceService.transferResources(attacker, defender);
        }

        playerService.returnAttackerToOriginalPosition(attacker, originalX, originalY);
        return true;  // Napad je izveden
    }

    private void rollDiceForPlayers() {

        do {
             diceRoll.setRedRoll(random.nextInt(6) + 1);
             diceRoll.setBlueRoll( random.nextInt(6) + 1);
        } while (diceRoll.getBlueRoll() == diceRoll.getRedRoll());
    }

        private void checkWinner() {
            if (diceRoll.getRedRoll() > diceRoll.getBlueRoll()) {
                diceRoll.setWinner("Red Player wins!");
            } else if (diceRoll.getBlueRoll() > diceRoll.getRedRoll() ) {
                diceRoll.setWinner("Blue Player wins!");
            } else {
                diceRoll.setWinner("It's a tie!");
            }
        }

    public static DiceRoll getDiceRoll() {
        return diceRoll;
    }


}







