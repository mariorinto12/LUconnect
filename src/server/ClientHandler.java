package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Semaphore clientSemaphore;
    private BufferedReader clientInputStream;
    private PrintWriter clientOutputStream;

    public ClientHandler(Socket socket, Semaphore semaphore, PrintWriter clientWriter) {
        this.clientSocket = socket;
        this.clientSemaphore = semaphore;
        this.clientOutputStream = clientWriter;
    }

    @Override
    public void run() {
        try {
            clientSemaphore.acquire();
            System.out.println("Client accepted: " + clientSocket.getInetAddress());

            clientInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientOutputStream.println("Welcome to LU-Connect Server.");
            Server.broadcastMessage("A new user has joined the chat!");

            String receivedMessage;
            while ((receivedMessage = clientInputStream.readLine()) != null) {
                System.out.println("Client message: " + receivedMessage);
                Server.broadcastMessage("Client: " + receivedMessage);
            }

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();

        } finally {
            try {
                if (clientInputStream != null) clientInputStream.close();
                if (clientOutputStream != null) clientOutputStream.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }

            clientSemaphore.release();
            System.out.println("Client disconnected.");

            // Llamar a la función para aceptar al siguiente cliente en espera
            Server.handleNextWaitingClient();
        }
    }
}
