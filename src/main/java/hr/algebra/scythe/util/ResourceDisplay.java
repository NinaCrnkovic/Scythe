package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Soldier;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class ResourceDisplay {


    public static VBox createResourceBox(Soldier soldier) {
        VBox resourceBox = new VBox(5);

        HBox woodBox = createResourceIconBox(Constants.WOOD_ICON_PATH, soldier.getWood(), 30);
        HBox metalBox = createResourceIconBox(Constants.METAL_ICON_PATH, soldier.getMetal(), 30);
        HBox foodBox = createResourceIconBox(Constants.FOOD_ICON_PATH, soldier.getFood(), 30);

        resourceBox.getChildren().addAll(woodBox, metalBox, foodBox);

        return resourceBox;
    }

    private static HBox createResourceIconBox(String iconPath, int value, double iconSize) {
        HBox iconBox = new HBox(5);

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(ResourceDisplay.class.getResourceAsStream(iconPath))));
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);

        Label valueLabel = new Label(Integer.toString(value));
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("-fx-font-size: 12px;");

        iconBox.setAlignment(Pos.CENTER);
        iconBox.getChildren().addAll(icon, valueLabel);

        return iconBox;

    }
}
