package hr.algebra.scythe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScytheApplication extends Application {

    private static final String CSS_PATH = "/view/styles/scythe.css";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ScytheApplication.class.getResource("view/scythe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());

        stage.setTitle("Scythe");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
