package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to LU-Connect Server.");

            BufferedReader serverInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter serverOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            // Start thread to listen for messages from server
            new Thread(new MessageListener(serverInputStream)).start();

            System.out.println(serverInputStream.readLine());  // Welcome message

            String userMessage;
            while (true) {
                userMessage = userInputReader.readLine();
                if (userMessage.equalsIgnoreCase("exit")) {
                    break;
                }
                serverOutputStream.println(userMessage);
            }

            clientSocket.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
