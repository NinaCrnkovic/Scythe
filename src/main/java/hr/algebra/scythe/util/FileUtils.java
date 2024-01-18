package hr.algebra.scythe.util;

import hr.algebra.scythe.model.DiceRoll;
import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.Player;
import javafx.scene.control.Alert;

import java.io.*;

public class FileUtils {

    public static final String GAME_SAVE_FILE_NAME = "savedGame.dat";

    public static void saveGame(Player.Color currentPlayerTurn,
                                Player redPlayer,
                                Player bluePlayer,
                                int numberOfMoves, boolean isGameOver, DiceRoll diceRoll)
    {
        GameState gameStateToBeSaved = new GameState(currentPlayerTurn, redPlayer, bluePlayer, numberOfMoves, isGameOver, diceRoll);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GAME_SAVE_FILE_NAME)))
        {
            oos.writeObject(gameStateToBeSaved);
            DialogUtils.showDialog(Alert.AlertType.INFORMATION,
                    "Game saved!", "Your game has been successfully saved!");

        }
        catch (IOException e)
        {
            DialogUtils.showDialog(Alert.AlertType.ERROR,
                    "Game not saved!", "Your game has not been successfully saved!");
            throw new RuntimeException(e);
        }
    }

    public static GameState loadGame()
    {
        GameState recoveredGameState;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GAME_SAVE_FILE_NAME)))
        {
            recoveredGameState = (GameState) ois.readObject();
        }
        catch(Exception ex)
        {
            DialogUtils.showDialog(Alert.AlertType.ERROR,
                    "Game not loaded!", "Your game has not been successfully loaded!");
            throw new RuntimeException(ex);
        }

        return recoveredGameState;
    }
}

