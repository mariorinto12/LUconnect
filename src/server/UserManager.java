package LUconnect.src.server;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, String> users;  // Almacena usuarios (nombre, contraseña)

    public UserManager() {
        this.users = new HashMap<>();
    }

    public boolean registerUser(String username, String password) {
        // Lógica para registrar un usuario
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        // Lógica para verificar credenciales
        return false;
    }
}