import java.rmi.RemoteException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.lang.SecurityManager;


public class Client extends UnicastRemoteObject implements ClientRMI {

    public Client() throws RemoteException {}

    public static void main(String[] args)  throws RemoteException {
      if ( System.getSecurityManager() == null)
			   System.setSecurityManager(new SecurityManager());

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
  			ServerRMI servicioChat = (ServerRMI) servicioRemoto;
        Scanner s = new Scanner(System.in);
        Client a = new Client();
        String loginName = "";

        while(true){
          String line[] = s.nextLine().split(" ");
          if(line[0].equals("NewUser")) {
              if(line.length == 4) servicioChat.newUser(line[1], line[2] , line[3], a);
              else if(line.length == 3) servicioChat.newUser(line[1], line[2] , "", a);

          } else if(line[0].equals("Login")) {
              if(line.length == 3){
                servicioChat.login(line[1], line[2],a);
                loginName = line[1];
              }

          } else if(line[0].equals("Exit")) {
              servicioChat.logout(a);
              System.exit(0);

          } else if(line[0].equals("Logout")) {
              servicioChat.logout(a);
              loginName = "";

          } else if(line[0].equals("SendMsg")) {
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
              LocalTime time = LocalTime.now();
              if(line.length > 2) {
                if(line[1].equals("-g")){
                  String msg = "";
                  for(int i = 3; i<line.length; i++){
                    msg += line[i];
                    msg += " ";
                  }
                  servicioChat.sendMsgGroup(loginName, line[2], msg, time.format(formatter));
                } else {
                  String msg = "";
                  for(int i = 2; i<line.length; i++){
                    msg += line[i];
                    msg += " ";
                  }
                  servicioChat.sendMsgUser(loginName,line[1],msg, time.format(formatter));
                }

              }

          } else if(line[0].equals("JoinGroup")) {
            if(line.length == 2) servicioChat.joinGroup(line[1], a);

          } else if(line[0].equals(("NewGroup"))){
              if(line.length == 2) servicioChat.newGroup(line[1]);

          } else if (line[0].equals(("LeaveGroup"))){
              if(line.length == 2) servicioChat.leaveGroup(line[1], a);
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
        //if(name.equals(username)) loginName = username;
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
