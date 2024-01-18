package hr.algebra.scythe.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Player.Color currentPlayerTurn;
    private Player redPlayer;
    private Player bluePlayer;
    private int numberOfMoves;

    private DiceRoll diceRoll;

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    private boolean isGameOver;





    public GameState(Player.Color currentPlayerTurn, Player redPlayer, Player bluePlayer, int numberOfMoves, boolean isGameOver, DiceRoll diceRoll) {
        this.currentPlayerTurn = currentPlayerTurn;
        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;
        this.numberOfMoves = numberOfMoves;
        this.isGameOver = isGameOver;
        this.diceRoll = diceRoll;
    }

    // Getters and setters

    public Player.Color getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public void setCurrentPlayerTurn(Player.Color currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public void setBluePlayer(Player bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public DiceRoll getDiceRoll() {
        return diceRoll;
    }



}
