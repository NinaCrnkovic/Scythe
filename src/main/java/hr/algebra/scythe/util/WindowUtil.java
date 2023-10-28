package hr.algebra.scythe.util;

import hr.algebra.scythe.controller.DiceRollController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowUtil {

    public int[] openDiceRollWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WindowUtil.class.getResource(Constants.DICE_ROLL_PATH));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Roll Dice");
            stage.setScene(new Scene(root));
            stage.showAndWait();


            DiceRollController controller = fxmlLoader.getController();
            int redRoll = controller.getRedPlayerRoll();
            int blueRoll = controller.getBluePlayerRoll();


            return new int[]{redRoll, blueRoll};

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
