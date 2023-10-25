package hr.algebra.scythe.model;
import java.util.List;

public class Player {

    public enum Color {
        RED, BLUE
    }

    private Color color;
    private int x;
    private int y;

    public Player(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    /*private PlayerColor color;
    private List<Unit> units;
    private int woodCount;
    private int metalCount;
    private int foodCount;

    public Player(PlayerColor color) {
        this.color = color;
        this.woodCount = 0;
        this.metalCount = 0;
        this.foodCount = 0;
    }



    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getWoodCount() {
        return woodCount;
    }

    public void addWood(int count) {
        this.woodCount += count;
    }

    public int getMetalCount() {
        return metalCount;
    }

    public void addMetal(int count) {
        this.metalCount += count;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void addFood(int count) {
        this.foodCount += count;
    }*/

}

