package hr.algebra.scythe.chat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteChatServiceImpl implements RemoteChatService {

    private final List<String> chatMessages;

    public RemoteChatServiceImpl() {
        chatMessages = new ArrayList<>();
    }
    @Override
    public void sendMessage(String message) throws RemoteException {
        chatMessages.add(message);
    }

    @Override
    public List<String> getAllChatMessages() throws RemoteException {
        return chatMessages;
    }

}
