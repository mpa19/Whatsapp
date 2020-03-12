public class Usuario {
    String nombre;
    String contraseña;
    int grupo;

    public  Usuario(String name, String password){
        this.nombre = name;
        this.contraseña = password;
    }

    public Usuario(String name, String password, int group){
        this.nombre = name;
        this.contraseña = password;
        this.grupo = group;
    }

    public String getNombre(){
        return nombre;
    }

    public String getContraseña(){
        return contraseña;
    }

    public int getGrupo(){
        return grupo;
    }
}
