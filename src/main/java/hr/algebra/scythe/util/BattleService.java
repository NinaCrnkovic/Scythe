package hr.algebra.scythe.util;

import hr.algebra.scythe.ScytheApplication;
import hr.algebra.scythe.model.NetworkConfiguration;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.RoleName;
import hr.algebra.scythe.model.Soldier;

import java.io.IOException;
import java.net.Socket;

public class BattleService {
    private PlayerService playerService;
    private WindowUtil windowUtil;
    private ResourceService resourceService;

    private static final String  INITIATE_DICE_ROLL = "Battle! Roll the dice!";

    private int redDiceRollResult = -1;
    private int blueDiceRollResult = -1;

    public BattleService(PlayerService playerService, WindowUtil windowUtil, ResourceService resourceService) {
        this.playerService = playerService;
        this.windowUtil = windowUtil;
        this.resourceService = resourceService;
    }
    public void requestDiceRoll() {
        NetworkingUtils.sendDiceRollRequestToServer("REQUEST_DICE_ROLL");
    }



    public boolean initiateAttack(Soldier attacker, Player currentPlayer, int toX, int toY, int originalX, int originalY) {
        Player opponent = (currentPlayer.getColor() == Player.Color.RED) ? playerService.getPlayerBlue() : playerService.getPlayerRed();
        Soldier defender = playerService.getSelectedSoldier(toX, toY, null);

        if (defender == null) {
            return false;
        }

        int[] rolls = windowUtil.openDiceRollWindow();
        int redRoll = rolls[0];
        int blueRoll = rolls[1];

        boolean isRedAttacker = (attacker.getColor() == Player.Color.RED);
        boolean attackerWins = (isRedAttacker && redRoll > blueRoll) || (!isRedAttacker && blueRoll > redRoll);

        if (attackerWins) {
            resourceService.transferResources(attacker, defender);
        }

        playerService.returnAttackerToOriginalPosition(attacker, originalX, originalY);
        return true;  // Napad je izveden
    }
    /*public void initiateDiceRoll() {
        // Otvara prozor za bacanje kockica za specificiranog igrača
        windowUtil.openDiceRollWindow();
    }*/









}

