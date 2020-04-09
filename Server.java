
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.lang.SecurityManager;


// Servidor
public class Server extends Servant
{
	public Server() throws RemoteException {};
	public static void main(String args[])
	{
		System.out.println("Cargando Servicio RMI");
		if ( System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		try
		{
				// Cargar el servicio.
				Servant servicioChat = new Servant();

				ServerRMI Chat = (ServerRMI) UnicastRemoteObject.exportObject(servicioChat, 0);
				// Imprimir la ubicacion del servicio.
				/*RemoteRef location = servicioChat.getRef();
				System.out.println(location.remoteToString());*/

				// Enlazar el objeto remoto (stub) con el registro de RMI.
				Registry registry = LocateRegistry.getRegistry();
				registry.bind("ChatRMI", Chat);
				System.err.println("Server ready");


		}
		catch (RemoteException re)
		{
			System.err.println("Remote Error - " + re);
		}
		catch (Exception e)
		{
			System.err.println("Error - " + e);
		}
	}
}
