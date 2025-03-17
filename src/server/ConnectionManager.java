package LUconnect.src.server;

import java.util.concurrent.Semaphore;

public class ConnectionManager {
    private Semaphore clientSemaphore;

    public ConnectionManager(int maxClients) {
        this.clientSemaphore = new Semaphore(maxClients, true);
    }

    public void acquireConnection() throws InterruptedException {
        // Lógica para aceptar/rechazar clientes según el límite
    }

    public void releaseConnection() {
        // Lógica para liberar una conexión cuando un cliente se desconecta
    }
}