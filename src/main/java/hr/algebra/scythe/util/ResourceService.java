package hr.algebra.scythe.util;

import hr.algebra.scythe.model.Soldier;
import hr.algebra.scythe.model.Tile;
import javafx.scene.control.Alert;

public class ResourceService {
    public void gatherResourcesFromTile(Soldier soldier, Tile tile) {
        int woodGained = 0;
        int metalGained = 0;
        int foodGained = 0;

        switch (tile.getType()) {
            case FOREST:
                woodGained = gatherRandomResources(1, 3);
                break;
            case MOUNTAIN:
                metalGained = gatherRandomResources(1, 3);
                break;
            case VILLAGE:
                foodGained = gatherRandomResources(1, 3);
                break;
            default:
                break;
        }

        soldier.addWood(woodGained);
        soldier.addMetal(metalGained);
        soldier.addFood(foodGained);

        StringBuilder message = new StringBuilder("You got ");
        if (woodGained > 0) {
            message.append(woodGained).append(" wood");
        }
        if (metalGained > 0) {
            if (woodGained > 0) {
                message.append(", ");
            }
            message.append(metalGained).append(" metal");
        }
        if (foodGained > 0) {
            if (woodGained > 0 || metalGained > 0) {
                message.append(", ");
            }
            message.append(foodGained).append(" food");
        }

        message.append(".");

        // Prikaži pop-up poruku
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resource Gain");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());

        // Prikazi poruku i čekaj na potvrdu korisnika
        alert.showAndWait();
    }

    public static int gatherRandomResources(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void transferResources(Soldier attacker, Soldier defender) {
        // Ispis stanja resursa prije transfera
        System.out.println("Before Transfer:");
        System.out.println("Attacker Resources - Wood: " + attacker.getWood() + ", Metal: " + attacker.getMetal() + ", Food: " + attacker.getFood());
        System.out.println("Defender Resources - Wood: " + defender.getWood() + ", Metal: " + defender.getMetal() + ", Food: " + defender.getFood());

        attacker.addWood(defender.getWood());
        attacker.addMetal(defender.getMetal());
        attacker.addFood(defender.getFood());

        defender.setWood(0);
        defender.setMetal(0);
        defender.setFood(0);

        // Ispis stanja resursa nakon transfera
        System.out.println("After Transfer:");
        System.out.println("Attacker Resources - Wood: " + attacker.getWood() + ", Metal: " + attacker.getMetal() + ", Food: " + attacker.getFood());
        System.out.println("Defender Resources - Wood: " + defender.getWood() + ", Metal: " + defender.getMetal() + ", Food: " + defender.getFood());
    }


}
