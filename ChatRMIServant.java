import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Map.*;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ChatRMIServant implements ChatRMI {

    private ArrayList<Usuario> usuarios = new ArrayList<>();
    //private Vector<ChatListener> listaListeners = new Vector<>();
    private Map<Usuario,ChatListener> loggeados = new HashMap<>();
    private Map<String,Vector<ChatListener> > groups = new HashMap<>();

    public ChatRMIServant() throws RemoteException {}

    public Usuario getUsuario(String nombre) {
        for (Usuario user : usuarios) {
            if (user.getNombre().equals(nombre)) return user;
        }
        return null;
    }

    public String login(String user, String contra, ChatListener listener) throws RemoteException {
        Usuario u = getUsuario(user);
        if(u != null) {
            if(u.getContraseña().equals(contra)) {
                if(!loggeados.containsKey(u) && !loggeados.containsValue(listener)){
                  notificarListenersLogin(user);
                  loggeados.put(u,listener);
                  return "Login correcte";
                }
            }
        }
        return "Usuario o contraseña erroneos";
    }

    public String crearUsuario(String user, String contra) throws RemoteException {
        Usuario u = getUsuario(user);
        if (u == null) {
            this.usuarios.add(new Usuario(user, contra));
            return "Usuario creado correctamente";
        }
        return "Username '" + user + "' ya esta en uso";
    }

    /*public void addChatListener(ChatListener listener) throws RemoteException {
        listaListeners.add(listener);
    }*/

    public void crearGroup(String name) throws RemoteException{
        this.groups.put(name, new Vector<>());
        notificarListenerGroup(name);
    }

    public void joinGroup(String name, ChatListener listener) throws RemoteException{
        for(Entry<String, Vector<ChatListener>> log: groups.entrySet()){
            String a = log.getKey();
            if(a.equals(name)) log.getValue().add(listener);
        }
    }

    public void removeChatListener(ChatListener listener) throws RemoteException {
        //listaListeners.remove(listener);
        Entry<Usuario, ChatListener> e = contener(listener);
        if(e != null) loggeados.remove(e.getKey());
    }

    //Funcion que comprueba que el usuario del listener esta logueado
    public Entry<Usuario, ChatListener> contener(ChatListener listener){
        for(Entry<Usuario, ChatListener> log: loggeados.entrySet()){
          ChatListener c = log.getValue();
          if(c.equals(listener)) return log;
        }
        return null;
    }

    public void sendMessage(String user, String message, ChatListener listener) throws RemoteException {
      Usuario u = getUsuario(user);
      //Mirar el usuario el cual envia el mensaje
      Entry<Usuario, ChatListener> e = contener(listener);

      //Mirar si el que envia el mensaje esta logueado/existe
      if(e != null){
        Usuario log = e.getKey();
        //Mirar si el usuario al cual va el mensaje esta logueado/existe
        if(loggeados.containsKey(u)){
          ChatListener c = loggeados.get(u);
          //Mirar si el usuario al que le envias el mensaje eres tu mismo
          if(!c.equals(listener)){
            try {
              LocalTime time = LocalTime.now();
              String content = "["+time+"] "+log.getNombre()+": "+message;
              c.messageToUser(content);
            }
            catch (RemoteException re) {
              System.out.println (" Listener not accessible, removing listener -" + c);
              loggeados.remove(u);
            }
          }
        }
      }
    }

    private void notificarListenersLogin(String user) {
        for(Entry<Usuario, ChatListener> log: loggeados.entrySet()){
          Usuario u = log.getKey();

          if(!u.getNombre().equals(user)){
            try {
                log.getValue().newLogin(user);
            }
            catch (RemoteException re) {
                System.out.println (" Listener not accessible, removing listener -" + log.getValue());
                loggeados.remove(u);
            }
          }
        }
        /*for (Enumeration e = listaListeners.elements(); e.hasMoreElements(); )
        {
            ChatListener listener = (ChatListener) e.nextElement();
            try {
                listener.newLogin(user);
            }
            catch (RemoteException re) {
                System.out.println (" Listener not accessible, removing listener -" + listener);
                listaListeners.remove( listener );
            }
        }*/
    }

    private void notificarListenerGroup(String group) {
        for(Entry<Usuario, ChatListener> log: loggeados.entrySet()){
            Usuario u = log.getKey();

            if(!u.getNombre().equals(group)){
                try {
                    log.getValue().newGroup(group);
                }
                catch (RemoteException re) {
                    System.out.println (" Listener not accessible, removing listener -" + log.getValue());
                    loggeados.remove(u);
                }
            }
        }
    }

    public void sendMessageToGroup(String group, String message, ChatListener listener) {
        Vector<ChatListener> cL = getListersGroup(group);
        if (cL != null) {
            for (Enumeration e = cL.elements(); e.hasMoreElements(); ) {
                ChatListener listener1 = (ChatListener) e.nextElement();
                try {
                    listener1.messageToGroup(message);
                } catch (RemoteException re) {
                    System.out.println(" Listener not accessible, removing listener -" + listener);
                    cL.remove(listener);
                }
            }
        }
    }

    private Vector<ChatListener> getListersGroup(String group){
        for (Entry<String, Vector<ChatListener>> log : groups.entrySet()){
            if(log.getKey().equals(group)) return log.getValue();
        }
        return null;
    }



}
