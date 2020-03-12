
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



// Servidor
public class ChatRMIServer extends ChatRMIServant
{
	public ChatRMIServer() throws RemoteException {};
	public static void main(String args[])
	{
		System.out.println("Cargando Servicio RMI");

		try
		{
				// Cargar el servicio.
				ChatRMIServant servicioChat = new ChatRMIServant();

				ChatRMI Chat = (ChatRMI) UnicastRemoteObject.exportObject(servicioChat, 0);
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
