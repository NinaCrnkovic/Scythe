package hr.algebra.scythe.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteChatService extends Remote {

    String REMOTE_CHAT_OBJECT_NAME = "hr.algebra.rmi.chat.service";
    void sendMessage(String message) throws RemoteException;
    List<String> getAllChatMessages() throws RemoteException;

}
