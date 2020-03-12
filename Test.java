import java.rmi.RemoteException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

public class Test extends UnicastRemoteObject implements ChatListener {

    public Test() throws RemoteException {}

    public static void main(String[] args)  throws RemoteException {
      try
  		{
  			// Comprobar si se ha especificado la direccion del servicio de registros
  			String registry = "localhost";
  			if (args.length >=1)
  				registry = args[0];

  			// Formatear la url del registro
  			String registro ="rmi://" + registry + "/ChatRMI";

  			// Buscar el servicio en el registro.
  			Remote servicioRemoto = Naming.lookup(registro);

  			// Convertir a un interfaz
  			ChatRMI servicioChat = (ChatRMI) servicioRemoto;
        Scanner s = new Scanner(System.in);
        Test a = new Test();
        while(true){
          String line[] = s.nextLine().split(" ");
          if(line[0].equals("NewUser")) {
              System.out.println(servicioChat.crearUsuario(line[1], line[2]));
          } else if(line[0].equals("Login")) {
              System.out.println(servicioChat.login(line[1], line[2],a));
          } else if(line[0].equals("Exit")) {
              servicioChat.removeChatListener(a);
              System.exit(0);
          } else if(line[0].equals("Logout")) {
              servicioChat.removeChatListener(a);
          } else if(line[0].equals("SendMsg")) {
              servicioChat.sendMessage(line[1],line[2], a);
          } else if(line[0].equals("JoinGroup")) {
              servicioChat.joinGroup(line[1], a);
          } else if(line[0].equals(("NewGroup"))){
              servicioChat.crearGroup(line[1]);
          } else if (line[0].equals(("SendGroup"))){
              servicioChat.sendMessageToGroup(line[1], line[2], a);
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

    public void newLogin (String user) throws RemoteException {
        System.out.println ("Usuario logeado: " + user);
    }

    public void messageToUser(String message) throws RemoteException {
      System.out.println(message);
    }

    public void messageToGroup(String message) throws RemoteException {
        System.out.println(message);
    }

    public void newGroup (String group) throws RemoteException {
        System.out.println ("Nuevo grupo: " + group);
    }

}
