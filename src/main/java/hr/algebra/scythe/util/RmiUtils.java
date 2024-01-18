package hr.algebra.scythe.util;

import hr.algebra.scythe.chat.RemoteChatService;
import hr.algebra.scythe.chat.RemoteChatServiceImpl;
import hr.algebra.scythe.controller.ScytheController;
import hr.algebra.scythe.model.ConfigurationKey;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiUtils {

    public static void startRmiRemoteChatServer(){
        Integer rmiPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RMI_PORT);
        Integer randomPortHint = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RANDOM_PORT_HINT);
        try {
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            ScytheController.remoteChatService = new RemoteChatServiceImpl();
            RemoteChatService skeleton = (RemoteChatService) UnicastRemoteObject.exportObject( ScytheController.remoteChatService, randomPortHint);
            registry.rebind(RemoteChatService.REMOTE_CHAT_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static void startRmiRemoteChatClient(){
        Integer rmiPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RMI_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);
        try {

            Registry registry = LocateRegistry.getRegistry(host, rmiPort);
            ScytheController.remoteChatService = (RemoteChatService) registry.lookup(RemoteChatService.REMOTE_CHAT_OBJECT_NAME);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
