package hr.algebra.scythe.model;

import hr.algebra.scythe.util.Constants;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;



public class Soldier implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Image image; // transient označava da ovo polje neće biti serijalizirano
    private String imagePath;     // putanja slike koja će biti serijalizirana

    private int x, y;

    private int wood;
    private int metal;
    private int food;

    private final Player.Color color;

    public Soldier(Player.Color color, int index, int x, int y) {
        this.color = color;
        this.imagePath = String.format(Constants.PLAYER_IMAGE_PATH_FORMAT, color.name().toLowerCase(), index + 1);
        this.image = loadImageFromPath(imagePath);
        this.x = x;
        this.y = y;
    }

    private Image loadImageFromPath(String imagePath) {
        return new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
    }

    public Image getImage() {
        if (image == null) { // Ako je slika null, učitajte je iz imagePath-a
            image = loadImageFromPath(imagePath);
        }
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

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public void setMetal(int metal) {
        this.metal = metal;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public boolean isAt(int x, int y) {
        return this.x == x && this.y == y;
    }

    public void addWood(int amount) {
        this.wood += amount;
    }


    public int getMetal() {
        return metal;
    }

    public void addMetal(int amount) {
        this.metal += amount;
    }



    public int getFood() {
        return food;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public void resetResources() {
        this.setWood(0);
        this.setMetal(0);
        this.setFood(0);
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


