package hr.algebra.scythe.thread;
import hr.algebra.scythe.model.GameMove;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class GetLastGameMoveThread extends GameMoveThread implements Runnable {

    private Label theLastGameMoveLabel;

    public GetLastGameMoveThread(Label theLastGameMoveLabel) {
        this.theLastGameMoveLabel = theLastGameMoveLabel;
    }

    @Override
    public void run() {
        while(true) {
            GameMove lastGameMove = getLastGameMove();

            Platform.runLater(() -> {
                theLastGameMoveLabel.setText(lastGameMove.getPlayerColor() + " location: x " + lastGameMove.getXCoordinate()+ ", y " + lastGameMove.getYCoordinate()
                        + " " + lastGameMove.getDateTime());
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

