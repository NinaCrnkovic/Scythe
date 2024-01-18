package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Soldier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;


import java.util.List;
import java.util.Objects;

public class ResourceDisplay {


    public static HBox createResourceBox(Soldier soldier) {
        HBox resourceBox = new HBox(Constants.MARGIN);

        HBox woodBox = createResourceIconBox(Constants.WOOD_ICON_PATH, soldier.getWood(), Constants.ICON_SIZE);
        HBox metalBox = createResourceIconBox(Constants.METAL_ICON_PATH, soldier.getMetal(), Constants.ICON_SIZE);
        HBox foodBox = createResourceIconBox(Constants.FOOD_ICON_PATH, soldier.getFood(), Constants.ICON_SIZE);

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
    private static ImageView createSoldierImage(Soldier soldier) {
        ImageView soldierImageView = new ImageView(soldier.getImage());
        soldierImageView.setFitWidth(Constants.IMAGE_WIDTH);
        soldierImageView.setFitHeight(Constants.IMAGE_HEIGHT);
        return soldierImageView;
    }

    public static void updateAllPlayerInfo(List<Player> players, GridPane allPlayersGrid) {
        allPlayersGrid.getChildren().clear();
        allPlayersGrid.getRowConstraints().clear();

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(70);

        for (int i = 0; i < (players.size() * 4); i++) {
            allPlayersGrid.getRowConstraints().add(rowConstraints);
        }

        int rowIndex = 0;
        for (Player player : players) {
            Label playerLabel = new Label(player.getColor().toString() + " Player");
            playerLabel.setPadding(new Insets(10, 10, 10, 10));
            playerLabel.getStyleClass().add(player.getColor().toString().toLowerCase() + "-player");
            allPlayersGrid.add(playerLabel, 0, rowIndex++, 2, 1);  // Spanning 2 columns

            for (int i = 0; i < 3; i++) {
                Soldier soldier = player.getSoldier(i);

                // Dodajemo sliku vojnika
                ImageView soldierImageView = createSoldierImage(soldier);
                Insets imageMargin = new Insets(10);  // 10px margina na svim stranama
                GridPane.setMargin(soldierImageView, imageMargin);
                allPlayersGrid.add(soldierImageView, 0, rowIndex);

                HBox resourcesBox = ResourceDisplay.createResourceBox(soldier);
                allPlayersGrid.add(resourcesBox, 1, rowIndex);

                rowIndex++;

                if (i < 2) { // Ako nije posljednji vojnik, dodajmo prazan red između vojnika za bolji vizualni razmak.
                    allPlayersGrid.add(new Label(""), 0, rowIndex++, 2, 1);
                }
            }
        }
    }


}
