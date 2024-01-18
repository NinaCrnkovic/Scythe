package hr.algebra.scythe;


import hr.algebra.scythe.controller.ScytheController;
import hr.algebra.scythe.model.*;


import hr.algebra.scythe.util.BoardService;
import hr.algebra.scythe.util.ConfigurationReader;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ScytheApplication extends Application {

    private static ScytheApplication instance;
    private static Scene mainScene;


    private static final String CSS_PATH = "/view/styles/scythe.css";
    public static RoleName loggedInRoleName;
    private static BoardService boardService;
    private ScytheController scytheController;
    public static ScytheApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(ScytheApplication.class.getResource("view/scythe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scytheController = fxmlLoader.getController();

        stage.setTitle(loggedInRoleName.name() + " - Scythe");
        stage.setScene(scene);
        stage.show();
    }

    public ScytheController getController() {
        return scytheController;
    }




    public static void main(String[] args) {
        String inputRoleName = args[0];
        loggedInRoleName = RoleName.CLIENT;

        for (RoleName rn : RoleName.values()) {
            if (rn.name().equals(inputRoleName)) {
                loggedInRoleName = rn;
                break;
            }
        }

        new Thread(Application::launch).start();


        if (loggedInRoleName == RoleName.SERVER) {

            acceptRequestsAsServer();

        } else if (loggedInRoleName == RoleName.CLIENT){

            acceptRequestsAsClient();

        }
    }

    /*private static void acceptRequestsAsServer() {
        try (ServerSocket serverSocket = new ServerSocket(NetworkConfiguration.SERVER_PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                new Thread(() ->  processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private static void acceptRequestsAsServer() {

        Integer serverPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.SERVER_PORT);

        try (ServerSocket serverSocket = new ServerSocket(serverPort)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                new Thread(() ->  processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private static void acceptRequestsAsClient() {
        try (ServerSocket serverSocket = new ServerSocket(NetworkConfiguration.CLIENT_PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                new Thread(() ->  processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static void acceptRequestsAsClient() {
        Integer clientPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.CLIENT_PORT);

        try (ServerSocket serverSocket =
                     new ServerSocket(clientPort)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                new Thread(() ->  processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            GameState gameState = (GameState) ois.readObject();
            Platform.runLater(() -> {
                ScytheApplication app = ScytheApplication.getInstance();
                ScytheController scytheController = app.getController();
                scytheController.updateGameBoard(gameState);
            });

            System.out.println("The game state received");
            oos.writeObject("The game state received confirmation");
            oos.flush();  // Ensure the data is sent
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static Scene getMainScene() {
        return mainScene;
    }




}
