import java.rmi.RemoteException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Client extends UnicastRemoteObject implements ClientRMI {

    public Client() throws RemoteException {}

    public static void main(String[] args)  throws RemoteException {
      try
  		{
        String name = "";
  			// Comprobar si se ha especificado la direccion del servicio de registros
  			String registry = "localhost";
  			if (args.length >=1)
  				registry = args[0];

  			// Formatear la url del registro
  			String registro ="rmi://" + registry + "/ChatRMI";

  			// Buscar el servicio en el registro.
  			Remote servicioRemoto = Naming.lookup(registro);

  			// Convertir a un interfaz
  			ServerRMI servicioChat = (ServerRMI) servicioRemoto;
        Scanner s = new Scanner(System.in);
        Client a = new Client();

        while(true){
          String line[] = s.nextLine().split(" ");
          if(line[0].equals("NewUser")) {
              servicioChat.newUser(line[1], line[2] , "");

          } else if(line[0].equals("Login")) {
              servicioChat.login(line[1], line[2],a);
              name = line[1];

          } else if(line[0].equals("Exit")) {
              servicioChat.logout(a);
              System.exit(0);

          } else if(line[0].equals("Logout")) {
              servicioChat.logout(a);

          } else if(line[0].equals("SendMsg")) {
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
              LocalTime time = LocalTime.now();
              servicioChat.sendMsgUser(name,line[1],line[2], time.format(formatter));

          } else if(line[0].equals("JoinGroup")) {
              servicioChat.joinGroup(line[1], a);

          } else if(line[0].equals(("NewGroup"))){
              servicioChat.newGroup(line[1]);

          } else if (line[0].equals(("SendGroup"))){
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
              LocalTime time = LocalTime.now();
              servicioChat.sendMsgGroup(name, line[1], line[2], time.format(formatter));
          }
        }
    }
    catch (NotBoundException nbe)
    {
      System.err.println("No existe el servicio de Chat en el registro!");
    }
    catch (RemoteException re)
    {
      System.err.println("Error Remoto - " + re);
    }
    catch (Exception e)
    {
      System.err.println("Error - " + e);
    }
    }

    public void notifyNewUser(String username) throws RemoteException {
        System.out.println ("Usuario logeado: " + username);
    }

    public void sendMsgUser(String userSrc, String message, String date) throws RemoteException {
      System.out.println("["+date+"] "+userSrc+": "+message);
    }

    public void sendMsgGroup(String userSrc, String groupName, String message, String date) throws RemoteException {
        System.out.println("["+date+"] ("+groupName+") "+userSrc+": "+message);
    }

    public void notifyNewGroup(String group) throws RemoteException {
        System.out.println ("Nuevo grupo: " + group);
    }

}
