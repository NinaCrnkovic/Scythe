package hr.algebra.scythe.util;

import hr.algebra.scythe.controller.DiceRollController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowUtil {

    public int[] openDiceRollWindow() {
        int[] results = new int[2];
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.DICE_ROLL_PATH));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Battle! Dice Roll");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                DiceRollController controller = loader.getController();
                results[0] = controller.getRedRoll();
                results[1] = controller.getBlueRoll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return results;
    }






}
