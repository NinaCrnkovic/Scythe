package hr.algebra.scythe.model;

import java.io.Serializable;

public class DiceRoll implements Serializable {
    private static final long serialVersionUID = 1L;

    private int redRoll;
    private int blueRoll;

    private String winner;
    public DiceRoll(int redRoll, int blueRoll, String winner) {
        this.redRoll = redRoll;
        this.blueRoll = blueRoll;
        this.winner = winner;
    }
    public DiceRoll() {

    }

    public int getRedRoll() {
        return redRoll;
    }

    public int getBlueRoll() {
        return blueRoll;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setRedRoll(int redRoll) {
        this.redRoll = redRoll;
    }

    public void setBlueRoll(int blueRoll) {
        this.blueRoll = blueRoll;
    }
}
