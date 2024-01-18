package hr.algebra.scythe.util;

import hr.algebra.scythe.model.DiceRoll;
import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.Player;

public class GameStateUtils {
    public static GameState createGameState(Player.Color currentPlayerTurn,
                                            Player redPlayer,
                                            Player bluePlayer,
                                            int numberOfMoves, boolean isGameOver, DiceRoll diceRoll)
    {
        return new GameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, isGameOver, diceRoll);
    }

    public static GameState updateGameState(Player.Color currentPlayerTurn,
                                            Player redPlayer,
                                            Player bluePlayer,
                                            int numberOfMoves,
                                            boolean isGameOver, DiceRoll diceRoll)
    {
        return new GameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, isGameOver, diceRoll);
    }


}
