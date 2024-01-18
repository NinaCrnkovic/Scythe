package hr.algebra.scythe.util;

import hr.algebra.scythe.ScytheApplication;
import hr.algebra.scythe.controller.ScytheController;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;
import java.util.List;

public class ChatUtils {


    public static void sendChatMessage(String chatMessage, TextArea chatTextArea) {
        try {
            ScytheController.remoteChatService.sendMessage(ScytheApplication.loggedInRoleName + ": " + chatMessage);

            List<String> chatMessages =
                    ScytheController.remoteChatService.getAllChatMessages();

            chatTextArea.clear();

            for (String message : chatMessages) {
                chatTextArea.appendText(message + "\n");
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void refreshChatMessages(TextArea chatTextArea) {

        chatTextArea.clear();
        try {

            List<String> allChatMessages = ScytheController.remoteChatService.getAllChatMessages();
            for(String message : allChatMessages){
                chatTextArea.appendText(message + "\n");
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
