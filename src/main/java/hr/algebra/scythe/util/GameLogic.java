package hr.algebra.scythe.util;
import hr.algebra.scythe.model.Player;
import hr.algebra.scythe.model.Soldier;
public class GameLogic {






    public static boolean isAdjacent(Soldier soldier, int x, int y) {
        int dx = x - soldier.getX();
        int dy = y - soldier.getY();
        return (Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0);
    }

    public static boolean isValidMove(int x, int y, int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }




}
