package hr.algebra.scythe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Random;

public class DiceRollController {

    @FXML
    private Button redPlayerRollDiceButton;
    @FXML
    private Label redPlayerResult;

    @FXML
    private Button bluePlayerRollDiceButton;
    @FXML
    private Label bluePlayerResult;

    @FXML
    private Label gameResult;

    private Random random = new Random();

    @FXML
    public void rollRedDice() {
        int roll = random.nextInt(6) + 1;
        redPlayerResult.setText("Red: " + roll);


        redPlayerRollDiceButton.setDisable(true);
        bluePlayerRollDiceButton.setDisable(false);
    }

    @FXML
    public void rollBlueDice() {
        int roll = random.nextInt(6) + 1;
        bluePlayerResult.setText("Blue: " + roll);

        int redRoll = getRedPlayerRoll();
        if (redRoll > roll) {
            gameResult.setText("Red Player wins!");
            bluePlayerRollDiceButton.setDisable(true);
        } else if (redRoll < roll) {
            gameResult.setText("Blue Player wins!");
            bluePlayerRollDiceButton.setDisable(true);
        } else {
            gameResult.setText("It's a tie! Roll again.");
            bluePlayerRollDiceButton.setDisable(false);
            redPlayerRollDiceButton.setDisable(false);
        }
    }

    public int getRedPlayerRoll() {
        return Integer.parseInt(redPlayerResult.getText().split(": ")[1]);
    }

    public int getBluePlayerRoll() {
        return Integer.parseInt(bluePlayerResult.getText().split(": ")[1]);
    }


}

