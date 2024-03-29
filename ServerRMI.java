import java.rmi.*;

public interface ServerRMI extends Remote {
    void newUser (String user, String password, String groupString) throws RemoteException;
    void sendMsgGroup(String userSrc, String group, String message, String date) throws RemoteException;
    void newGroup(String group) throws RemoteException;
    void sendMsgUser(String userSrc, String userDst, String message, String date) throws RemoteException;

    void leaveGroup(String group, String username, ClientRMI listener) throws RemoteException;
    void login(String user, String password, ClientRMI listener) throws RemoteException;
    void logout(ClientRMI listener) throws RemoteException;
    void joinGroup(String name, String username, ClientRMI listener) throws RemoteException;
}
