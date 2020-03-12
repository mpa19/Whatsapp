import java.rmi.*;
interface ChatListener extends Remote {
    void newLogin(String user) throws RemoteException;
    void messageToUser(String message) throws RemoteException;
    void newGroup(String group) throws RemoteException;
    void messageToGroup(String message) throws RemoteException;


}
