package hr.algebra.scythe.thread;

import hr.algebra.scythe.model.GameMove;

public class SaveNewGameMoveThread extends GameMoveThread implements Runnable {

    private GameMove gameMove;

    public SaveNewGameMoveThread(GameMove gameMove) {
        this.gameMove = gameMove;
    }

    @Override
    public void run() {
        saveNewGameMove(gameMove);
    }
}
