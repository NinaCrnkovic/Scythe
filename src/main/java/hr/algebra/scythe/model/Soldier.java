package hr.algebra.scythe.model;

import hr.algebra.scythe.util.Constants;
import javafx.scene.image.Image;
import java.util.Objects;

public class Soldier {


    private final Image image;
    private int x, y;

    private final Player.Color color;

    public Soldier(Player.Color color, int index, int x, int y) {
        this.color = color;
        String imagePath = String.format(Constants.PLAYER_IMAGE_PATH_FORMAT, color.name().toLowerCase(), index + 1);
        this.image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public Player.Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAt(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Soldier soldier = (Soldier) o;
        return x == soldier.x &&
                y == soldier.y &&
                Objects.equals(image, soldier.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, x, y);
    }
}

