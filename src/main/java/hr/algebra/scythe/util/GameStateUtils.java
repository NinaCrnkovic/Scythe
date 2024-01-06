package hr.algebra.scythe.util;

import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.Player;

public class GameStateUtils {
    public static GameState createGameState(Player.Color currentPlayerTurn,
                                            Player redPlayer,
                                            Player bluePlayer,
                                            int numberOfMoves, boolean isGameOver)
    {
        GameState gameStateToBeSaved = new GameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, isGameOver);
        return gameStateToBeSaved;
    }

    public static GameState updateGameState(Player.Color currentPlayerTurn,
                                            Player redPlayer,
                                            Player bluePlayer,
                                            int numberOfMoves,
                                            boolean isGameOver)
    {
        GameState gameState = new GameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, isGameOver);
        return gameState;
    }


}
