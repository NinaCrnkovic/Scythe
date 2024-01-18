package hr.algebra.scythe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Random;

public class DiceRollController {

    @FXML
    private Label redPlayerResult;
    @FXML
    private Label bluePlayerResult;
    @FXML
    private Label gameResult;

    private Random random = new Random();
    private int redRoll;
    private int blueRoll;

    public void initialize() {
        rollDiceForPlayers();
        checkWinner();
    }

    private void rollDiceForPlayers() {
        do {
            redRoll = random.nextInt(6) + 1;
            blueRoll = random.nextInt(6) + 1;
        } while (redRoll == blueRoll);

        redPlayerResult.setText("Red Player Rolled: " + redRoll);
        bluePlayerResult.setText("Blue Player Rolled: " + blueRoll);
    }

    private void checkWinner() {
        if (redRoll > blueRoll) {
            gameResult.setText("Red Player wins!");
        } else if (blueRoll > redRoll) {
            gameResult.setText("Blue Player wins!");
        } else {
            gameResult.setText("It's a tie!");
        }
    }



    public int getRedRoll() {
        return redRoll;
    }

    public int getBlueRoll() {
        return blueRoll;
    }


}

