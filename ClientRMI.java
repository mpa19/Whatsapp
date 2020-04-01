import java.rmi.*;
interface ClientRMI extends Remote {
    void notifyNewUser(String username) throws RemoteException;
    void sendMsgUser(String userSrc, String message, String date) throws RemoteException;
    void notifyNewGroup(String group) throws RemoteException;
    void sendMsgGroup(String groupName, String userSrc, String message, String date) throws RemoteException;
}
