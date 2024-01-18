package hr.algebra.scythe.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameMove implements Serializable {
    private Player.Color playerColor;


    private LocalDateTime dateTime;

    private int xCoordinate;
    private int yCoordinate;

    public GameMove(Player.Color playerColor, int xCoordinate, int yCoordinate, LocalDateTime dateTime) {
        this.playerColor = playerColor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.dateTime = dateTime;
    }


    // Getters and setters for each field
    public Player.Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Player.Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "GameMove{" +
                "playerColor=" + playerColor +
                ", dateTime=" + dateTime +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                '}';
    }
}
