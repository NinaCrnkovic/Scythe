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

    private Random random = new Random();

    @FXML
    public void rollRedDice() {
        int roll = random.nextInt(6) + 1;
        redPlayerResult.setText("Red: " + roll);

        // Deaktivirajte gumb za crvenog igrača i aktivirajte gumb za plavog igrača
        redPlayerRollDiceButton.setDisable(true);
        bluePlayerRollDiceButton.setDisable(false);
    }
    @FXML
    public void rollBlueDice() {
        int roll = random.nextInt(6) + 1;
        bluePlayerResult.setText("Blue: " + roll);

        // Ovdje možete dodati logiku za određivanje pobjednika i zatvaranje prozora
        if (Integer.parseInt(redPlayerResult.getText().split(": ")[1]) > roll) {
            // Crveni igrač pobjeđuje
        } else if (Integer.parseInt(redPlayerResult.getText().split(": ")[1]) < roll) {
            // Plavi igrač pobjeđuje
        } else {
            // Izjednačeno
        }
    }
}

