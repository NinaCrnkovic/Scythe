package hr.algebra.scythe.util;

import hr.algebra.scythe.ScytheApplication;
import hr.algebra.scythe.model.GameState;
import hr.algebra.scythe.model.NetworkConfiguration;
import hr.algebra.scythe.model.RoleName;
import hr.algebra.scythe.networking.Country;
import hr.algebra.scythe.networking.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkingUtils {
    public static void sendGameStateToServer(GameState gameState) {

        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST, NetworkConfiguration.SERVER_PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());


            sendSerializableRequest(clientSocket, gameState);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameStateToClient(GameState gameState) {

        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST, NetworkConfiguration.CLIENT_PORT)){
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

    public static void sendDiceRollRequestToClient(String message) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST,
                ScytheApplication.loggedInRoleName == RoleName.CLIENT ?
                        NetworkConfiguration.SERVER_PORT : NetworkConfiguration.CLIENT_PORT)) {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendDiceRollRequestToServer(String message) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST,
                ScytheApplication.loggedInRoleName == RoleName.SERVER ?
                        NetworkConfiguration.SERVER_PORT : NetworkConfiguration.CLIENT_PORT)) {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
