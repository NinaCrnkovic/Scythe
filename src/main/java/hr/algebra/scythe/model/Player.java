package hr.algebra.scythe.model;
import javafx.scene.control.Alert;

import java.util.List;

public class Player {
    public enum Color {
        RED, BLUE
    }
    private Color color;
    private final Soldier[] soldiers = new Soldier[3];

    public Player(Color color) {
        this.color = color;
        for (int i = 0; i < 3; i++) {
            soldiers[i] = new Soldier(color, i, 0, 0);
        }
    }

    public Color getColor() {
        return color;
    }
    public void setSoldierPosition(int index, int x, int y) {
        soldiers[index].setX(x);
        soldiers[index].setY(y);
    }

    public Soldier getSoldier(int index) {
        return soldiers[index];
    }

    public void gatherResourcesFromTile(Soldier soldier, Tile tile) {
        int woodGained = gatherRandomResources(1, 3); // Generira random broj od 1 do 3
        int metalGained = gatherRandomResources(1, 3);
        int foodGained = gatherRandomResources(1, 3);

        soldier.addWood(woodGained);
        soldier.addMetal(metalGained);
        soldier.addFood(foodGained);

        String message = String.format("You got %d wood, %d metal, and %d food.", woodGained, metalGained, foodGained);

        // Prikaži pop-up poruku
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resource Gain");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Prikazi poruku i čekaj na potvrdu korisnika
        alert.showAndWait();
    }

    public int totalResources() {
        int total = 0;
        for(Soldier soldier : soldiers) {
            total += soldier.getWood();
            total += soldier.getMetal();
            total += soldier.getFood();
        }
        return total;
    }



    private int gatherRandomResources(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}

