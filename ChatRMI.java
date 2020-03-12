import java.rmi.*;

public interface ChatRMI extends Remote {

    String login(String user, String contra, ChatListener listener) throws RemoteException;
    String crearUsuario(String user, String contra) throws RemoteException;
    //void addChatListener(ChatListener listener) throws RemoteException;
    void removeChatListener(ChatListener listener) throws RemoteException;
    void sendMessage(String user, String message, ChatListener listener) throws RemoteException;
    void joinGroup(String name, ChatListener listener) throws RemoteException;
    void crearGroup(String group) throws RemoteException;
    void sendMessageToGroup(String group, String message, ChatListener listener) throws RemoteException;
}
