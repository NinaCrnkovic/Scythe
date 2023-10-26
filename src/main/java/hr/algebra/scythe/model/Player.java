package hr.algebra.scythe.model;
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
}

