import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Map.*;
import java.util.Map;
import java.util.HashMap;



public class Servant implements ServerRMI {

    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private Map<Usuario,ClientRMI> loggeados = new HashMap<>();
    private Map<String,Vector<Usuario> > groups = new HashMap<>();

    public Servant() throws RemoteException {}

    public Usuario getUsuario(String nombre) {
        for (Usuario user : usuarios) {
            if (user.getNombre().equals(nombre)) return user;
        }
        return null;
    }

    public void login(String user, String contra, ClientRMI listener) throws RemoteException {
        Usuario u = getUsuario(user);
        if(u != null) {
            if(u.getContraseña().equals(contra)) {
                if(!loggeados.containsKey(u) && !loggeados.containsValue(listener)){
                  loggeados.put(u,listener);
                  notificarListenersLogin(user);
                }
            }
        }
    }

    public void newUser (String user, String password, String groupString) throws RemoteException {
        Usuario u = getUsuario(user);
        if (u == null) {
            this.usuarios.add(new Usuario(user, password));
            if(groups.containsKey(groupString)) {
              joinGroup(groupString, user, null);
            }
        }
    }

    public void newGroup(String name) throws RemoteException{
        Vector<Usuario> cL = getUsersGroup(name);
        if(cL == null){
          this.groups.put(name, new Vector<>());
          notificarListenerGroup(name);
        }
    }

    public void joinGroup(String name, String username, ClientRMI listener) throws RemoteException{
        Vector<Usuario> cL = getUsersGroup(name);
        //Mirar si existe el grupo
        if (cL != null) {
          //Mirar si ya esta en el grupo
          Usuario u = getUsuario(username);
          if(!cL.contains(u)){
            cL.add(u);
          }
        }
    }

    public void logout(ClientRMI listener) throws RemoteException {
        Entry<Usuario, ClientRMI> e = contener(listener);
        if(e != null) loggeados.remove(e.getKey());
    }

    //Funcion que comprueba que el usuario del listener esta logueado
    public Entry<Usuario, ClientRMI> contener(ClientRMI listener){
        for(Entry<Usuario, ClientRMI> log: loggeados.entrySet()){
          ClientRMI c = log.getValue();
          if(c.equals(listener)) return log;
        }
        return null;
    }

    public void sendMsgUser(String userSrc, String userDst, String message, String date) throws RemoteException {
      Usuario uSrc = getUsuario(userSrc);
      //Mirar si el que envia el mensaje esta logueado/existe
      if(loggeados.containsKey(uSrc)){

        //Mirar si el usuario al cual va el mensaje esta logueado/existe
        Usuario uDst = getUsuario(userDst);
        if(loggeados.containsKey(uDst)){

          ClientRMI c = loggeados.get(uDst);
          ClientRMI c1 = loggeados.get(uSrc);
          //Mirar si el usuario al que le envias el mensaje eres tu mismo
          if(!c.equals(c1)){
            try {
              c.sendMsgUser(userSrc, message, date);
            }
            catch (RemoteException re) {
              System.out.println (" Listener not accessible, removing listener -" + c);
              loggeados.remove(uDst);
            }
          }
        }
      }
    }

    private void notificarListenersLogin(String user) {
        for(Entry<Usuario, ClientRMI> log: loggeados.entrySet()){
          Usuario u = log.getKey();

          try {
              log.getValue().notifyNewUser(user);
          }
          catch (RemoteException re) {
              System.out.println (" Listener not accessible, removing listener -" + log.getValue());
              loggeados.remove(u);
          }

        }
    }

    private void notificarListenerGroup(String group) {
        for(Entry<Usuario, ClientRMI> log: loggeados.entrySet()){
            try {
                log.getValue().notifyNewGroup(group);
            }
            catch (RemoteException re) {
                System.out.println (" Listener not accessible, removing listener -" + log.getValue());
            }
        }
    }

    public void sendMsgGroup(String userSrc, String group, String message, String date) {
        Vector<Usuario> cL = getUsersGroup(group);
        System.out.println("1");

        if (cL != null) {
          System.out.println("2");

          //ClientRMI uSrc = loggeados.get(getUsuario(userSrc));

          Usuario u = getUsuario(userSrc);
          if(cL.contains(u) && loggeados.containsKey(u)){
            System.out.println("3");

            for (Enumeration e = cL.elements(); e.hasMoreElements(); ) {
                Usuario u1 = (Usuario) e.nextElement();
                System.out.println(u1.getNombre());
                try {
                    if(loggeados.containsKey(u1)) loggeados.get(u1).sendMsgGroup(userSrc, group, message, date);
                } catch (RemoteException re) {
                    System.out.println(" Listener not accessible, removing listener -");
                }
            }
          }
        }
    }

    private Vector<Usuario> getUsersGroup(String group){
        for (Entry<String, Vector<Usuario>> log : groups.entrySet()){
            if(log.getKey().equals(group)) return log.getValue();
        }
        return null;
    }


    public void leaveGroup(String group, String username, ClientRMI listener) {
      for (Entry<String, Vector<Usuario>> log : groups.entrySet()){
          if(log.getKey().equals(group)) {
            log.getValue().remove(listener);
            break;
          }
      }
    }



}
