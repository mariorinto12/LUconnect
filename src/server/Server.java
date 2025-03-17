package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Server {

    private static final int SERVER_PORT = 12345;
    private static final int FILE_TRANSFER_PORT = 54321;
    private static final int MAX_CONCURRENT_CLIENTS = 3;

    private static Semaphore clientSemaphore;
    private static List<PrintWriter> clientWriters = new LinkedList<>();
    private static Queue<Socket> waitingClients = new LinkedList<>();

    public static void main(String[] args) {
        clientSemaphore = new Semaphore(MAX_CONCURRENT_CLIENTS, true);
    
        // Iniciar servidor para transferencia de archivos en otro puerto.
        new Thread(() -> startFileTransferServer()).start();
    
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on port " + SERVER_PORT);
    
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    
                synchronized (clientWriters) {
                    if (clientSemaphore.availablePermits() == 0) {
                        waitingClients.add(clientSocket);
                        int positionInQueue = waitingClients.size();
                        clientWriter.println("Server is full. You are in position #" + positionInQueue + " in the waiting queue.");
                        System.out.println("New client added to waiting queue. Position: " + positionInQueue);
                    } else {
                        clientWriters.add(clientWriter);
                        new Thread(new ClientHandler(clientSocket, clientSemaphore, clientWriter)).start();
                    }
                }
            }
    
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    // Añade este método adicional (faltaba)
    private static void startFileTransferServer() {
        try {
            ServerSocket fileServerSocket = new ServerSocket(FILE_TRANSFER_PORT);
            System.out.println("File Transfer Server started on port " + FILE_TRANSFER_PORT);
    
            while (true) {
                Socket fileClientSocket = fileServerSocket.accept();
                new Thread(new FileTransferHandler(fileClientSocket)).start();
            }
    
        } catch (IOException fileServerException) {
            fileServerException.printStackTrace();
        }
    }

    /**
     * Envía mensajes recibidos a todos los clientes conectados.
     * @param message Mensaje que será enviado a todos los clientes.
     */
    public static void broadcastMessage(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    /**
     * Gestiona la entrada automática de clientes en espera cuando uno se desconecta.
     */
    public static void handleNextWaitingClient() {
        synchronized (clientWriters) {
            if (!waitingClients.isEmpty()) {
                Socket nextClient = waitingClients.poll();
                try {
                    PrintWriter clientWriter = new PrintWriter(nextClient.getOutputStream(), true);
                    clientWriters.add(clientWriter);
                    clientWriter.println("You are now connected to the server.");
                    new Thread(new ClientHandler(nextClient, clientSemaphore, clientWriter)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
