package hr.algebra.scythe.util;

import hr.algebra.scythe.ScytheApplication;
import hr.algebra.scythe.model.ConfigurationKey;
import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.RoleName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkingUtils {
    public static void sendGameStateToServer(GameState gameState) {
        Integer serverPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.SERVER_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);

        try (Socket clientSocket = new Socket(host, serverPort)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            sendSerializableRequest(clientSocket, gameState);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameStateToClient(GameState gameState) {
        Integer clientPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.CLIENT_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);

        try (Socket clientSocket = new Socket(host, clientPort)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());


            sendSerializableRequest(clientSocket, gameState);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequest(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to server: " );
        String confirmation = (String)ois.readObject();
        System.out.println(confirmation);



    }

}
