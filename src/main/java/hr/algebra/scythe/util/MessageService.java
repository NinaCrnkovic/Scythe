package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Player;
import javafx.scene.control.Alert;

public class MessageService {

    public static void displayEndGameMessage(int redResources, int blueResources) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");

        StringBuilder message = new StringBuilder();

        if(redResources == blueResources) {
            message.append("It's a tie!\n");
        } else if (redResources > blueResources) {
            message.append("Red player wins!\n");
        }
        else {
            message.append("Blue player wins!\n");
        }

        message.append("Red Player had ").append(redResources).append(" resources.\n");
        message.append("Blue Player had ").append(blueResources).append(" resources.");

        alert.setContentText(message.toString());
        alert.showAndWait();

    }
}
